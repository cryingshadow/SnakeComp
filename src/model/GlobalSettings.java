package model;

import java.util.*;

/**
 * Global settings for a snake competition.
 * @author cryingshadow
 */
public class GlobalSettings {

    /**
     * Is the maze to be an arena (i.e., having walls at the border of the maze)?
     */
    private boolean arena;

    /**
     * The size of a single field.
     */
    private int fieldSize;

    /**
     * How many pieces of food will be in the maze per participating snake?
     */
    private int foodPerSnake;

    /**
     * The height of the maze.
     */
    private int height;

    /**
     * The initial length of a snake.
     */
    private int initialSnakeLength;

    /**
     * The maximum hunger a snake can survive.
     */
    private Optional<Integer> maxHunger;

    /**
     * The number of walls in the maze (without the arena walls if set).
     */
    private int walls;


    /**
     * The width of the maze.
     */
    private int width;

    /**
     * Default settings.
     */
    public GlobalSettings() {
        this.fieldSize = 50;
        this.foodPerSnake = 1;
        this.arena = false;
        this.initialSnakeLength = 3;
        this.maxHunger = Optional.of(1000);
        this.height = 20;
        this.width = 20;
        this.walls = 50;
    }

    /**
     * @return The size of a single field.
     */
    public int getFieldSize() {
        return this.fieldSize;
    }

    /**
     * @return How many pieces of food will be in the maze per participating snake?
     */
    public int getFoodPerSnake() {
        return this.foodPerSnake;
    }

    /**
     * @return The height of the maze.
     */
    public int getHeight() {
        return this.height;
    }

    /**
     * @return The initial length of a snake.
     */
    public int getInitialSnakeLength() {
        return this.initialSnakeLength;
    }

    /**
     * @return The maximum hunger a snake can survive.
     */
    public Optional<Integer> getMaxHunger() {
        return this.maxHunger;
    }

    /**
     * @return The number of walls in the maze (without the arena walls if set).
     */
    public int getWalls() {
        return this.walls;
    }

    /**
     * @return The width of the maze.
     */
    public int getWidth() {
        return this.width;
    }

    /**
     * @return Is the maze to be an arena (i.e., having walls at the border of the maze)?
     */
    public boolean isArena() {
        return this.arena;
    }

    /**
     * @param arena Is the maze to be an arena (i.e., having walls at the border of the maze)?
     */
    public void setArena(final boolean arena) {
        this.arena = arena;
    }

    /**
     * @param fieldSize The size of a single field.
     */
    public void setFieldSize(final int fieldSize) {
        this.fieldSize = fieldSize;
    }

    /**
     * @param foodPerSnake How many pieces of food will be in the maze per participating snake?
     */
    public void setFoodPerSnake(final int foodPerSnake) {
        this.foodPerSnake = foodPerSnake;
    }

    /**
     * @param height The height of the maze.
     */
    public void setHeight(final int height) {
        this.height = height;
    }

    /**
     * @param initialSnakeLength The initial length of a snake.
     */
    public void setInitialSnakeLength(final int initialSnakeLength) {
        this.initialSnakeLength = initialSnakeLength;
    }

    /**
     * @param maxHunger The maximum hunger a snake can survive.
     */
    public void setMaxHunger(final Optional<Integer> maxHunger) {
        this.maxHunger = maxHunger;
    }

    /**
     * @param walls The number of walls in the maze (without the arena walls if set).
     */
    public void setWalls(final int walls) {
        this.walls = walls;
    }

    /**
     * @param width The width of the maze.
     */
    public void setWidth(final int width) {
        this.width = width;
    }

}

