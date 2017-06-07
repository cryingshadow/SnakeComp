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
        return this.getPositions(field -> field.getType().equals(FieldType.SNAKE_HEAD_EATING));
    }

    /**
     * @return The positions with food on them that is not being eaten.
     */
    public Collection<Position> getFood() {
        return this.getPositions(field -> field.getType().equals(FieldType.FOOD));
    }

    /**
     * @return The free positions in this maze.
     */
    public List<Position> getFreePositions() {
        return this.getPositions(field -> field.getType().equals(FieldType.FREE));
    }

    /**
     * @return The maze
     */
    public Field[][] getMaze() {
        return this.maze;
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
     * @return The number of fields with food in this maze.
     */
    public int numOfFood() {
        return
            this.getPositions(
                field -> field.getType().equals(FieldType.FOOD) || field.getType().equals(FieldType.COLLISION_ON_FOOD)
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
