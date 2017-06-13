package view;

import java.awt.*;
import java.util.*;

import javax.swing.*;
import javax.swing.event.*;

import model.*;

/**
 * Display for a maze.
 * @author cryingshadow
 */
public class MazeDisplay extends JPanel {

    /**
     * The color of a free field.
     */
    public static final Color BACKGROUND = Color.BLACK;

    /**
     * The color of a collision.
     */
    private static final Color COLLISION = Color.YELLOW;

    /**
     * The color of food.
     */
    private static final Color FOOD = Color.WHITE;

    /**
     * The line width factor for a snake's head.
     */
    private static final int HEAD_LINE_WIDTH_FACTOR = 10;

    /**
     * For serialization.
     */
    private static final long serialVersionUID = 3055355083944967024L;

    /**
     * The color of a wall.
     */
    private static final Color WALL = Color.GRAY;

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
     * The maze.
     */
    private Maze maze;

    /**
     * The settings.
     */
    private final Settings settings;

    /**
     * @param maze The maze.
     * @param settings The settings.
     */
    public MazeDisplay(final Maze maze, final Settings settings) {
        if (maze.getWidth() < 1 || maze.getHeight() < 1) {
            throw new IllegalArgumentException("Maze must have at least one row and column!");
        }
        this.maze = maze;
        this.settings = settings;
        this.maze.addChangeListener(
            new ChangeListener() {

                @Override
                public void stateChanged(final ChangeEvent e) {
                    MazeDisplay.this.repaint();
                }

            }
        );
        this.settings.addChangeListener(
            new ChangeListener() {

                @Override
                public void stateChanged(final ChangeEvent e) {
                    MazeDisplay.this.invalidate();
                }

            }
        );
    }

    @Override
    public Dimension getPreferredSize() {
        final int size = this.settings.getZoom().getFieldSize();
        return new Dimension(size * this.maze.getWidth(), size * this.maze.getHeight());
    }

    /**
     * @param maze The maze.
     */
    public void setMaze(final Maze maze) {
        this.maze = maze;
        this.repaint();
    }

    @Override
    protected void paintComponent(final Graphics g) {
        final int size = this.settings.getZoom().getFieldSize();
        final int height = this.maze.getHeight();
        for (int i = 0; i < height; i++) {
            final int yOffset = (height - i - 1) * size;
            for (int j = 0; j < this.maze.getWidth(); j++) {
                final int xOffset = j * size;
                this.paintField(g, xOffset, yOffset, this.maze.getField(j, i), size);
            }
        }
    }

    /**
     * Paints the content of the field on top of its background (i.e., paintBackground must have been called before).
     * @param g The graphics.
     * @param x The offset on the x-axis.
     * @param y The offset on the y-axis.
     * @param field The field.
     * @param size The field size.
     */
    private void paintField(
        final Graphics g,
        final int x,
        final int y,
        final Field field,
        final int size
    ) {
        MazeDisplay.paintFieldBackground(g, x, y, size, MazeDisplay.BACKGROUND);
        final Optional<Color> snakeColor = field.getSnakeColor();
        switch (field.getType()) {
            case COLLISION_ON_FOOD:
            case COLLISION_ON_FREE:
            case COLLISION_ON_WALL:
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
                this.paintHead(g, x, y, size, MazeDisplay.BACKGROUND);
                break;
            case SNAKE_HEAD_EATING:
                MazeDisplay.paintSnakePart(g, x, y, size, snakeColor);
                this.paintHead(g, x, y, size, MazeDisplay.FOOD);
                break;
            case WALL:
                MazeDisplay.paintFieldBackground(g, x, y, size, MazeDisplay.WALL);
                break;
        }
    }

    /**
     * Paints the interior of a snake's head.
     * @param g The graphics.
     * @param x The offset on the x-axis.
     * @param y The offset on the y-axis.
     * @param size The field size.
     * @param color The inner head color.
     */
    private void paintHead(final Graphics g, final int x, final int y, final int size, final Color color) {
        final int lineWidth = (this.settings.getZoom().getFieldSize() / MazeDisplay.HEAD_LINE_WIDTH_FACTOR);
        final int reducedSize = size - 2 * lineWidth;
        g.setColor(color);
        g.fillOval(x + lineWidth, y + lineWidth, reducedSize, reducedSize);
    }

}
