package control;

import java.util.*;

import generators.*;
import model.*;

/**
 *
 * @author cryingshadow
 */
public class CompetitionControl {

    /**
     * The positions of food.
     */
    private final Food food;

    /**
     * The settings.
     */
    private final GlobalSettings settings;

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
     */
    public CompetitionControl() {
        this.settings = new GlobalSettings();
        this.walls =
            new Walls(
                new MazeGenerator().generateMaze(
                    this.settings.getWidth(),
                    this.settings.getHeight(),
                    this.settings.isArena(),
                    this.settings.getWalls()
                )
            );
        this.food = new Food(Collections.emptyList());
        this.snakes = new Snakes(Collections.emptyList());
    }

    /**
     * @return The current maze.
     */
    public Maze getCurrentMaze() {
        final int width = this.getWidth();
        final int height = this.getHeight();
        final Field[][] maze = new Field[height][width];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                final Position curPos = new Position(x, y);
                final Field curField;
                if (this.snakes.isSnake(curPos)) {
                    final Optional<Snake> snake = this.snakes.getUniqueSnakeAt(curPos);
                    if (snake.isPresent()) {
                        if (this.walls.isWall(curPos)) {
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
                } else if (this.walls.isWall(curPos)) {
                    curField = new Field(FieldType.WALL, Optional.empty());
                } else if (this.food.isFood(curPos)) {
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
