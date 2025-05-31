package view;

import java.awt.*;

import javax.swing.*;
import javax.swing.event.*;

import control.*;
import model.*;

public class MainFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    public MainFrame(
        final Maze maze,
        final Snakes snakes,
        final SnakeControls snakeControls,
        final Competition competition,
        final Settings settings,
        final CompetitionControl control
    ) {
        super("Snake Competition");
        final JPanel content = new JPanel(new GridBagLayout());
        final JPanel statsAndControls = new JPanel();
        statsAndControls.setLayout(new GridBagLayout());
        final JScrollPane scroll = new JScrollPane(content);
        this.getContentPane().add(scroll);
        final MazeDisplay mazeDisplay = new MazeDisplay(maze, settings);
        final SnakesDisplay snakesDisplay = new SnakesDisplay(settings, snakes);
        final SettingsDisplay settingsDisplay = new SettingsDisplay(settings, competition, control);
        final CompetitionDisplay competitionDisplay =
            new CompetitionDisplay(settings, competition, snakeControls, control);
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
        c.gridy = 0;
        c.anchor = GridBagConstraints.NORTHEAST;
        content.add(mazeDisplay, c);
        c.gridx = 1;
        c.anchor = GridBagConstraints.NORTH;
        c.fill = GridBagConstraints.NONE;
        c.weightx = 0.0;
        content.add(statsAndControls, c);
        settings.addChangeListener(
            new ChangeListener() {

                @Override
                public void stateChanged(final ChangeEvent e) {
                    MainFrame.this.validate();
                    content.scrollRectToVisible(statsAndControls.getBounds());
                }

            }
        );
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

}
