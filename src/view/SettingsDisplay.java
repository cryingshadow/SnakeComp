package view;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

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
     * For serialization.
     */
    private static final long serialVersionUID = -6697575997683663504L;

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
        this.addMazeInitializer();
        this.addSourceDirectoryChooser();
        this.addSpeedChooser();
    }

    /**
     * Adds a panel for maze initialization (yet without snakes and food).
     */
    private void addMazeInitializer() {
        final JPanel mazeInitializer = new JPanel();
        mazeInitializer.setBorder(BorderFactory.createTitledBorder("Maze"));
        final JButton generateButton = new JButton("NEW MAZE");
        generateButton.addActionListener(
            new ActionListener(){

                @Override
                public void actionPerformed(final ActionEvent e) {
                    SettingsDisplay.this.competitionControl.generateMaze();
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
        mazeInitializer.add(generateButton);
        this.addWithHorizontalFill(mazeInitializer);
    }

    /**
     * Adds a file chooser for determining the source directory for snake controls.
     * @param initButton The button for initialization of the maze.
     */
    private void addSourceDirectoryChooser() {
        final JPanel sourceDirectoryChooser = new JPanel();
        sourceDirectoryChooser.setBorder(BorderFactory.createTitledBorder("Source Directory"));
        sourceDirectoryChooser.setLayout(new GridLayout(3, 1));
        final JLabel currentFile =
            new JLabel(SettingsDisplay.labelTextFromSourceDirectory(this.settings.getSourceDirectory()));
        final JButton startButton = new JButton("START");
        startButton.addActionListener(
            new ActionListener(){

                @Override
                public void actionPerformed(final ActionEvent e) {
                    SettingsDisplay.this.competitionControl.startCompetition();
                }

            }
        );
        startButton.setEnabled(!this.competition.isRunning() && this.settings.getSourceDirectory().isPresent());
        final ChangeListener startButtonEnabler =
            new ChangeListener() {

                @Override
                public void stateChanged(final ChangeEvent e) {
                    startButton.setEnabled(
                        !SettingsDisplay.this.competition.isRunning()
                        && SettingsDisplay.this.settings.getSourceDirectory().isPresent()
                    );
                }

            };
        this.competition.addChangeListener(startButtonEnabler);
        this.settings.addChangeListener(startButtonEnabler);
        final JButton chooseButton = new JButton("Browse");
        final JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Choose source directory");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setMultiSelectionEnabled(false);
        chooseButton.addActionListener(
            new ActionListener(){

                @Override
                public void actionPerformed(final ActionEvent e) {
                    if (chooser.showOpenDialog(SettingsDisplay.this) == JFileChooser.APPROVE_OPTION) {
                        SettingsDisplay.this.settings.setSourceDirectory(Optional.of(chooser.getSelectedFile()));
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
        sourceDirectoryChooser.add(currentFile);
        sourceDirectoryChooser.add(chooseButton);
        sourceDirectoryChooser.add(startButton);
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
                public void actionPerformed(final ActionEvent e) {
                    SettingsDisplay.this.settings.setSpeed((Speed)speedChooser.getSelectedItem());
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
                public void actionPerformed(final ActionEvent e) {
                    SettingsDisplay.this.competitionControl.turn();
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
