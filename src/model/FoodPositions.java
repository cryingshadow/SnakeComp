package model;

import java.util.*;

import generators.*;

public class FoodPositions {

    private final Collection<Position> food;

    private final FoodGenerator generator;

    public FoodPositions(final FoodGenerator foodGenerator) {
        this.food = new LinkedHashSet<Position>();
        this.generator = foodGenerator;
    }

    public void clear() {
        this.food.clear();
    }

    public void generateFood(final Maze maze) {
        this.food.addAll(this.generator.generateFood(maze));
    }

    public boolean isFood(final Position pos) {
        return this.food.stream().filter(foodPos -> foodPos.equals(pos)).findFirst().isPresent();
    }

    public boolean removeFood(final Collection<Position> poss) {
        return this.food.removeAll(poss);
    }

    public void setMinAmount(final int amount) {
        this.generator.setAmount(amount);
    }

}
