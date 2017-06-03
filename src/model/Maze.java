package model;

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
     * @return The maze
     */
    public Field[][] getMaze() {
        return this.maze;
    }

}
