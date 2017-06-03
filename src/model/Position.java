package model;

/**
 * A simple 2D position.
 * @author cryingshadow
 */
public class Position {

    /**
     * The position on the x-axis.
     */
    private final int x;

    /**
     * The position on the y-axis.
     */
    private final int y;

    /**
     * @param x The position on the x-axis.
     * @param y The position on the y-axis.
     */
    public Position(final int x, final int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * @return The position on the x-axis.
     */
    public int getX() {
        return this.x;
    }

    /**
     * @return The position on the y-axis.
     */
    public int getY() {
        return this.y;
    }

}
