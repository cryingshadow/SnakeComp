package model;

import java.awt.*;
import java.util.*;
import java.util.stream.*;

/**
 * A snake has a head that moves one field each turn and a body of a certain length that follows the head.
 * @author cryingshadow
 */
public class Snake {

    /**
     * Is the snake still alive?
     */
    private boolean alive;

    /**
     * The snake's color.
     */
    private final Color color;

    /**
     * The snake's current hunger.
     */
    private int hunger;

    /**
     * The maximum hunger the snake can survive.
     */
    private final Optional<Integer> maxHunger;

    /**
     * The snake's name.
     */
    private final String name;

    /**
     * The positions of the snake. The last one is the position of the snake's head.
     */
    private final LinkedList<Position> snake;

    /**
     * @param initialPosition The initial position of the snake's head.
     * @param initialLength The initial length of the snake.
     * @param maxHunger The maximum hunger the snake can survive.
     * @param name The snake's name.
     * @param color The snake's color.
     */
    public Snake(
        final Position initialPosition,
        final int initialLength,
        final Optional<Integer> maxHunger,
        final String name,
        final Color color
    ) {
        if (initialLength < 1) {
            throw new IllegalArgumentException("Length must be positive!");
        }
        if (maxHunger.isPresent() && maxHunger.get() < 1) {
            throw new IllegalArgumentException("Maximum hunger must be positive or null!");
        }
        this.name = name;
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
     * Move this snake with a collision and without growth to the specified position. Also kill this snake.
     * @param nextPos The next position of the snake's head.
     */
    public void collisionMove(final Position nextPos) {
        this.move(nextPos);
        this.alive = false;
    }


    /**
     * @return All positions of the snake's body.
     */
    public Collection<Position> getBody() {
        return this.snake.stream().skip(1).filter(pos -> {return pos != null;}).collect(Collectors.toList());
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
     * @return The snake's name.
     */
    public String getName() {
        return this.name;
    }

    /**
     * @param direction The direction in which the snake moves.
     * @return The next position the snake would reach.
     */
    public Position getNextPosition(final Direction direction) {
        final Position currentHead = this.getHead();
        switch (direction) {
            case DOWN:
                return new Position(currentHead.getX(), currentHead.getY() - 1);
            case LEFT:
                return new Position(currentHead.getX() - 1, currentHead.getY());
            case RIGHT:
                return new Position(currentHead.getX() + 1, currentHead.getY());
            case UP:
                return new Position(currentHead.getX(), currentHead.getY() + 1);
            default:
                throw new IllegalStateException("Someone found a new direction...");
        }
    }

    /**
     * Move this snake without collision to the specified position and let it grow by one. Also remove any hunger from
     * this snake.
     * @param nextPos The next position of the snake's head.
     */
    public void growingMove(final Position nextPos) {
        this.snake.offer(nextPos);
        this.hunger = 0;
    }

    /**
     * @return Is the snake still alive?
     */
    public boolean isAlive() {
        return this.alive;
    }

    /**
     * Move this snake without collision or growth to the specified position. Also increase this snake's hunger by one.
     * @param nextPos The next position of the snake's head.
     */
    public void normalMove(final Position nextPos) {
        this.move(nextPos);
        this.hunger++;
        if (this.maxHunger.isPresent() && this.hunger > this.maxHunger.get()) {
            this.alive = false;
        }
    }

    /**
     * Move this snake without growth to the specified position.
     * @param nextPos The next position of the snake's head.
     */
    private void move(final Position nextPos) {
        this.snake.poll();
        this.snake.offer(nextPos);
    }

}
