package generators;

import java.util.*;

import model.*;

/**
 * Generator of random mazes.
 * @author cryingshadow
 */
public class MazeGenerator {

    /**
     * Random number generator.
     */
    private final Random random = new Random();

    /**
     * @param width The width of the maze.
     * @param height The width of the maze.
     * @param arena Is the maze an arena (i.e., there are walls at its border).
     * @param walls The number of walls that should be in the maze (without the arena border if set).
     * @return A collection of positions for the walls in the maze.
     */
    public Collection<Position> generateMaze(final int width, final int height, final boolean arena, final int walls) {
        if (walls > width * height) {
            throw new IllegalArgumentException("Not enough fields to place the walls!");
        }
        final Set<Position> result = new LinkedHashSet<Position>();
        final int offset;
        final int total;
        if (arena) {
            for (int x = 0; x < width + 2; x++) {
                result.add(new Position(x, 0));
                result.add(new Position(x, height + 1));
            }
            for (int y = 1; y < height + 1; y++) {
                result.add(new Position(0, y));
                result.add(new Position(width + 1, y));
            }
            offset = 1;
            total = walls + result.size();
        } else {
            offset = 0;
            total = walls;
        }
        int todo = total - result.size();
        while (todo > 0) {
            final Collection<Position> wall = this.generateWall(width, height, offset, todo);
            result.addAll(wall);
            todo = total - result.size();
        }
        return result;
    }

    /**
     * @param width The width of the maze.
     * @param height The width of the maze.
     * @param offset An offset for the positions to be generated.
     * @param todo The number of walls left to be added.
     * @return A straight line of walls somewhere in the maze which is at most as long as the remaining walls to be
     *         added.
     */
    private Collection<Position> generateWall(final int width, final int height, final int offset, final int todo) {
        final int x = this.random.nextInt(width);
        final int y = this.random.nextInt(height);
        final boolean horizontal = this.random.nextBoolean();
        final int length = this.random.nextInt(todo) + 1;
        final Collection<Position> res = new ArrayList<Position>();
        if (horizontal) {
            for (int i = 0; i < length; i++) {
                res.add(new Position(((x + i) % width) + offset, y + offset));
            }
        } else {
            for (int i = 0; i < length; i++) {
                res.add(new Position(x + offset, ((y + i) % height) + offset));
            }
        }
        return res;
    }

}
