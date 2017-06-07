package model;

import java.util.*;

/**
 * The collection of walls in the current maze.
 * @author cryingshadow
 */
public class Walls {

    /**
     * The walls.
     */
    private Collection<Position> walls;

    /**
     * @param walls The walls;
     */
    public Walls(final Collection<Position> walls) {
        this.walls = walls;
    }

    /**
     * @return The walls.
     */
    public Collection<Position> getWalls() {
        return this.walls;
    }

    /**
     * @param pos A position.
     * @return True if the specified position is a wall.
     */
    public boolean isWall(final Position pos) {
        return this.walls.contains(pos);
    }

    /**
     * @param walls The walls.
     */
    public void setWalls(final Collection<Position> walls) {
        this.walls = walls;
    }

}
