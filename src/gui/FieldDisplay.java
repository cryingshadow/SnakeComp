package gui;

import java.awt.*;
import java.util.*;

import javax.swing.*;

import model.*;

/**
 * Display of a single field in a snake game.
 * @author cryingshadow
 */
public class FieldDisplay extends JPanel {

    /**
     * For serialization.
     */
    private static final long serialVersionUID = -6276472917021577539L;

    /**
     * Paints the background of the field.
     * @param g The graphics.
     * @param type The field type.
     * @param size The field size.
     */
    private static void paintBackground(final Graphics g, final FieldType type, final int size) {
        switch (type) {
            case WALL:
                g.setColor(Color.GRAY);
                break;
            default:
                g.setColor(Color.BLACK);
        }
        g.fillRect(0, 0, size, size);
    }

    /**
     * Paints the content of the field on top of its background (i.e., paintBackground must have been called before).
     * @param g The graphics.
     * @param type The field type.
     * @param snakePart The snake part.
     * @param size The field size.
     */
    private static void paintContent(
        final Graphics g,
        final FieldType type,
        final Optional<SnakePart> snakePart,
        final int size
    ) {
        final Color currentColor = g.getColor();
        switch (type) {
            case FOOD:
                g.setColor(Color.WHITE);
                g.fillOval(0, 0, size, size);
                break;
            case SNAKE:
                if (!snakePart.isPresent()) {
                    throw new IllegalArgumentException("Type SNAKE is incompatible with an empty snake part!");
                }
                final SnakePart actualSnakePart = snakePart.get();
                g.setColor(actualSnakePart.getColor());
                g.fillOval(0, 0, size, size);
                if (actualSnakePart.isHead()) {
                    final int lineWidth = 5;
                    final int reducedSize = size - 2 * lineWidth;
                    g.setColor(currentColor);
                    g.fillOval(lineWidth, lineWidth, reducedSize, reducedSize);
                }
                break;
            default:
                // do nothing
        }
    }

    /**
     * The field to display.
     */
    private Field field;

    /**
     * The size of this square field in pixels.
     */
    private int fieldSize;

    /**
     * @param field The field to display.
     * @param fieldSize The size of this square field in pixels.
     */
    public FieldDisplay(final Field field, final int fieldSize) {
        this.field = field;
        this.setFieldSize(fieldSize);
    }

    /**
     * @return The field to display.
     */
    public Field getField() {
        return this.field;
    }

    /**
     * @return the fieldSize
     */
    public int getFieldSize() {
        return this.fieldSize;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(this.getFieldSize(), this.getFieldSize());
    }

    /**
     * @param field The field to display.
     */
    public void setField(final Field field) {
        this.field = field;
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
        final FieldType type = this.field.getType();
        FieldDisplay.paintBackground(g, type, size);
        FieldDisplay.paintContent(g, type, this.field.getSnakePart(), size);
    }

}
