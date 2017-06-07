package control;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

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
                        if (actualSnake.selfCollision()) {
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
     * @param competition The competition.
     */
    public CompetitionControl(final Settings settings, final Maze maze, final Snakes snakes, final Competition competition) {
        this.settings = settings;
        this.mazeGenerator = new MazeGenerator();
        this.snakeGenerator = new SnakeGenerator();
        this.walls = new Walls(Collections.emptyList());
        this.food = new Food(new FoodGenerator());
        this.snakes = snakes;
        this.competition = competition;
        this.maze = maze;
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
        this.maze.setMaze(
            CompetitionControl.toMaze(this.getWidth(), this.getHeight(), this.snakes, this.walls, this.food)
        );
    }

    /**
     * Initializes the maze by adding food and snakes.
     */
    public void startCompetition() {
        final Collection<SnakeControl> snakeControls =
            DynamicCompiler.compileAndLoad(this.settings.getSourceDirectory().get());
        final int width = this.getWidth();
        final int height = this.getHeight();
        this.food.clear();
        this.snakes.setSnakes(
            this.snakeGenerator.generateSnakes(
                snakeControls,
                new Maze(
                    CompetitionControl.toMaze(
                        width,
                        height,
                        new Snakes(),
                        this.walls,
                        this.food
                    )
                ),
                this.settings
            )
        );
        this.food.setAmount(this.settings.getFoodPerSnake() * snakeControls.size());
        this.food.generateFood(new Maze(CompetitionControl.toMaze(width, height, this.snakes, this.walls, this.food)));
        this.maze.setMaze(CompetitionControl.toMaze(width, height, this.snakes, this.walls, this.food));
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
        final Map<Snake, Position> nextPositionsOfSnakes =
            snakesForNextMove
            .stream()
            .filter(Snake::isAlive)
            .collect(Collectors.toMap(Function.identity(), snake -> this.nextPositionOfSnake(snake)));
        this.food.setAmount(this.settings.getFoodPerSnake() * nextPositionsOfSnakes.size());
        final Map<Position, Long> occurrences =
            nextPositionsOfSnakes
            .entrySet()
            .stream()
            .map(Map.Entry::getValue)
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        this.snakes.setSnakes(
            snakesForNextMove.stream().map(
                snake -> {
                    if (!snake.isAlive()) {
                        return snake;
                    }
                    final Position nextPos = nextPositionsOfSnakes.get(snake);
                    if (occurrences.get(nextPos) == 1 && this.food.isFood(nextPos)) {
                        return snake.growingMove(nextPos);
                    }
                    return snake.normalMove(nextPos);
                }
            ).collect(Collectors.toList()));
        final int width = this.getWidth();
        final int height = this.getHeight();
        this.food.generateFood(new Maze(CompetitionControl.toMaze(width, height, this.snakes, this.walls, this.food)));
        this.maze.setMaze(CompetitionControl.toMaze(width, height, this.snakes, this.walls, this.food));
        this.competition.increaseTurns();
        this.competition.setRunning(!this.snakes.getAliveSnakes().isEmpty());
    }

    /**
     * @param snake A snake.
     * @return The specified snake killed if it died on the current maze. The snake unchanged otherwise.
     */
    private Snake applyDeath(final Snake snake) {
        if (!snake.isAlive()) {
            return snake;
        }
        if (snake.isStarved()) {
            return snake.kill();
        }
        final Position pos = snake.getHead();
        switch (this.maze.getMaze()[pos.getY()][pos.getX()].getType()) {
            case COLLISION_ON_WALL:
            case COLLISION_ON_FOOD:
            case COLLISION_ON_FREE:
                return snake.kill();
            default:
                return snake;
        }
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
     * @return The next position where the snake wants to move to.
     */
    private Position nextPositionOfSnake(final Snake snake) {
        return this.wrapPosition(snake.getNextPosition(snake.getNextDirection(this.maze)));
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
