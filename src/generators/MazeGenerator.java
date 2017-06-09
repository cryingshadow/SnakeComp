package generators;

import java.util.*;
import java.util.stream.*;

import model.*;
import util.*;

/**
 * Generator of random mazes.
 * @author cryingshadow
 */
public class MazeGenerator {

    /**
     * @param walls The walls.
     * @param width The width of the maze.
     * @param height The height of the maze.
     * @param offset The offset for the maze positions due to the arena setting.
     * @return True if all free fields can reach each other.
     */
    private static boolean allFreeFieldsAreConnected(
        final Set<Position> walls,
        final int width,
        final int height,
        final int offset
    ) {
        final Set<Position> freePoss = MazeGenerator.computeAllFreePositions(walls, width, height, offset);
        final UnionFind<Position> union = new UnionFind<Position>(freePoss);
        for (final Position freePos : freePoss) {
            for (
                final Position otherFreePos :
                    MazeGenerator.getSurroundingFreePositions(freePos, walls, width, height, offset)
            ) {
                union.union(freePos, otherFreePos);
            }
        }
        return union.getClasses().size() == 1;
    }

    /**
     * @param walls The walls.
     * @param width The width of the maze.
     * @param height The height of the maze.
     * @param offset The offset for the maze positions due to the arena setting.
     * @return The set of all free positions.
     */
    private static Set<Position> computeAllFreePositions(
        final Set<Position> walls,
        final int width,
        final int height,
        final int offset
    ) {
        final Set<Position> res = new LinkedHashSet<Position>();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                final Position pos = new Position(x + offset, y + offset);
                if (!walls.contains(pos)) {
                    res.add(pos);
                }
            }
        }
        return res;
    }

    /**
     * @param walls The walls.
     * @param width The width of the maze.
     * @param height The height of the maze.
     * @param offset The offset for the maze positions due to the arena setting.
     * @return A set of positions where a wall can be placed.
     */
    private static Set<Position> computeAvailablePositions(
        final Set<Position> walls,
        final int width,
        final int height,
        final int offset
    ) {
        final Set<Position> res = new LinkedHashSet<Position>();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                final Position pos = new Position(x + offset, y + offset);
                if (MazeGenerator.isAvailable(pos, walls, width, height, offset)) {
                    res.add(pos);
                }
            }
        }
        return res;
    }

    /**
     * @param pos A position.
     * @param walls The walls.
     * @param width The width of the maze.
     * @param height The height of the maze.
     * @param offset The offset for the maze positions due to the arena setting.
     * @return The positions of the surrounding free positions of the specified position.
     */
    private static List<Position> getSurroundingFreePositions(
        final Position pos,
        final Set<Position> walls,
        final int width,
        final int height,
        final int offset
    ) {
        final List<Position> res = new ArrayList<Position>(4);
        final int x = pos.getX();
        final int y = pos.getY();
        if (x == 0) {
            if (offset == 0) {
                res.add(new Position(width - 1, y));
            }
        } else {
            res.add(new Position(x - 1, y));
        }
        if (x == width - 1) {
            if (offset == 0) {
                res.add(new Position(0, y));
            }
        } else {
            res.add(new Position(x + 1, y));
        }
        if (y == 0) {
            if (offset == 0) {
                res.add(new Position(x, height - 1));
            }
        } else {
            res.add(new Position(x, y - 1));
        }
        if (y == height - 1) {
            if (offset == 0) {
                res.add(new Position(x, 0));
            }
        } else {
            res.add(new Position(x, y + 1));
        }
        res.removeAll(walls);
        return res;
    }

    /**
     * @param pos A position.
     * @param walls The walls.
     * @param width The width of the maze.
     * @param height The height of the maze.
     * @param offset The offset for the maze positions due to the arena setting.
     * @return True if it is possible to place a wall at the specified position.
     */
    private static boolean isAvailable(
        final Position pos,
        final Set<Position> walls,
        final int width,
        final int height,
        final int offset
    ) {
        return
            !walls.contains(pos)
            && MazeGenerator.surroundingFieldsHaveThreeConnections(pos, walls, width, height, offset)
            && MazeGenerator.allFreeFieldsAreConnected(
                Stream.concat(walls.stream(), Stream.of(pos)).collect(Collectors.toSet()),
                width,
                height,
                offset
            );
    }

    /**
     * @param pos A position.
     * @param walls The walls.
     * @param width The width of the maze.
     * @param height The height of the maze.
     * @param offset The offset for the maze positions due to the arena setting.
     * @return True if all free surrounding fields currently have at least 3 free surrounding fields themselves.
     */
    private static boolean surroundingFieldsHaveThreeConnections(
        final Position pos,
        final Set<Position> walls,
        final int width,
        final int height,
        final int offset
    ) {
        for (
            final Position surrounding : MazeGenerator.getSurroundingFreePositions(pos, walls, width, height, offset)
        ) {
            if (MazeGenerator.getSurroundingFreePositions(surrounding, walls, width, height, offset).size() < 3) {
                return false;
            }
        }
        return true;
    }

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
        Set<Position> available = MazeGenerator.computeAvailablePositions(result, width, height, offset);
        while (todo > 0) {
            if (available.size() < todo) {
                throw new IllegalStateException("Not enough valid fields left to place the walls!");
            }
            final Collection<Position> wall = this.generateWall(width, height, offset, todo);
            for (final Position brick : wall) {
                if (available.contains(brick)) {
                    result.add(brick);
                    available = MazeGenerator.computeAvailablePositions(result, width, height, offset);
                }
            }
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
        final Collection<Position> res = new LinkedList<Position>();
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
