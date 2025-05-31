package control;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;
import java.util.stream.*;

import javax.swing.event.*;

import generators.*;
import model.*;
import util.*;

public class CompetitionControl {

    private static Field[][] toMaze(
        final int width,
        final int height,
        final Snakes snakes,
        final Walls walls,
        final FoodPositions food
    ) {
        final Field[][] maze = new Field[height][width];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                final Position curPos = new Position(x, y);
                final Field curField;
                if (snakes.isSnake(curPos)) {
                    final Optional<Snake> snake = snakes.getUniqueSnakeAt(curPos);
                    if (snake.isPresent()) {
                        final Snake actualSnake = snake.get();
                        if (actualSnake.selfCollisionAt(curPos)) {
                            curField = new Field(FieldType.COLLISION_ON_FREE, Optional.empty());
                        } else if (walls.isWall(curPos)) {
                            curField = new Field(FieldType.COLLISION_ON_WALL, Optional.empty());
                        } else {
                            if (actualSnake.getHeadPosition().equals(curPos)) {
                                if (food.isFood(curPos)) {
                                    curField =
                                        new Field(FieldType.SNAKE_HEAD_EATING, Optional.of(actualSnake.getColor()));
                                } else {
                                    curField = new Field(FieldType.SNAKE_HEAD, Optional.of(actualSnake.getColor()));
                                }
                            } else {
                                curField = new Field(FieldType.SNAKE_BODY, Optional.of(actualSnake.getColor()));
                            }
                        }
                    } else if (food.isFood(curPos)) {
                        curField = new Field(FieldType.COLLISION_ON_FOOD, Optional.empty());
                    } else {
                        curField = new Field(FieldType.COLLISION_ON_FREE, Optional.empty());
                    }
                } else if (walls.isWall(curPos)) {
                    curField = new Field(FieldType.WALL, Optional.empty());
                } else if (food.isFood(curPos)) {
                    curField = new Field(FieldType.FOOD, Optional.empty());
                } else {
                    curField = new Field(FieldType.FREE, Optional.empty());
                }
                maze[y][x] = curField;
            }
        }
        return maze;
    }

    private final Competition competition;

    private ExecutorService executor;

    private final FoodPositions food;

    private final Maze maze;

    private final MazeGenerator mazeGenerator;

    private final Settings settings;

    private final SnakeControls snakeControls;

    private final SnakeGenerator snakeGenerator;

    private final Snakes snakes;

    private final Walls walls;

    public CompetitionControl(
        final Settings settings,
        final Maze maze,
        final Snakes snakes,
        final SnakeControls snakeControls,
        final Competition competition
    ) {
        this.settings = settings;
        this.mazeGenerator = new MazeGenerator();
        this.snakeGenerator = new SnakeGenerator();
        this.walls = new Walls(Collections.emptyList());
        this.food = new FoodPositions(new FoodGenerator());
        this.snakes = snakes;
        this.competition = competition;
        this.maze = maze;
        this.snakeControls = snakeControls;
        this.executor = Executors.newFixedThreadPool(Math.max(1, snakeControls.getSnakeControls().size()));
        this.snakes.addChangeListener(
            new ChangeListener() {

                @Override
                public void stateChanged(final ChangeEvent e) {
                    if (CompetitionControl.this.snakes.getAliveSnakes().isEmpty()) {
                        CompetitionControl.this.competition.setRunning(false);
                    }
                }

            }
        );
    }

    public void generateWalls() {
        this.snakes.clear();
        this.food.clear();
        this.walls.setWalls(
            this.mazeGenerator.generateMaze(
                this.settings.getWidth(),
                this.settings.getHeight(),
                this.settings.isArena(),
                this.settings.getWalls()
            )
        );
        this.generateSnakePositions();
        this.removeSnakePositions();
    }

    public void initSnakes(final List<SnakeControl> controls) {
        this.snakeControls.setSnakeControls(controls);
        this.executor.shutdown();
        try {
            if (!this.executor.awaitTermination(800, TimeUnit.MILLISECONDS)) {
                this.executor.shutdownNow();
            }
        } catch (final InterruptedException e) {
            this.executor.shutdownNow();
        }
        this.executor = Executors.newFixedThreadPool(Math.max(1, controls.size()));
        this.generateSnakePositions();
        this.removeSnakePositions();
    }

    public void loadSnakes() {
        this.initSnakes(DynamicCompiler.compileAndLoad(this.settings.getSourceDirectory().get()));
    }

    public void startCompetition() {
        this.generateSnakePositions();
        this.food.setMinAmount(this.settings.getFoodPerSnake() * this.snakes.getAliveSnakes().size());
        this.food.generateFood(new Maze(this.getCurrentMaze()));
        this.maze.setMaze(this.getCurrentMaze());
        this.competition.reset();
        this.competition.setRunning(true);
        final Thread turnThread = new Thread(new TurnControl(this.settings, this.competition, this));
        turnThread.start();
    }

    public void turn() {
        this.food.removeFood(this.maze.getEatenFood());
        final List<Snake> snakesForNextMove =
            this
            .snakes
            .getAllSnakes()
            .stream()
            .map(snake -> this.respawn(snake))
            .map(snake -> this.applyDeath(snake))
            .collect(Collectors.toList());
        final Map<Snake, Pair<Position, Boolean>> nextPositionsOfSnakes =
            snakesForNextMove
            .stream()
            .filter(Snake::isAlive)
            .parallel()
            .collect(Collectors.toMap(Function.identity(), snake -> this.nextPositionOfSnake(snake)));
        this.food.setMinAmount(this.settings.getFoodPerSnake() * nextPositionsOfSnakes.size());
        final Map<Position, Long> occurrences =
            nextPositionsOfSnakes
            .entrySet()
            .stream()
            .map(Map.Entry::getValue)
            .map(Pair::getKey)
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        this.snakes.setSnakes(
            snakesForNextMove.stream().map(
                snake -> {
                    if (!snake.isAlive()) {
                        return snake;
                    }
                    final Pair<Position, Boolean> nextPosWithFlag = nextPositionsOfSnakes.get(snake);
                    final Position nextPos = nextPosWithFlag.getKey();
                    if (occurrences.get(nextPos) == 1 && this.food.isFood(nextPos)) {
                        return snake.growingMove(nextPosWithFlag);
                    }
                    return snake.normalMove(nextPosWithFlag);
                }
            ).collect(Collectors.toList()));
        this.food.generateFood(new Maze(this.getCurrentMaze()));
        this.maze.setMaze(this.getCurrentMaze());
        this.competition.increaseTurns();
        this.competition.setRunning(this.competition.isRunning() && !this.snakes.getAliveSnakes().isEmpty());
    }

    private Snake applyDeath(final Snake snake) {
        if (!snake.isAlive()) {
            return snake;
        }
        if (snake.isStarved() || snake.isTooSlow()) {
            return snake.kill();
        }
        final Position pos = snake.getHeadPosition();
        switch (this.maze.getField(pos.getX(), pos.getY()).getType()) {
            case COLLISION_ON_WALL:
            case COLLISION_ON_FOOD:
            case COLLISION_ON_FREE:
                return snake.kill();
            default:
                return snake;
        }
    }

    private void generateSnakePositions() {
        this.food.clear();
        this.snakes.setSnakes(
            this.snakeGenerator.generateSnakes(
                this.snakeControls.getSnakeControls(),
                new Maze(
                    CompetitionControl.toMaze(this.getWidth(), this.getHeight(), new Snakes(), this.walls, this.food)
                ),
                this.settings
            )
        );
        this.maze.setMaze(this.getCurrentMaze());
    }

    private Field[][] getCurrentMaze() {
        return CompetitionControl.toMaze(this.getWidth(), this.getHeight(), this.snakes, this.walls, this.food);
    }

    private int getHeight() {
        return this.settings.isArena() ? this.settings.getHeight() + 2 : this.settings.getHeight();
    }

    private int getWidth() {
        return this.settings.isArena() ? this.settings.getWidth() + 2 : this.settings.getWidth();
    }

    private Pair<Position, Boolean> nextPositionOfSnake(final Snake snake) {
        final Pair<Direction, Boolean> res = snake.getNextDirection(new Maze(this.maze), this.executor);
        return new Pair<Position, Boolean>(this.wrapPosition(snake.getNextPosition(res.getKey())), res.getValue());
    }

    private void removeSnakePositions() {
        this.snakes.removePositions();
        this.maze.setMaze(this.getCurrentMaze());
    }

    private Snake respawn(final Snake snake) {
        if (!this.settings.isRespawning() || snake.isAlive()) {
            return snake;
        }
        return snake.respawn(this.snakeGenerator.getRespawnPosition(this.maze), this.settings.getInitialSnakeLength());
    }

    private Position wrapPosition(final Position pos) {
        final int width = this.getWidth();
        final int height = this.getHeight();
        int x = pos.getX();
        int y = pos.getY();
        if (x >= 0 && x < width && y >= 0 && y < height) {
            return pos;
        }
        while (x < 0) {
            x += width;
        }
        x = x % width;
        while (y < 0) {
            y += height;
        }
        y = y % height;
        return new Position(x, y);
    }

}
