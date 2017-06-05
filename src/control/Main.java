package control;

import java.awt.*;
import java.io.*;
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
        final JPanel content = new JPanel(new FlowLayout());
        final JPanel statsAndControls = new JPanel();
        statsAndControls.setLayout(new BoxLayout(statsAndControls, BoxLayout.Y_AXIS));
        final JScrollPane scroll = new JScrollPane(content);
        scroll.setPreferredSize(new Dimension(1300, 1020));
        frame.getContentPane().add(scroll);
        final Settings settings = new Settings();
        final List<SnakeControl> snakeControls = DynamicCompiler.compileAndLoad(new File(args[0]));
        final CompetitionControl control = new CompetitionControl(settings, snakeControls);
        final MazeDisplay mazeDisplay = new MazeDisplay(control.getCurrentMaze(), 50);
        final SnakesDisplay snakesDisplay = new SnakesDisplay(control.getSnakes());
        final TurnControl turnControl = new TurnControl(settings, control, mazeDisplay, snakesDisplay);
        final SettingsDisplay settingsDisplay = new SettingsDisplay(settings, turnControl);
        statsAndControls.add(snakesDisplay);
        statsAndControls.add(settingsDisplay);
        content.add(mazeDisplay);
        content.add(statsAndControls);
        final Thread turnThread = new Thread(turnControl);
        turnThread.start();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

}
