package control.samples;

import model.*;

import java.util.*;

import control.*;

/**
 * A randomly moving snake.
 * @author cryingshadow
 */
public class GreedySnakeControl implements SnakeControl {

    /**
     * The snake's name.
     */
    private final String name;

    /**
     * @param name The snake's name.
     */
    public GreedySnakeControl(final String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Direction nextDirection(final Maze maze, final int xPos, final int yPos) {
        final List<Position> food = new ArrayList<Position>(maze.getFood());
        final Position curPos = new Position(xPos, yPos);
        food.sort(
            new Comparator<Position>(){

                @Override
                public int compare(final Position o1, final Position o2) {
                    return o1.manhattanDistance(curPos) - o2.manhattanDistance(curPos);
                }

            }
        );
        final Position foodPos = food.get(0);
        final int foodX = foodPos.getX();
        final int foodY = foodPos.getY();
        if (foodX > xPos) {
            return Direction.RIGHT;
        }
        if (foodX < xPos) {
            return Direction.LEFT;
        }
        if (foodY > yPos) {
            return Direction.DOWN;
        }
        return Direction.UP;
    }

}
