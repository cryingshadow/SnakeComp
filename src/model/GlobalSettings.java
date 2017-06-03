package model;

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
     * Default settings.
     */
    public GlobalSettings() {
        this.fieldSize = 50;
        this.foodPerSnake = 1;
        this.arena = false;
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

}

