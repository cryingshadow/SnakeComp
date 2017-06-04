package control;

import java.util.*;

import generators.*;
import model.*;

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
    public static Maze toMaze(
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
                        if (walls.isWall(curPos)) {
                            curField = new Field(FieldType.COLLISION, Optional.empty());
                        } else {
                            final Snake actualSnake = snake.get();
                            final SnakePart part =
                                new SnakePart(actualSnake.getColor(), actualSnake.getHead().equals(curPos));
                            curField = new Field(FieldType.SNAKE, Optional.of(part));
                        }
                    } else {
                        curField = new Field(FieldType.COLLISION, Optional.empty());
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
        return new Maze(maze);
    }

    /**
     * The positions of food.
     */
    private final Food food;

    /**
     * The settings.
     */
    private final Settings settings;

    /**
     * The snakes.
     */
    private final Snakes snakes;

    /**
     * The positions of walls.
     */
    private final Walls walls;

    /**
     * Generate an instance of a competition.
     * @param settings The settings.
     * @param snakeControls The participating snake controls.
     */
    public CompetitionControl(final Settings settings, final Collection<SnakeControl> snakeControls) {
        this.settings = settings;
        this.walls =
            new Walls(
                new MazeGenerator().generateMaze(
                    this.settings.getWidth(),
                    this.settings.getHeight(),
                    this.settings.isArena(),
                    this.settings.getWalls()
                )
            );
        this.food = new Food(new FoodGenerator(this.settings.getFoodPerSnake() * snakeControls.size()));
        this.snakes =
            new Snakes(
                new SnakeGenerator().generateSnakes(
                    snakeControls,
                    CompetitionControl.toMaze(
                        this.getWidth(),
                        this.getHeight(),
                        new Snakes(Collections.emptyList()),
                        this.walls,
                        this.food
                    ),
                    this.settings
                )
            );
        this.food.generateFood(
            CompetitionControl.toMaze(this.getWidth(), this.getHeight(), this.snakes, this.walls, this.food)
        );
    }

    /**
     * @return The current maze.
     */
    public Maze getCurrentMaze() {
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

}
