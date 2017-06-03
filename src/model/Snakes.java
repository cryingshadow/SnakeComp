package model;

import java.util.*;

/**
 * Collection of the snakes in the competition.
 * @author cryingshadow
 */
public class Snakes {

    /**
     * The snakes.
     */
    private final Collection<Snake> snakes;

    /**
     * @param snakes The snakes;
     */
    public Snakes(final Collection<Snake> snakes) {
        this.snakes = snakes;
    }

    /**
     * @return The snakes.
     */
    public Collection<Snake> getSnakes() {
        return this.snakes;
    }

    /**
     * @param pos A position.
     * @return The snake at the specified position if there is exactly one such snake. Empty optional otherwise.
     */
    public Optional<Snake> getUniqueSnakeAt(final Position pos) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @param pos A position.
     * @return True if at least one snake is at the specified position.
     */
    public boolean isSnake(final Position pos) {
        // TODO Auto-generated method stub
        return false;
    }

}
