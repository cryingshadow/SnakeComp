package control.samples;

import control.*;
import model.*;

/**
 * A snake that moves up and left in an alternating way.
 * @author cryingshadow
 */
public class UpLeftSnakeControl implements SnakeControl {

    /**
     * Has the last step been taken in horizontal direction?
     */
    private boolean horizontal = true;

    @Override
    public String getName() {
        return "UpLeft";
    }

    @Override
    public Direction nextDirection(final Maze maze, final int xPos, final int yPos) {
        this.horizontal = !this.horizontal;
        if (this.horizontal) {
            return Direction.LEFT;
        } else {
            return Direction.UP;
        }
    }

}
