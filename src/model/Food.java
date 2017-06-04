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
     * Generator for food.
     */
    private final FoodGenerator generator;

    /**
     * @param foodGenerator
     */
    public Food(final FoodGenerator foodGenerator) {
        this.food = new LinkedHashSet<Position>();
        this.generator = foodGenerator;
    }

    /**
     * @param maze
     */
    public void generateFood(final Maze maze) {
        this.food.addAll(this.generator.generateFood(maze));
    }

    /**
     * @param pos A position.
     * @return True if food is at the specified position.
     */
    public boolean isFood(final Position pos) {
        return this.food.stream().filter(foodPos -> foodPos.equals(pos)).findFirst().isPresent();
    }

    /**
     * Removes food from the specified position.
     * @param pos The position where to remove food.
     * @return True if food has actually been removed.
     */
    public boolean removeFood(final Position pos) {
        return this.food.remove(pos);
    }

    /**
     * @param amount The amount of food that should be in the maze.
     */
    public void setAmount(final int amount) {
        this.generator.setAmount(amount);
    }

}
