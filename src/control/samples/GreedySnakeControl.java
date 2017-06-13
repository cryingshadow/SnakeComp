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
     * @param p1 Some position.
     * @param p2 Some other position.
     * @return The Manhattan distance between the specified positions.
     */
    public static int manhattanDistance(final Position p1, final Position p2) {
        return Math.abs(p1.getX() - p2.getX()) + Math.abs(p1.getY() - p2.getY());
    }

    @Override
    public String getName() {
        return "Greedy";
    }

    @Override
    public Direction nextDirection(final Maze maze, final int xPos, final int yPos) {
        final List<Position> food = new ArrayList<Position>(maze.getFood());
        final Position curPos = new Position(xPos, yPos);
        food.sort(
            new Comparator<Position>(){

                @Override
                public int compare(final Position o1, final Position o2) {
                    return
                        GreedySnakeControl.manhattanDistance(o1, curPos)
                        - GreedySnakeControl.manhattanDistance(o2, curPos);
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
            return Direction.UP;
        }
        return Direction.DOWN;
    }

}
