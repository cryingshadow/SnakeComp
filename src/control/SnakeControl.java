package control;

import model.*;

/**
 * A snake control decides in which direction (UP, RIGHT, DOWN, or LEFT) a snake moves next given the current state of
 * the maze (including its content, i.e., food and snakes).
 * @author cryingshadow
 */
public interface SnakeControl {

    /**
     * @return The name of the snake being controlled.
     */
    String getName();

    /**
     * @param maze The maze.
     * @param xPos The position of the snake's head in the maze on the x-axis.
     * @param yPos The position of the snake's head in the maze on the y-axis.
     * @return The direction in which to move next.
     */
    Direction nextDirection(final Maze maze, final int xPos, final int yPos);

}
