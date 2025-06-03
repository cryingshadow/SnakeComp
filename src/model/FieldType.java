package model;

public enum FieldType {

    COLLISION_ON_FOOD(true),

    COLLISION_ON_FREE(true),

    COLLISION_ON_WALL(true),

    FOOD(false),

    FREE(false),

    SNAKE_BODY(true),

    SNAKE_HEAD(true),

    SNAKE_HEAD_EATING(true),

    WALL(true);

    public final boolean isObstacle;

    private FieldType(final boolean isObstacle) {
        this.isObstacle = isObstacle;
    }

}
