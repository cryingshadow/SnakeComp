package control;

import java.awt.*;
import java.util.*;

import javax.swing.*;

import model.*;
import view.*;

/**
 * Test class.
 * @author cryingshadow
 */
public class Main {

    /**
     * @param args Ignored.
     */
    public static void main(final String[] args) {
        final JFrame frame = new JFrame("SnakeTest");
        final Container content = frame.getContentPane();
        content.add(new MazeDisplay(new Maze(new Field[][]{
            {
                new Field(FieldType.FREE, Optional.empty()),
                new Field(FieldType.WALL, Optional.empty()),
                new Field(FieldType.FOOD, Optional.empty())
            },
            {
                new Field(FieldType.SNAKE, Optional.of(new SnakePart(Color.RED, false))),
                new Field(FieldType.SNAKE, Optional.of(new SnakePart(Color.GREEN, true))),
                new Field(FieldType.FREE, Optional.empty())
            }
        }), 50));
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

}
