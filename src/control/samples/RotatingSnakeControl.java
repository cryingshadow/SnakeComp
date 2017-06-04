package control.samples;

import control.*;
import model.*;

/**
 * A snake that repeats a sequence of moves such that it rotates around a field.
 * @author cryingshadow
 */
public class RotatingSnakeControl implements SnakeControl {

    /**
     * The moves that are repeated by this control.
     */
    private static final Direction[] MOVES =
        new Direction[]{
            Direction.UP,
            Direction.UP,
            Direction.RIGHT,
            Direction.RIGHT,
            Direction.DOWN,
            Direction.DOWN,
            Direction.LEFT,
            Direction.LEFT
        };

    /**
     * The current index of the next move;
     */
    private int index = 0;

    @Override
    public String getName() {
        return "Rotating";
    }

    @Override
    public Direction nextDirection(final Maze maze, final int xPos, final int yPos) {
        this.index = this.index % RotatingSnakeControl.MOVES.length;
        return RotatingSnakeControl.MOVES[this.index++];
    }

}
