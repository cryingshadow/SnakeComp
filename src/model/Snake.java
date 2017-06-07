package model;

import java.awt.*;
import java.util.*;

import control.*;

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
     * @param snake The positions of the snake. The last one is the position of the snake's head.
     */
    private Snake(
        final boolean alive,
        final Color color,
        final SnakeControl control,
        final int hunger,
        final Optional<Integer> maxHunger,
        final LinkedList<Position> snake
    ) {
        this.alive = alive;
        this.color = color;
        this.control = control;
        this.hunger = hunger;
        this.maxHunger = maxHunger;
        this.snake = snake;
    }

    /**
     * @return This snake being alive but without positions or hunger.
     */
    public Snake clear() {
        return new Snake(true, this.color, this.control, 0, this.maxHunger, new LinkedList<Position>());
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
     * @return The direction in which to move next.
     */
    public Direction getNextDirection(final Maze maze) {
        final Position curPos = this.getHead();
        return this.control.nextDirection(maze, curPos.getX(), curPos.getY());
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
     * @param nextPos The next position of the snake's head.
     * @return The moved and grown snake.
     */
    public Snake growingMove(final Position nextPos) {
        final LinkedList<Position> newSnake = new LinkedList<Position>(this.snake);
        newSnake.offer(nextPos);
        return new Snake(this.alive, this.color, this.control, 0, this.maxHunger, newSnake);
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
     * @return This snake being dead.
     */
    public Snake kill() {
        return new Snake(false, this.color, this.control, this.hunger, this.maxHunger, this.snake);
    }

    /**
     * Move this snake without collision or growth to the specified position. Also increase this snake's hunger by one.
     * @param nextPos The next position of the snake's head.
     * @return The moved snake.
     */
    public Snake normalMove(final Position nextPos) {
        final LinkedList<Position> newSnake = new LinkedList<Position>(this.snake);
        newSnake.poll();
        newSnake.offer(nextPos);
        return new Snake(this.alive, this.color, this.control, this.hunger + 1, this.maxHunger, newSnake);
    }

    /**
     * @param pos A position.
     * @return True if this snake collides with itself at the specified collision.
     */
    public boolean selfCollisionAt(final Position pos) {
        return this.getPositions().stream().filter(p -> pos.equals(p)).count() > 1;
    }

}
