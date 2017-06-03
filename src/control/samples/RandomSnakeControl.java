package control.samples;

import model.*;

import java.util.*;

import control.*;

/**
 * A randomly moving snake.
 * @author cryingshadow
 */
public class RandomSnakeControl implements SnakeControl {

    /**
     * Random number generator.
     */
    private final Random random = new Random();

    @Override
    public String getName() {
        return "Random";
    }

    @Override
    public Direction nextDirection(final Maze maze, final int xPos, final int yPos) {
        switch (this.random.nextInt(4)) {
            case 0:
                return Direction.DOWN;
            case 1:
                return Direction.LEFT;
            case 2:
                return Direction.RIGHT;
            default:
                return Direction.UP;
        }
    }

}
