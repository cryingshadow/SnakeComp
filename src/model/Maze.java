package model;

import java.util.*;
import java.util.function.*;

/**
 * A maze is a rectangular collection of fields.
 * @author cryingshadow
 */
public class Maze {

    /**
     * The maze.
     */
    private final Field[][] maze;

    /**
     * @param maze The maze;
     */
    public Maze(final Field[][] maze) {
        this.maze = maze;
    }

    /**
     * @return The free positions in this maze.
     */
    public List<Position> getFreePositions() {
        return this.getPositions(field -> field.getType().equals(FieldType.FREE));
    }

    /**
     * @return The maze
     */
    public Field[][] getMaze() {
        return this.maze;
    }

    /**
     * @return The number of fields with food in this maze.
     */
    public int numOfFood() {
        return this.getPositions(field -> field.getType().equals(FieldType.FOOD)).size();
    }

    /**
     * @param predicate Some predicate on fields.
     * @return The positions of the fields satisfying the predicate.
     */
    private List<Position> getPositions(final Predicate<Field> predicate) {
        final List<Position> res = new LinkedList<Position>();
        for (int y = 0; y < this.maze.length; y++) {
            final Field[] mazeRow = this.maze[y];
            for (int x = 0; x < mazeRow.length; x++) {
                if (predicate.test(mazeRow[x])) {
                    res.add(new Position(x, y));
                }
            }
        }
        return res;
    }

}
