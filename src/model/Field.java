package model;

import java.awt.*;
import java.util.*;

/**
 * A field has a type and possibly a snake color.
 * @author cryingshadow
 */
public class Field {

    /**
     * The snake color on this field.
     */
    private final Optional<Color> snakeColor;

    /**
     * The type of this field.
     */
    private final FieldType type;

    /**
     * @param type The type.
     * @param snakeColor The snake color on this field.
     */
    public Field(final FieldType type, final Optional<Color> snakeColor) {
        this.type = type;
        this.snakeColor = snakeColor;
    }

    /**
     * @return The snake color on this field.
     */
    public Optional<Color> getSnakeColor() {
        return this.snakeColor;
    }

    /**
     * @return The type.
     */
    public FieldType getType() {
        return this.type;
    }

}
