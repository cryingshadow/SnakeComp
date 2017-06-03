package control;

import java.awt.*;
import java.util.*;

import javax.swing.*;

import gui.*;
import model.*;

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
        final int size = 50;
        content.setLayout(new GridLayout(3,3));
        content.add(new FieldDisplay(new Field(FieldType.FREE, Optional.empty()), size));
        content.add(new FieldDisplay(new Field(FieldType.WALL, Optional.empty()), size));
        content.add(new FieldDisplay(new Field(FieldType.FOOD, Optional.empty()), size));
        content.add(new FieldDisplay(new Field(FieldType.SNAKE, Optional.of(new SnakePart(Color.RED, false))), size));
        content.add(new FieldDisplay(new Field(FieldType.SNAKE, Optional.of(new SnakePart(Color.GREEN, true))), size));
        content.add(new FieldDisplay(new Field(FieldType.FREE, Optional.empty()), size));
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

}
