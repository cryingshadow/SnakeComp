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
     * The color of a free field.
     */
    private static final Color BACKGROUND = Color.BLACK;

    /**
     * The color of a collision.
     */
    private static final Color COLLISION = Color.YELLOW;

    /**
     * The color of food.
     */
    private static final Color FOOD = Color.WHITE;

    /**
     * The line width for a snake's head.
     */
    private static final int HEAD_LINE_WIDTH = 5;

    /**
     * For serialization.
     */
    private static final long serialVersionUID = 3055355083944967024L;

    /**
     * The color of a wall.
     */
    private static final Color WALL = Color.GRAY;

    /**
     * Paints the content of the field on top of its background (i.e., paintBackground must have been called before).
     * @param g The graphics.
     * @param x The offset on the x-axis.
     * @param y The offset on the y-axis.
     * @param field The field.
     * @param size The field size.
     */
    private static void paintField(
        final Graphics g,
        final int x,
        final int y,
        final Field field,
        final int size
    ) {
        MazeDisplay.paintFieldBackground(g, x, y, size, MazeDisplay.BACKGROUND);
        final Optional<Color> snakeColor = field.getSnakeColor();
        switch (field.getType()) {
            case COLLISION:
                MazeDisplay.paintFieldBackground(g, x, y, size, MazeDisplay.COLLISION);
                break;
            case FOOD:
                g.setColor(MazeDisplay.FOOD);
                g.fillOval(x, y, size, size);
                break;
            case FREE:
                // do nothing
                break;
            case SNAKE_BODY:
                MazeDisplay.paintSnakePart(g, x, y, size, snakeColor);
                break;
            case SNAKE_HEAD:
                MazeDisplay.paintSnakePart(g, x, y, size, snakeColor);
                MazeDisplay.paintHead(g, x, y, size, MazeDisplay.BACKGROUND);
                break;
            case SNAKE_HEAD_EATING:
                MazeDisplay.paintSnakePart(g, x, y, size, snakeColor);
                MazeDisplay.paintHead(g, x, y, size, MazeDisplay.FOOD);
                break;
            case WALL:
                MazeDisplay.paintFieldBackground(g, x, y, size, MazeDisplay.WALL);
                break;
        }
    }

    /**
     * Paints the background of the field.
     * @param g The graphics.
     * @param x The offset on the x-axis.
     * @param y The offset on the y-axis.
     * @param size The field size.
     * @param color The background color.
     */
    private static void paintFieldBackground(
        final Graphics g,
        final int x,
        final int y,
        final int size,
        final Color color
    ) {
        g.setColor(color);
        g.fillRect(x, y, size, size);
    }

    /**
     * Paints the interior of a snake's head.
     * @param g The graphics.
     * @param x The offset on the x-axis.
     * @param y The offset on the y-axis.
     * @param size The field size.
     * @param color The inner head color.
     */
    private static void paintHead(final Graphics g, final int x, final int y, final int size, final Color color) {
        final int reducedSize = size - 2 * MazeDisplay.HEAD_LINE_WIDTH;
        g.setColor(color);
        g.fillOval(x + MazeDisplay.HEAD_LINE_WIDTH, y + MazeDisplay.HEAD_LINE_WIDTH, reducedSize, reducedSize);
    }

    /**
     * Paints a part of a snake.
     * @param g The graphics.
     * @param x The offset on the x-axis.
     * @param y The offset on the y-axis.
     * @param size The field size.
     * @param color The background color.
     */
    private static void paintSnakePart(
        final Graphics g,
        final int x,
        final int y,
        final int size,
        final Optional<Color> color
    ) {
        if (!color.isPresent()) {
            throw new IllegalArgumentException("Type SNAKE is incompatible with an empty snake part!");
        }
        final Color snakeColor = color.get();
        g.setColor(snakeColor);
        g.fillOval(x, y, size, size);
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
                final int xOffset = j * size;
                MazeDisplay.paintField(g, xOffset, yOffset, fieldRow[j], size);
            }
        }
    }

}
