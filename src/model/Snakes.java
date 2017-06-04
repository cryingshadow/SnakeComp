package model;

import java.util.*;
import java.util.stream.*;

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
        final List<Snake> found = this.snakesAt(pos).collect(Collectors.toList());
        if (found.size() == 1) {
            return Optional.of(found.get(0));
        }
        return Optional.empty();
    }

    /**
     * @param pos A position.
     * @return True if at least one snake is at the specified position.
     */
    public boolean isSnake(final Position pos) {
        return this.snakesAt(pos).findFirst().isPresent();
    }

    /**
     * @param pos A position.
     * @return A stream of snakes at the specified position.
     */
    private Stream<Snake> snakesAt(final Position pos) {
        return this.snakes.stream().filter(snake -> {return snake.getHead().equals(pos);});
    }

}
