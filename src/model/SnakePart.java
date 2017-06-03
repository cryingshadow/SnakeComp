package model;

import java.awt.*;

/**
 * A part of a snake has a color and is either the head or the body.
 * @author cryingshadow
 */
public class SnakePart {

    /**
     * The snake's color.
     */
    private final Color color;

    /**
     * Is this part the head?
     */
    private final boolean isHead;

    /**
     * @param color The snake's color.
     * @param isHead Is this part the head?
     */
    public SnakePart(final Color color, final boolean isHead) {
        this.color = color;
        this.isHead = isHead;
    }

    /**
     * @return The color.
     */
    public Color getColor() {
        return this.color;
    }

    /**
     * @return Is this part the head?
     */
    public boolean isHead() {
        return this.isHead;
    }

}
