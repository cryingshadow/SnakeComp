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
        statsAndControls.setLayout(new GridBagLayout());
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
        final CompetitionDisplay competitionDisplay = new CompetitionDisplay(settings, competition, control);
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
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

}
