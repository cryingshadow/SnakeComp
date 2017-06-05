package control;

import java.awt.*;
import java.util.List;

import javax.swing.*;

import model.*;
import util.*;
import view.*;

/**
 * Test class.
 * @author cryingshadow
 */
public class Main {

    /**
     * @param args Carries the directory of the snake controls.
     */
    public static void main(final String[] args) {
        final JFrame frame = new JFrame("SnakeTest");
        final JPanel content = new JPanel();
        final JScrollPane scroll = new JScrollPane(content);
        scroll.setPreferredSize(new Dimension(1300, 1020));
        frame.getContentPane().add(scroll);
        final List<SnakeControl> snakeControls = DynamicCompiler.compileAndLoad(args[0]);
        final CompetitionControl control = new CompetitionControl(new Settings(), snakeControls);
        final MazeDisplay mazeDisplay = new MazeDisplay(control.getCurrentMaze(), 50);
        final SnakesDisplay snakeDisplay = new SnakesDisplay(control.getSnakes());
        content.add(mazeDisplay);
        content.add(snakeDisplay);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        final Thread frameThread =
            new Thread(
                new Runnable(){

                    @Override
                    public void run() {
                        frame.setVisible(true);
                    }

                }
            );
        frameThread.start();
        try {
            while (!control.over()) {
                Thread.sleep(300);
                control.turn();
                mazeDisplay.setMaze(control.getCurrentMaze());
                snakeDisplay.setSnakes(control.getSnakes());
            }
        } catch (final InterruptedException e) {
            e.printStackTrace();
        }
    }

}
