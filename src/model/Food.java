package model;

import java.util.*;

/**
 * The collection of food in the current maze.
 * @author cryingshadow
 */
public class Food {

    /**
     * The food.
     */
    private final Collection<Position> food;

    /**
     * @param food The food;
     */
    public Food(final Collection<Position> food) {
        this.food = food;
    }

    /**
     * @return The food.
     */
    public Collection<Position> getFood() {
        return this.food;
    }

    /**
     * @param pos A position.
     * @return True if food is at the specified position.
     */
    public boolean isFood(final Position pos) {
        // TODO Auto-generated method stub
        return false;
    }

}
