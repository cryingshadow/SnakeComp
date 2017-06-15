package model;

/**
 * A field type.
 * @author cryingshadow
 */
public enum FieldType {

    /**
     * A collision between at least two snakes on a field with food.
     */
    COLLISION_ON_FOOD(true),

    /**
     * A collision between at least two snakes on an otherwise free field.
     */
    COLLISION_ON_FREE(true),

    /**
     * A collision between a snake and a wall.
     */
    COLLISION_ON_WALL(true),

    /**
     * A free field with food on it.
     */
    FOOD(false),

    /**
     * An empty free field that can be moved on by a snake.
     */
    FREE(false),

    /**
     * A free field with a part of a snake body on it.
     */
    SNAKE_BODY(true),

    /**
     * A free field with a snake head on it.
     */
    SNAKE_HEAD(true),

    /**
     * A free field with an eating snake head on it.
     */
    SNAKE_HEAD_EATING(true),

    /**
     * A wall blocks movement. Snakes hitting a wall die.
     */
    WALL(true);

    /**
     * Is this field type an obstacle (i.e., having moved there would have led to a collision)?
     */
    private final boolean obstacle;

    /**
     * @param obstacle Is this field type an obstacle (i.e., having moved there would have led to a collision)?
     */
    private FieldType(final boolean obstacle) {
        this.obstacle = obstacle;
    }

    /**
     * @return Is this field type an obstacle (i.e., having moved there would have led to a collision)?
     */
    public boolean isObstacle() {
        return this.obstacle;
    }

}
