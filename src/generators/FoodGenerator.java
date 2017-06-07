package generators;

import java.util.*;

import model.*;

/**
 * Generator for food in a maze.
 * @author cryingshadow
 */
public class FoodGenerator {

    /**
     * The amount of food that should be in the maze.
     */
    private int amount;

    /**
     * Random number generator.
     */
    private final Random random = new Random();

    /**
     * @param amount The amount of food that should be in the maze.
     */
    public FoodGenerator() {
        this.amount = 0;
    }

    /**
     * @param maze The maze.
     * @return The positions where to add food such that the amount of this generator is reached.
     */
    public Collection<Position> generateFood(final Maze maze) {
        int numOfFood = maze.numOfFood();
        final List<Position> freePositions = maze.getFreePositions();
        if (this.amount - numOfFood > freePositions.size()) {
            throw new IllegalArgumentException("Not enough free positions left for food!");
        }
        final Collection<Position> res = new LinkedList<Position>();
        while (numOfFood < this.amount) {
            res.add(freePositions.remove(this.random.nextInt(freePositions.size())));
            numOfFood++;
        }
        return res;
    }

    /**
     * @return The amount of food that should be in the maze.
     */
    public int getAmount() {
        return this.amount;
    }

    /**
     * @param amount The amount of food that should be in the maze.
     */
    public void setAmount(final int amount) {
        this.amount = amount;
    }
}
