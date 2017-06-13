package control;

import java.awt.*;
import java.io.*;
import java.util.*;

import javax.swing.*;
import javax.swing.event.*;

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
        statsAndControls.setLayout(new GridBagLayout());
        final JScrollPane scroll = new JScrollPane(content);
        scroll.setPreferredSize(new Dimension(1300, 1020));
        frame.getContentPane().add(scroll);
        final Settings settings = new Settings();
        final Maze maze = new Maze(settings);
        final Competition competition = new Competition();
        final Snakes snakes = new Snakes();
        final SnakeControls snakeControls = new SnakeControls();
        final CompetitionControl control = new CompetitionControl(settings, maze, snakes, snakeControls, competition);
        final MazeDisplay mazeDisplay = new MazeDisplay(maze, settings);
        final SnakesDisplay snakesDisplay = new SnakesDisplay(snakes);
        final SettingsDisplay settingsDisplay = new SettingsDisplay(settings, competition, control);
        final CompetitionDisplay competitionDisplay = new CompetitionDisplay(competition, snakeControls, control);
        final GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        statsAndControls.add(snakesDisplay, c);
        c.gridy = 1;
        statsAndControls.add(settingsDisplay, c);
        c.gridy = 2;
        statsAndControls.add(competitionDisplay, c);
        content.add(mazeDisplay);
        content.add(statsAndControls);
        settings.addChangeListener(
            new ChangeListener() {

                @Override
                public void stateChanged(final ChangeEvent e) {
                    frame.validate();
                }

            }
        );
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        if (args.length > 0) {
            Main.preload(args[0], settings, control);
        }
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * Preloads the snake controls in the specified path.
     * @param path The path.
     * @param settings The settings.
     * @param control The competition control.
     */
    private static void preload(final String path, final Settings settings, final CompetitionControl control) {
        settings.setSourceDirectory(Optional.of(new File(path)));
        control.loadSnakes();
    }

}
