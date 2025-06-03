package model;

import java.util.*;
import java.util.function.*;

/**
 * A maze is a rectangular collection of fields.
 * @author cryingshadow
 */
public class Maze extends ChangeListenable {

    /**
     * The maze.
     */
    private Field[][] maze;

    /**
     * @param maze The maze.
     */
    public Maze(final Field[][] maze) {
        this.maze = maze;
    }

    /**
     * @param maze The maze.
     */
    public Maze(final Maze maze) {
        this.maze = maze.maze;
    }

    /**
     * Creates a maze according to the specified settings with free fields.
     * @param settings The settings.
     * @param height The height of the maze.
     */
    public Maze(final Settings settings) {
        if (settings.isArena()) {
            this.maze = new Field[settings.getHeight() + 2][settings.getWidth() + 2];
            for (int i = 1; i < this.maze.length - 1; i++) {
                for (int j = 1; j < this.maze[i].length - 1; j++) {
                    this.maze[i][j] = new Field(FieldType.FREE, Optional.empty());
                }
            }
            for (int i = 0; i < this.maze.length; i++) {
                this.maze[i][0] = new Field(FieldType.WALL, Optional.empty());
                this.maze[i][this.maze[i].length - 1] = new Field(FieldType.WALL, Optional.empty());
            }
            for (int i = 0; i < this.maze[0].length; i++) {
                this.maze[0][i] = new Field(FieldType.WALL, Optional.empty());
                this.maze[this.maze.length - 1][i] = new Field(FieldType.WALL, Optional.empty());
            }
        } else {
            this.maze = new Field[settings.getHeight()][settings.getWidth()];
            for (int i = 0; i < this.maze.length; i++) {
                for (int j = 0; j < this.maze[i].length; j++) {
                    this.maze[i][j] = new Field(FieldType.FREE, Optional.empty());
                }
            }
        }
    }

    /**
     * @return The positions of food that is being eaten.
     */
    public Collection<Position> getEatenFood() {
        return this.getPositions(field -> field.type().equals(FieldType.SNAKE_HEAD_EATING));
    }

    /**
     * @param x The horizontal position.
     * @param y The vertical position.
     * @return The field at the specified position.
     */
    public Field getField(final int x, final int y) {
        return this.maze[y][x];
    }

    /**
     * @param pos A position.
     * @return The field at the specified position.
     */
    public Field getField(final Position pos) {
        return this.getField(pos.getX(), pos.getY());
    }

    /**
     * @return The positions with food on them that is not being eaten.
     */
    public Collection<Position> getFood() {
        return this.getPositions(field -> field.type().equals(FieldType.FOOD));
    }

    /**
     * @return The free positions in this maze.
     */
    public List<Position> getFreePositions() {
        return this.getPositions(field -> field.type().equals(FieldType.FREE));
    }

    /**
     * @return The height of this maze.
     */
    public int getHeight() {
        return this.maze.length;
    }

    /**
     * @param predicate Some predicate on fields.
     * @return The positions of the fields satisfying the predicate.
     */
    public List<Position> getPositions(final Predicate<Field> predicate) {
        final List<Position> res = new LinkedList<Position>();
        for (int y = 0; y < this.maze.length; y++) {
            final Field[] mazeRow = this.maze[y];
            for (int x = 0; x < mazeRow.length; x++) {
                if (predicate.test(mazeRow[x])) {
                    res.add(new Position(x, y));
                }
            }
        }
        return res;
    }

    /**
     * @param x The horizontal position.
     * @param y The vertical position.
     * @return The surrounding positions of the specified one.
     */
    public List<Position> getSurroundingPositions(final int x, final int y) {
        final int width = this.getWidth();
        final int height = this.getHeight();
        final List<Position> res = new ArrayList<Position>(4);
        res.add(new Position(x == 0 ? width - 1 : x - 1, y));
        res.add(new Position(x == width - 1 ? 0 : x + 1, y));
        res.add(new Position(x, y == 0 ? height - 1 : y - 1));
        res.add(new Position(x, y == height - 1 ? 0 : y + 1));
        return res;
    }

    /**
     * @param pos A position.
     * @return The surrounding positions of the specified one.
     */
    public List<Position> getSurroundingPositions(final Position pos) {
        return this.getSurroundingPositions(pos.getX(), pos.getY());
    }

    /**
     * @return The width of this maze.
     */
    public int getWidth() {
        return this.maze[0].length;
    }

    /**
     * @return The number of fields with food in this maze.
     */
    public int numOfFood() {
        return
            this.getPositions(
                field -> field.type().equals(FieldType.FOOD) || field.type().equals(FieldType.COLLISION_ON_FOOD)
            ).size();
    }

    /**
     * @param maze The maze.
     */
    public void setMaze(final Field[][] maze) {
        this.maze = maze;
        this.notifyChangeListeners();
    }

}
