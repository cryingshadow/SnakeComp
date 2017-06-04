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
     * @return The snakes that are alive.
     */
    public Collection<Snake> getAliveSnakes() {
        return this.getAliveSnakesAsStream().collect(Collectors.toList());
    }

    /**
     * @param pos A position.
     * @return All alive snakes at the specified position.
     */
    public List<Snake> getAllAliveSnakesAt(final Position pos) {
        return this.snakesAt(pos).collect(Collectors.toList());
    }

    /**
     * @return The snakes.
     */
    public Collection<Snake> getAllSnakes() {
        return this.snakes;
    }

    /**
     * @param pos A position.
     * @return The snake at the specified position if there is exactly one such snake being alive. Empty optional
     *         otherwise.
     */
    public Optional<Snake> getUniqueSnakeAt(final Position pos) {
        final List<Snake> found = this.getAllAliveSnakesAt(pos);
        if (found.size() == 1) {
            return Optional.of(found.get(0));
        }
        return Optional.empty();
    }

    /**
     * @param pos A position.
     * @return True if at least one snake is alive at the specified position.
     */
    public boolean isSnake(final Position pos) {
        return this.snakesAt(pos).findFirst().isPresent();
    }

    /**
     * @param newSnakes The snakes.
     */
    public void setSnakes(final Collection<Snake> newSnakes) {
        this.snakes.clear();
        this.snakes.addAll(newSnakes);
    }

    /**
     * @return The snakes that are alive as stream.
     */
    private Stream<Snake> getAliveSnakesAsStream() {
        return this.snakes.stream().filter(Snake::isAlive);
    }

    /**
     * @param pos A position.
     * @return A stream of alive snakes at the specified position.
     */
    private Stream<Snake> snakesAt(final Position pos) {
        return this.getAliveSnakesAsStream().filter(snake -> snake.getPositions().contains(pos));
    }

}
