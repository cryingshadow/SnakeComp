package model;

/**
 * A field type.
 * @author cryingshadow
 */
public enum FieldType {

    /**
     * A collision between at least two snakes on a field with food.
     */
    COLLISION_ON_FOOD,

    /**
     * A collision between at least two snakes on an otherwise free field.
     */
    COLLISION_ON_FREE,

    /**
     * A collision between a snake and a wall.
     */
    COLLISION_ON_WALL,

    /**
     * A free field with food on it.
     */
    FOOD,

    /**
     * An empty free field that can be moved on by a snake.
     */
    FREE,

    /**
     * A free field with a part of a snake body on it.
     */
    SNAKE_BODY,

    /**
     * A free field with a snake head on it.
     */
    SNAKE_HEAD,

    /**
     * A free field with an eating snake head on it.
     */
    SNAKE_HEAD_EATING,

    /**
     * A wall blocks movement. Snakes hitting a wall die.
     */
    WALL

}
