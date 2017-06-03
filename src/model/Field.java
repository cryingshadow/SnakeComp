package model;

import java.util.*;

/**
 * A field has a type and possibly a snake part.
 * @author cryingshadow
 */
public class Field {

    /**
     * The snake part on this field.
     */
    private final Optional<SnakePart> snakePart;

    /**
     * The type of this field.
     */
    private final FieldType type;

    /**
     * @param type The type.
     * @param snakePart The snake part on this field.
     */
    public Field(final FieldType type, final Optional<SnakePart> snakePart) {
        this.type = type;
        this.snakePart = snakePart;
    }

    /**
     * @return The snake part on this field.
     */
    public Optional<SnakePart> getSnakePart() {
        return this.snakePart;
    }

    /**
     * @return The type.
     */
    public FieldType getType() {
        return this.type;
    }

}
