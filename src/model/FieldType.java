package model;

/**
 * A field type.
 * @author cryingshadow
 */
public enum FieldType {

    /**
     * A collision between a snake and a wall or another snake.
     */
    COLLISION,

    /**
     * A free field with food on it.
     */
    FOOD,

    /**
     * An empty free field that can be moved on by a snake.
     */
    FREE,

    /**
     * A free field with a part of a snake on it.
     */
    SNAKE,

    /**
     * A wall blocks movement. Snakes hitting a wall die.
     */
    WALL

}
