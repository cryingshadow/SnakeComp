package model;

import java.awt.*;
import java.util.*;
import java.util.List;

import control.*;
import util.*;

/**
 * A snake has a head that moves one field each turn and a body of a certain length that follows the head.
 * @author cryingshadow
 */
public class Snake {

    /**
     * Is the snake still alive?
     */
    private final boolean alive;

    /**
     * The snake's color.
     */
    private final Color color;

    /**
     * The control for this snake.
     */
    private final SnakeControl control;

    /**
     * The snake's current hunger.
     */
    private final int hunger;

    /**
     * How often did the snake control take too long to compute the next direction?
     */
    private final int longThinker;

    /**
     * The maximum hunger the snake can survive.
     */
    private final Optional<Integer> maxHunger;

    /**
     * The positions of the snake. The last one is the position of the snake's head.
     */
    private final LinkedList<Position> snake;

    /**
     * @param initialPosition The initial position of the snake's head.
     * @param initialLength The initial length of the snake.
     * @param maxHunger The maximum hunger the snake can survive.
     * @param color The snake's color.
     * @param control The control for this snake.
     */
    public Snake(
        final Position initialPosition,
        final int initialLength,
        final Optional<Integer> maxHunger,
        final Color color,
        final SnakeControl control
    ) {
        if (initialLength < 1) {
            throw new IllegalArgumentException("Length must be positive!");
        }
        if (maxHunger.isPresent() && maxHunger.get() < 1) {
            throw new IllegalArgumentException("Maximum hunger must be positive or null!");
        }
        this.control = control;
        this.color = color;
        this.alive = true;
        this.hunger = 0;
        this.maxHunger = maxHunger;
        this.longThinker = 0;
        this.snake = new LinkedList<Position>();
        for (int i = 1; i < initialLength; i++) {
            this.snake.offer(null);
        }
        this.snake.offer(initialPosition);
    }

    /**
     * @param alive Is the snake still alive?
     * @param color The snake's color.
     * @param control The control for this snake.
     * @param hunger The snake's current hunger.
     * @param maxHunger The maximum hunger the snake can survive.
     * @param longThinker Did the snake control take too long last time to compute the next direction?
     * @param snake The positions of the snake. The last one is the position of the snake's head.
     */
    private Snake(
        final boolean alive,
        final Color color,
        final SnakeControl control,
        final int hunger,
        final Optional<Integer> maxHunger,
        final int longThinker,
        final LinkedList<Position> snake
    ) {
        this.alive = alive;
        this.color = color;
        this.control = control;
        this.hunger = hunger;
        this.maxHunger = maxHunger;
        this.longThinker = longThinker;
        this.snake = snake;
    }

    /**
     * @return This snake being alive but without positions or hunger.
     */
    public Snake clear() {
        return new Snake(true, this.color, this.control, 0, this.maxHunger, 0, new LinkedList<Position>());
    }

    /**
     * @return The snake's color.
     */
    public Color getColor() {
        return this.color;
    }

    /**
     * @return The position of the snake's head.
     */
    public Position getHead() {
        return this.snake.getLast();
    }

    /**
     * @return The length of this snake.
     */
    public int getLength() {
        return this.snake.size();
    }

    /**
     * @return The snake's name.
     */
    public String getName() {
        return this.control.getName();
    }

    /**
     * @param maze The maze.
     * @return The direction in which to move next and a flag indicating whether the computation took too long.
     */
    public Pair<Direction, Boolean> getNextDirection(final Maze maze) {
        final Position curPos = this.getHead();
        final List<Direction> container = new Vector<Direction>(1);
        final Object monitor = this;
        final Thread t =
            new Thread(
                new Runnable() {

                    @Override
                    public void run() {
                        container.add(Snake.this.control.nextDirection(maze, curPos.getX(), curPos.getY()));
                        synchronized (monitor) {
                            monitor.notify();
                        }
                    }

                }
            );
        t.start();
        try {
            synchronized (monitor) {
                monitor.wait(200);
            }
        } catch (final InterruptedException e) {
            // just continue
        } finally {
            if (t.isAlive()) {
                t.interrupt();
                final Thread killer =
                    new Thread(
                        new Runnable() {

                            @SuppressWarnings("deprecation")
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(2000);
                                    t.stop();
                                } catch (final InterruptedException e) {
                                    // do nothing
                                }
                            }

                        }
                    );
                killer.start();
            }
        }
        final List<Direction> snapshot = new ArrayList<Direction>(container);
        if (snapshot.size() > 0) {
            return new Pair<Direction, Boolean>(snapshot.get(0), false);
        }
        return new Pair<Direction, Boolean>(Direction.UP, true);
    }

    /**
     * @param direction The direction in which the snake moves.
     * @return The next position the snake would reach. This position might have a coordinate that is out of bounds and
     *         has to be post-processed by an over- or underflow.
     */
    public Position getNextPosition(final Direction direction) {
        final Position currentHead = this.getHead();
        switch (direction) {
            case DOWN:
                return new Position(currentHead.getX(), currentHead.getY() + 1);
            case LEFT:
                return new Position(currentHead.getX() - 1, currentHead.getY());
            case RIGHT:
                return new Position(currentHead.getX() + 1, currentHead.getY());
            case UP:
                return new Position(currentHead.getX(), currentHead.getY() - 1);
            default:
                throw new IllegalStateException("Someone found a new direction...");
        }
    }

    /**
     * @return All positions occupied by this snake.
     */
    public Collection<Position> getPositions() {
        return this.snake;
    }

    /**
     * Move this snake without collision to the specified position and let it grow by one. Also remove any hunger from
     * this snake.
     * @param nextPos The next position of the snake's head and a flag indicating whether the snake control took too
     *                long to compute the next direction.
     * @return The moved and grown snake.
     */
    public Snake growingMove(final Pair<Position, Boolean> nextPos) {
        final LinkedList<Position> newSnake = new LinkedList<Position>(this.snake);
        newSnake.offer(nextPos.getKey());
        return new Snake(
            this.alive,
            this.color,
            this.control,
            0,
            this.maxHunger,
            nextPos.getValue() ? this.longThinker + 1 : this.longThinker,
            newSnake
        );
    }

    /**
     * @return Is the snake still alive?
     */
    public boolean isAlive() {
        return this.alive;
    }

    /**
     * @return True if this snake's hunger exceeds the maximum hunger.
     */
    public boolean isStarved() {
        return this.maxHunger.isPresent() && this.maxHunger.get() < this.hunger;
    }

    /**
     * @return True if this snake took too long to compute the next direction more than twice.
     */
    public boolean isTooSlow() {
        return this.longThinker > 2;
    }

    /**
     * @return This snake being dead.
     */
    public Snake kill() {
        return new Snake(false, this.color, this.control, this.hunger, this.maxHunger, this.longThinker, this.snake);
    }

    /**
     * Move this snake without collision or growth to the specified position. Also increase this snake's hunger by one.
     * @param nextPos The next position of the snake's head and a flag indicating whether the snake control took too
     *                long to compute the next direction.
     * @return The moved snake.
     */
    public Snake normalMove(final Pair<Position, Boolean> nextPos) {
        final LinkedList<Position> newSnake = new LinkedList<Position>(this.snake);
        newSnake.poll();
        newSnake.offer(nextPos.getKey());
        return new Snake(
            this.alive,
            this.color,
            this.control,
            this.hunger + 1,
            this.maxHunger,
            nextPos.getValue() ? this.longThinker + 1 : this.longThinker,
            newSnake
        );
    }

    /**
     * @param pos A position.
     * @return True if this snake collides with itself at the specified collision.
     */
    public boolean selfCollisionAt(final Position pos) {
        return this.getPositions().stream().filter(p -> pos.equals(p)).count() > 1;
    }

}
