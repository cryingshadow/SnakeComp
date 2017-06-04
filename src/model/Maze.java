package model;

import java.util.*;

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
        final List<Position> res = new LinkedList<Position>();
        for (int y = 0; y < this.maze.length; y++) {
            final Field[] mazeRow = this.maze[y];
            for (int x = 0; x < mazeRow.length; x++) {
                switch (mazeRow[x].getType()) {
                    case FREE:
                        res.add(new Position(x, y));
                        break;
                    default:
                        // do nothing
                }
            }
        }
        return res;
    }

    /**
     * @return The maze
     */
    public Field[][] getMaze() {
        return this.maze;
    }

}
