package view;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.function.*;

import javax.swing.*;
import javax.swing.event.*;

import control.*;
import model.*;

/**
 * Panel for configuring the maze.
 * @author cryingshadow
 */
public class MazeSettingsDisplay extends JPanel {

    /**
     * For serialization.
     */
    private static final long serialVersionUID = -73323973550133352L;

    /**
     * @param title The title.
     * @param comp The component.
     * @return The specified component with a title label in front of it.
     */
    private static JPanel addTitle(final String title, final Component comp) {
        final JPanel res = new JPanel(new GridBagLayout());
        final GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0.0;
        c.fill = GridBagConstraints.HORIZONTAL;
        res.add(new JLabel(title + ": "), c);
        c.gridx = 1;
        c.weightx = 1.0;
        res.add(comp, c);
        return res;
    }

    /**
     * @param title The title.
     * @param init The initial value.
     * @param min The minimum value.
     * @param max The maximum value.
     * @param step The step width.
     * @param setter The setter.
     * @return A panel with a spinner having an integer number model specified by init, min, max, and step. Moreover,
     *         the spinner has a label in front of it with the specified title and changes to the number model invoke
     *         the specified setter.
     */
    private static JPanel createSpinnerPanel(
        final String title,
        final int init,
        final int min,
        final int max,
        final int step,
        final Consumer<Integer> setter
    ) {
        final JSpinner spinner = new JSpinner(new SpinnerNumberModel(init, min, max, step));
        spinner.addChangeListener(
            new ChangeListener() {

                @Override
                public void stateChanged(final ChangeEvent e) {
                    setter.accept((Integer)spinner.getModel().getValue());
                }

            }
        );
        return MazeSettingsDisplay.addTitle(title, spinner);
    }

    /**
     * The competition.
     */
    private final Competition competition;

    /**
     * The competition control.
     */
    private final CompetitionControl competitionControl;

    /**
     * The settings.
     */
    private final Settings settings;

    /**
     * @param settings The settings.
     * @param competition The competition.
     * @param competitionControl The competition control.
     */
    public MazeSettingsDisplay(
        final Settings settings,
        final Competition competition,
        final CompetitionControl competitionControl
    ) {
        this.settings = settings;
        this.competition = competition;
        this.competitionControl = competitionControl;
        this.setLayout(new GridBagLayout());
        final GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        c.ipady = 3;
        this.setBorder(BorderFactory.createTitledBorder("Maze"));
        final JSlider zoom = new JSlider(Settings.MIN_FIELD_SIZE, Settings.MAX_FIELD_SIZE, Settings.NORMAL_FIELD_SIZE);
        zoom.setMajorTickSpacing(10);
        zoom.setPaintTicks(true);
        zoom.setPaintLabels(true);
        zoom.setLabelTable(zoom.createStandardLabels(50, 50));
        zoom.addChangeListener(
            new ChangeListener(){

                @Override
                public void stateChanged(final ChangeEvent event) {
                    try {
                        MazeSettingsDisplay.this.settings.setZoom(zoom.getValue());
                    } catch (final Exception e) {
                        ExceptionDisplay.showException(MazeSettingsDisplay.this, e);
                    }
                }

            }
        );
        final JCheckBox arena = new JCheckBox("Arena", this.settings.isArena());
        arena.addActionListener(
            new ActionListener() {

                @Override
                public void actionPerformed(final ActionEvent e) {
                    MazeSettingsDisplay.this.settings.setArena(arena.isSelected());
                }

            }
        );
        final JButton generateButton = new JButton("NEW MAZE");
        generateButton.addActionListener(
            new ActionListener(){

                @Override
                public void actionPerformed(final ActionEvent event) {
                    try {
                        MazeSettingsDisplay.this.competitionControl.generateWalls();
                    } catch (final Exception e) {
                        ExceptionDisplay.showException(MazeSettingsDisplay.this, e);
                    }
                }

            }
        );
        this.competition.addChangeListener(
            new ChangeListener() {

                @Override
                public void stateChanged(final ChangeEvent e) {
                    generateButton.setEnabled(!MazeSettingsDisplay.this.competition.isRunning());
                }

            }
        );
        c.gridy = 0;
        this.add(MazeSettingsDisplay.addTitle("Zoom", zoom), c);
        c.gridy = 1;
        this.add(
            MazeSettingsDisplay.createSpinnerPanel(
                "Width",
                this.settings.getWidth(),
                Settings.MINIMUM_DIMENSION,
                Settings.MAXIMUM_DIMENSION,
                1,
                this.settings::setWidth
            ),
            c
        );
        c.gridy = 2;
        this.add(
            MazeSettingsDisplay.createSpinnerPanel(
                "Height",
                this.settings.getHeight(),
                Settings.MINIMUM_DIMENSION,
                Settings.MAXIMUM_DIMENSION,
                1,
                this.settings::setHeight
            ),
            c
        );
        c.gridy = 3;
        this.add(
            MazeSettingsDisplay.createSpinnerPanel(
                "Walls",
                this.settings.getWalls(),
                0,
                (Settings.MAXIMUM_DIMENSION * Settings.MAXIMUM_DIMENSION) / 2,
                1,
                this.settings::setWalls
            ),
            c
        );
        c.gridy = 4;
        this.add(arena, c);
        c.gridy = 5;
        this.add(
            MazeSettingsDisplay.createSpinnerPanel(
                "Food per Snake",
                this.settings.getFoodPerSnake(),
                1,
                10,
                1,
                this.settings::setFoodPerSnake
            ),
            c
        );
        c.gridy = 6;
        this.add(
            MazeSettingsDisplay.createSpinnerPanel(
                "Initial Snake Length",
                this.settings.getInitialSnakeLength(),
                1,
                100,
                1,
                this.settings::setInitialSnakeLength
            ),
            c
        );
        c.gridy = 7;
        this.add(
            MazeSettingsDisplay.createSpinnerPanel(
                "Maximum Hunger",
                this.settings.getMaxHunger().orElse(0),
                0,
                Settings.MAXIMUM_DIMENSION * Settings.MAXIMUM_DIMENSION,
                1,
                this::setMaxHunger
            ),
            c
        );
        c.gridy = 8;
        this.add(generateButton, c);
    }

    /**
     * @param maxHunger The maximum hunger a snake can survive.
     */
    private void setMaxHunger(final int maxHunger) {
        this.settings.setMaxHunger(maxHunger == 0 ? Optional.empty() : Optional.of(maxHunger));
    }

}
