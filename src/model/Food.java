package model;

import java.util.*;

import generators.*;

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
     * @param foodGenerator
     */
    public Food(final FoodGenerator foodGenerator) {
        this.food = new LinkedHashSet<Position>();
    }

    /**
     * @param maze
     */
    public void generateFood(final Maze maze) {
        // TODO Auto-generated method stub

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
        return this.food.stream().filter(foodPos -> foodPos.equals(pos)).findFirst().isPresent();
    }

}
