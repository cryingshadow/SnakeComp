package model;

import java.awt.*;
import java.util.*;

public record Field(FieldType type, Optional<Color> snakeColor) {

    public Field(final FieldType type) {
        this(type, Optional.empty());
    }


}
