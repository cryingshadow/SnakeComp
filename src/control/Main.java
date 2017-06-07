package control;

import java.awt.*;

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
        final JPanel content = new JPanel(new FlowLayout());
        final JPanel statsAndControls = new JPanel();
        statsAndControls.setLayout(new GridLayout(2, 1));
        final JScrollPane scroll = new JScrollPane(content);
        scroll.setPreferredSize(new Dimension(1300, 1020));
        frame.getContentPane().add(scroll);
        final Settings settings = new Settings();
        final Maze maze = new Maze(settings);
        final Competition competition = new Competition();
        final Snakes snakes = new Snakes();
        final CompetitionControl control = new CompetitionControl(settings, maze, snakes, competition);
        final MazeDisplay mazeDisplay = new MazeDisplay(maze, settings);
        final SnakesDisplay snakesDisplay = new SnakesDisplay(snakes);
        final SettingsDisplay settingsDisplay = new SettingsDisplay(settings, competition, control);
        statsAndControls.add(snakesDisplay);
        statsAndControls.add(settingsDisplay);
        content.add(mazeDisplay);
        content.add(statsAndControls);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

}
