package view;

import java.awt.*;
import java.util.*;

import javax.swing.*;

import model.*;

/**
 * Display for a maze.
 * @author cryingshadow
 */
public class MazeDisplay extends JPanel {

    /**
     * For serialization.
     */
    private static final long serialVersionUID = 3055355083944967024L;

    /**
     * Paints the background of the field.
     * @param g The graphics.
     * @param x The offset on the x-axis.
     * @param y The offset on the y-axis.
     * @param type The field type.
     * @param size The field size.
     */
    private static void paintFieldBackground(
        final Graphics g,
        final int x,
        final int y,
        final FieldType type,
        final int size
    ) {
        switch (type) {
            case WALL:
                g.setColor(Color.GRAY);
                break;
            default:
                g.setColor(Color.BLACK);
        }
        g.fillRect(x, y, size, size);
    }

    /**
     * Paints the content of the field on top of its background (i.e., paintBackground must have been called before).
     * @param g The graphics.
     * @param x The offset on the x-axis.
     * @param y The offset on the y-axis.
     * @param type The field type.
     * @param snakePart The snake part.
     * @param size The field size.
     */
    private static void paintFieldContent(
        final Graphics g,
        final int x,
        final int y,
        final FieldType type,
        final Optional<SnakePart> snakePart,
        final int size
    ) {
        final Color currentColor = g.getColor();
        switch (type) {
            case FOOD:
                g.setColor(Color.WHITE);
                g.fillOval(x, y, size, size);
                break;
            case SNAKE:
                if (!snakePart.isPresent()) {
                    throw new IllegalArgumentException("Type SNAKE is incompatible with an empty snake part!");
                }
                final SnakePart actualSnakePart = snakePart.get();
                g.setColor(actualSnakePart.getColor());
                g.fillOval(x, y, size, size);
                if (actualSnakePart.isHead()) {
                    final int lineWidth = 5;
                    final int reducedSize = size - 2 * lineWidth;
                    g.setColor(currentColor);
                    g.fillOval(x + lineWidth, y + lineWidth, reducedSize, reducedSize);
                }
                break;
            default:
                // do nothing
        }
    }

    /**
     * The field size.
     */
    private int fieldSize;

    /**
     * The maze.
     */
    private final Maze maze;

    /**
     * @param maze The maze.
     * @param fieldSize The field size.
     */
    public MazeDisplay(final Maze maze, final int fieldSize) {
        final Field[][] array = maze.getMaze();
        if (array.length < 1 || array[0].length < 1) {
            throw new IllegalArgumentException("Maze must have at least one row and column!");
        }
        this.maze = maze;
        this.fieldSize = fieldSize;
    }

    /**
     * @return the fieldSize
     */
    public int getFieldSize() {
        return this.fieldSize;
    }

    @Override
    public Dimension getPreferredSize() {
        final int size = this.getFieldSize();
        final Field[][] array = this.maze.getMaze();
        return new Dimension(size * array[0].length, size * array.length);
    }

    /**
     * @param fieldSize the fieldSize to set
     */
    public void setFieldSize(final int fieldSize) {
        this.fieldSize = fieldSize;
    }

    @Override
    protected void paintComponent(final Graphics g) {
        final int size = this.getFieldSize();
        final Field[][] fieldArray = this.maze.getMaze();
        for (int i = 0; i < fieldArray.length; i++) {
            final Field[] fieldRow = fieldArray[i];
            final int yOffset = i * size;
            for (int j = 0; j < fieldRow.length; j++) {
                final Field field = fieldRow[j];
                final FieldType type = field.getType();
                final int xOffset = j * size;
                MazeDisplay.paintFieldBackground(g, xOffset, yOffset, type, size);
                MazeDisplay.paintFieldContent(g, xOffset, yOffset, type, field.getSnakePart(), size);
            }
        }
    }

}
