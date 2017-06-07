package model;

/**
 * Maze field sizes.
 * @author cryingshadow
 */
public enum Zoom {

    /**
     * Big size.
     */
    BIG(100),

    /**
     * Huge size.
     */
    HUGE(200),

    /**
     * Normal size.
     */
    NORMAL(50),

    /**
     * Small size
     */
    SMALL(25),

    /**
     * Tiny size.
     */
    TINY(10);

    /**
     * The field size.
     */
    private final int fieldSize;

    /**
     * @param fieldSize The field size.
     */
    private Zoom(final int fieldSize) {
        this.fieldSize = fieldSize;
    }

    /**
     * @return The field size.
     */
    public int getFieldSize() {
        return this.fieldSize;
    }

}
