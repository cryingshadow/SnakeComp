package model;

import java.util.*;
import java.util.stream.*;

/**
 * Collection of the snakes in the competition.
 * @author cryingshadow
 */
public class Snakes extends ChangeListenable {

    /**
     * The snakes.
     */
    private List<Snake> snakes;

    /**
     * @param snakes The snakes;
     */
    public Snakes(final List<Snake> snakes) {
        this.snakes = snakes;
    }

    /**
     * Removes all snakes.
     */
    public void clear() {
        this.snakes = Collections.emptyList();
        this.notifyChangeListeners();
    }

    /**
     * @return The snakes that are alive.
     */
    public List<Snake> getAliveSnakes() {
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
    public List<Snake> getAllSnakes() {
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
     * @param snakes The snakes.
     */
    public void setSnakes(final List<Snake> snakes) {
        this.snakes = snakes;
        this.notifyChangeListeners();
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
