package view;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.function.*;

import javax.swing.*;
import javax.swing.event.*;

import control.*;
import model.*;

/**
 * Display for the settings.
 * @author cryingshadow
 */
public class SettingsDisplay extends JPanel {

    /**
     * The maximum number of fields in a dimension.
     */
    private static final int MAXIMUM_DIMENSION = 1000;

    /**
     * The minimum number of fields in a dimension.
     */
    private static final int MINIMUM_DIMENSION = 5;

    /**
     * For serialization.
     */
    private static final long serialVersionUID = -6697575997683663504L;

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
        return SettingsDisplay.addTitle(title, spinner);
    }

    /**
     * @param sourceDirectory The source directory.
     * @return The absolute path of the specified source directory if present. The String "none" otherwise.
     */
    private static String labelTextFromSourceDirectory(final Optional<File> sourceDirectory) {
        return sourceDirectory.isPresent() ? sourceDirectory.get().getAbsolutePath() : "none";
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
     * The current number of components directly added to this display.
     */
    private int numOfComponent;

    /**
     * The settings.
     */
    private final Settings settings;

    /**
     * @param settings The settings.
     * @param competition The competition.
     * @param competitionControl The competition control.
     */
    public SettingsDisplay(
        final Settings settings,
        final Competition competition,
        final CompetitionControl competitionControl
    ) {
        this.settings = settings;
        this.competition = competition;
        this.competitionControl = competitionControl;
        this.numOfComponent = 0;
        this.setLayout(new GridBagLayout());
        this.addMazePanel();
        this.addSourceDirectoryChooser();
        this.addSpeedChooser();
    }

    /**
     * Adds a panel for maze initialization (yet without snakes and food) and configuration.
     */
    private void addMazePanel() {
        final JPanel mazePanel = new JPanel(new GridLayout(8, 1));
        mazePanel.setBorder(BorderFactory.createTitledBorder("Maze"));
        final JComboBox<Zoom> zoom =
            new JComboBox<Zoom>(new Zoom[]{Zoom.HUGE, Zoom.BIG, Zoom.NORMAL, Zoom.SMALL, Zoom.TINY});
        zoom.setSelectedItem(this.settings.getZoom());
        zoom.addActionListener(
            new ActionListener(){

                @Override
                public void actionPerformed(final ActionEvent event) {
                    try {
                        SettingsDisplay.this.settings.setZoom((Zoom)zoom.getSelectedItem());
                    } catch (final Exception e) {
                        ExceptionDisplay.showException(SettingsDisplay.this, e);
                    }
                }

            }
        );
        final JCheckBox arena = new JCheckBox("Arena", this.settings.isArena());
        arena.addActionListener(
            new ActionListener() {

                @Override
                public void actionPerformed(final ActionEvent e) {
                    SettingsDisplay.this.settings.setArena(arena.isSelected());
                }

            }
        );
        final JButton generateButton = new JButton("NEW MAZE");
        generateButton.addActionListener(
            new ActionListener(){

                @Override
                public void actionPerformed(final ActionEvent event) {
                    try {
                        SettingsDisplay.this.competitionControl.generateMaze();
                    } catch (final Exception e) {
                        ExceptionDisplay.showException(SettingsDisplay.this, e);
                    }
                }

            }
        );
        this.competition.addChangeListener(
            new ChangeListener() {

                @Override
                public void stateChanged(final ChangeEvent e) {
                    generateButton.setEnabled(!SettingsDisplay.this.competition.isRunning());
                }

            }
        );
        mazePanel.add(SettingsDisplay.addTitle("Zoom", zoom));
        mazePanel.add(
            SettingsDisplay.createSpinnerPanel(
                "Width",
                this.settings.getWidth(),
                SettingsDisplay.MINIMUM_DIMENSION,
                SettingsDisplay.MAXIMUM_DIMENSION,
                1,
                this.settings::setWidth
            )
        );
        mazePanel.add(
            SettingsDisplay.createSpinnerPanel(
                "Height",
                this.settings.getHeight(),
                SettingsDisplay.MINIMUM_DIMENSION,
                SettingsDisplay.MAXIMUM_DIMENSION,
                1,
                this.settings::setHeight
            )
        );
        mazePanel.add(
            SettingsDisplay.createSpinnerPanel(
                "Walls",
                this.settings.getWalls(),
                0,
                (SettingsDisplay.MAXIMUM_DIMENSION * SettingsDisplay.MAXIMUM_DIMENSION) / 2,
                1,
                this.settings::setWalls
            )
        );
        mazePanel.add(arena);
        mazePanel.add(
            SettingsDisplay.createSpinnerPanel(
                "Food per Snake",
                this.settings.getFoodPerSnake(),
                1,
                5,
                1,
                this.settings::setFoodPerSnake
            )
        );
        mazePanel.add(
            SettingsDisplay.createSpinnerPanel(
                "Initial Snake Length",
                this.settings.getInitialSnakeLength(),
                1,
                100,
                1,
                this.settings::setInitialSnakeLength
            )
        );
        mazePanel.add(generateButton);
        this.addWithHorizontalFill(mazePanel);
    }

    /**
     * Adds a file chooser for determining the source directory for snake controls.
     * @param initButton The button for initialization of the maze.
     */
    private void addSourceDirectoryChooser() {
        final JPanel sourceDirectoryChooser = new JPanel(new GridLayout(3, 1));
        sourceDirectoryChooser.setBorder(BorderFactory.createTitledBorder("Source Directory"));
        final JLabel currentFile =
            new JLabel(SettingsDisplay.labelTextFromSourceDirectory(this.settings.getSourceDirectory()));
        final JButton chooseButton = new JButton("BROWSE");
        final JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Choose source directory");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setMultiSelectionEnabled(false);
        chooseButton.addActionListener(
            new ActionListener(){

                @Override
                public void actionPerformed(final ActionEvent event) {
                    if (chooser.showOpenDialog(SettingsDisplay.this) == JFileChooser.APPROVE_OPTION) {
                        try {
                            SettingsDisplay.this.settings.setSourceDirectory(Optional.of(chooser.getSelectedFile()));
                        } catch (final Exception e) {
                            ExceptionDisplay.showException(SettingsDisplay.this, e);
                        }
                    }
                }

            }
        );
        this.settings.addChangeListener(
            new ChangeListener() {

                @Override
                public void stateChanged(final ChangeEvent e) {
                    currentFile.setText(
                        SettingsDisplay.labelTextFromSourceDirectory(SettingsDisplay.this.settings.getSourceDirectory())
                    );
                }

            }
        );
        this.competition.addChangeListener(
            new ChangeListener() {

                @Override
                public void stateChanged(final ChangeEvent e) {
                    chooseButton.setEnabled(!SettingsDisplay.this.competition.isRunning());
                }

            }
        );
        final JButton loadButton = new JButton("LOAD");
        loadButton.addActionListener(
            new ActionListener(){

                @Override
                public void actionPerformed(final ActionEvent event) {
                    try {
                        SettingsDisplay.this.competitionControl.loadSnakes();
                    } catch (final Exception e) {
                        ExceptionDisplay.showException(SettingsDisplay.this, e);
                    }
                }

            }
        );
        loadButton.setEnabled(!this.competition.isRunning() && this.settings.getSourceDirectory().isPresent());
        final ChangeListener loadButtonEnabler =
            new ChangeListener() {

                @Override
                public void stateChanged(final ChangeEvent e) {
                    loadButton.setEnabled(
                        !SettingsDisplay.this.competition.isRunning()
                        && SettingsDisplay.this.settings.getSourceDirectory().isPresent()
                    );
                }

            };
        this.competition.addChangeListener(loadButtonEnabler);
        this.settings.addChangeListener(loadButtonEnabler);
        sourceDirectoryChooser.add(currentFile);
        sourceDirectoryChooser.add(chooseButton);
        sourceDirectoryChooser.add(loadButton);
        this.addWithHorizontalFill(sourceDirectoryChooser);
    }

    /**
     * Adds a speed chooser to this panel.
     * @param turnButton The button for manual turn invocation.
     */
    private void addSpeedChooser() {
        final JButton turnButton = this.createManualTurnButton();
        final JComboBox<Speed> speedChooser =
            new JComboBox<Speed>(
                new Speed[]{
                    Speed.MANUAL,
                    Speed.SLOWEST,
                    Speed.SLOW,
                    Speed.NORMAL,
                    Speed.FAST,
                    Speed.RACING
                }
            );
        speedChooser.setSelectedItem(this.settings.getSpeed());
        speedChooser.addActionListener(
            new ActionListener(){

                @Override
                public void actionPerformed(final ActionEvent event) {
                    try {
                        SettingsDisplay.this.settings.setSpeed((Speed)speedChooser.getSelectedItem());
                    } catch (final Exception e) {
                        ExceptionDisplay.showException(SettingsDisplay.this, e);
                    }
                }

            }
        );
        final JPanel speedChooserPanel = new JPanel(new GridLayout(2, 1));
        speedChooserPanel.setBorder(BorderFactory.createTitledBorder("Speed"));
        speedChooserPanel.add(speedChooser);
        speedChooserPanel.add(turnButton);
        this.addWithHorizontalFill(speedChooserPanel);
    }

    /**
     * Adds the specified component with a horizontal fill option and full horizontal weight.
     * @param comp A component.
     */
    private void addWithHorizontalFill(final Component comp) {
        final GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        c.gridx = 0;
        c.gridy = this.numOfComponent++;
        this.add(comp, c);
    }

    /**
     * @return A button for invoking a manual turn.
     */
    private JButton createManualTurnButton() {
        final JButton button = new JButton("NEXT TURN");
        button.addActionListener(
            new ActionListener(){

                @Override
                public void actionPerformed(final ActionEvent event) {
                    try {
                        SettingsDisplay.this.competitionControl.turn();
                    } catch (final Exception e) {
                        ExceptionDisplay.showException(SettingsDisplay.this, e);
                    }
                }

            }
        );
        button.setEnabled(this.settings.getSpeed().equals(Speed.MANUAL));
        this.settings.addChangeListener(
            new ChangeListener() {

                @Override
                public void stateChanged(final ChangeEvent e) {
                    button.setEnabled(SettingsDisplay.this.settings.getSpeed().equals(Speed.MANUAL));
                }

            }
        );
        return button;
    }

}
