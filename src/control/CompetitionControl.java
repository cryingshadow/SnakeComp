package control;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

import javax.swing.event.*;

import generators.*;
import model.*;
import util.*;

/**
 * The main control of a competition.
 * @author cryingshadow
 */
public class CompetitionControl {

    /**
     * @param width The width of the maze (including arena border if set).
     * @param height The height of the maze (including arena border if set).
     * @param snakes The snakes in the maze.
     * @param walls The walls in the maze.
     * @param food The food in the maze.
     * @return The specified maze.
     */
    private static Field[][] toMaze(
        final int width,
        final int height,
        final Snakes snakes,
        final Walls walls,
        final Food food
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
                            if (actualSnake.getHead().equals(curPos)) {
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

    /**
     * The competition.
     */
    private final Competition competition;

    /**
     * The positions of food.
     */
    private final Food food;

    /**
     * The maze.
     */
    private final Maze maze;

    /**
     * The maze generator.
     */
    private final MazeGenerator mazeGenerator;

    /**
     * The settings.
     */
    private final Settings settings;

    /**
     * The snake controls.
     */
    private final SnakeControls snakeControls;

    /**
     * The snake generator.
     */
    private final SnakeGenerator snakeGenerator;

    /**
     * The snakes.
     */
    private final Snakes snakes;

    /**
     * The positions of walls.
     */
    private final Walls walls;

    /**
     * Create a competition.
     * @param settings The settings.
     * @param maze The maze.
     * @param snakes The snakes.
     * @param snakeControls The snake controls.
     * @param competition The competition.
     */
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
        this.food = new Food(new FoodGenerator());
        this.snakes = snakes;
        this.competition = competition;
        this.maze = maze;
        this.snakeControls = snakeControls;
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

    /**
     * Generates a new maze (yet without food or snakes).
     */
    public void generateMaze() {
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
        this.generateSnakes();
        this.removeSnakePositions();
    }

    /**
     * Loads and initializes the snakes.
     */
    public void loadSnakes() {
        this.snakeControls.setSnakeControls(DynamicCompiler.compileAndLoad(this.settings.getSourceDirectory().get()));
        this.generateSnakes();
        this.removeSnakePositions();
    }

    /**
     * Starts the competition in a new thread.
     */
    public void startCompetition() {
        this.generateSnakes();
        this.food.setAmount(this.settings.getFoodPerSnake() * this.snakes.getAliveSnakes().size());
        this.food.generateFood(new Maze(this.getCurrentMaze()));
        this.maze.setMaze(this.getCurrentMaze());
        this.competition.reset();
        this.competition.setRunning(true);
        final Thread turnThread = new Thread(new TurnControl(this.settings, this.competition, this));
        turnThread.start();
    }

    /**
     * Perform one turn of the competition.
     */
    public void turn() {
        this.food.removeFood(this.maze.getEatenFood());
        final List<Snake> snakesForNextMove =
            this
            .snakes
            .getAllSnakes()
            .stream()
            .map(snake -> this.applyDeath(snake))
            .collect(Collectors.toList());
        final Map<Snake, Pair<Position, Boolean>> nextPositionsOfSnakes =
            snakesForNextMove
            .stream()
            .filter(Snake::isAlive)
            .parallel()
            .collect(Collectors.toMap(Function.identity(), snake -> this.nextPositionOfSnake(snake)));
        this.food.setAmount(this.settings.getFoodPerSnake() * nextPositionsOfSnakes.size());
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

    /**
     * @param snake A snake.
     * @return The specified snake killed if it died on the current maze. The snake unchanged otherwise.
     */
    private Snake applyDeath(final Snake snake) {
        if (!snake.isAlive()) {
            return snake;
        }
        if (snake.isStarved() || snake.isTooSlow()) {
            return snake.kill();
        }
        final Position pos = snake.getHead();
        switch (this.maze.getField(pos.getX(), pos.getY()).getType()) {
            case COLLISION_ON_WALL:
            case COLLISION_ON_FOOD:
            case COLLISION_ON_FREE:
                return snake.kill();
            default:
                return snake;
        }
    }

    /**
     * Generates start positions for the snakes.
     */
    private void generateSnakes() {
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

    /**
     * @return The current maze.
     */
    private Field[][] getCurrentMaze() {
        return CompetitionControl.toMaze(this.getWidth(), this.getHeight(), this.snakes, this.walls, this.food);
    }

    /**
     * @return The height of the maze (including the arena border if set).
     */
    private int getHeight() {
        return this.settings.isArena() ? this.settings.getHeight() + 2 : this.settings.getHeight();
    }

    /**
     * @return The width of the maze (including the arena border if set).
     */
    private int getWidth() {
        return this.settings.isArena() ? this.settings.getWidth() + 2 : this.settings.getWidth();
    }

    /**
     * @param snake A snake.
     * @return The next position where the snake wants to move to and a flag indicating whether the computation took
     *         too long.
     */
    private Pair<Position, Boolean> nextPositionOfSnake(final Snake snake) {
        final Pair<Direction, Boolean> res = snake.getNextDirection(new Maze(this.maze));
        return new Pair<Position, Boolean>(this.wrapPosition(snake.getNextPosition(res.getKey())), res.getValue());
    }

    /**
     * Removes all snake positions.
     */
    private void removeSnakePositions() {
        this.snakes.removePositions();
        this.maze.setMaze(this.getCurrentMaze());
    }

    /**
     * @param pos A position that may be out of bounds.
     * @return The wrapped position if it was out of bounds. The specified position otherwise.
     */
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
