package control.samples;

import model.*;

import java.util.*;
import java.util.stream.*;

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
        final List<Position> surrounding =
            maze
            .getSurroundingPositions(xPos, yPos)
            .stream()
            .filter(pos -> !maze.getField(pos).type().isObstacle)
            .collect(Collectors.toList());
        if (surrounding.isEmpty()) {
            return Direction.UP;
        }
        return new Position(xPos, yPos).computeDirection(surrounding.get(this.random.nextInt(surrounding.size())));
    }

}
