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
 * Panel for selecting the source directory for snake controls and loading them.
 * @author cryingshadow
 */
public class SourceDirectoryChooser extends JPanel {

    /**
     * For serialization.
     */
    private static final long serialVersionUID = 1664432915770702824L;

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
     * The settings.
     */
    private final Settings settings;

    /**
     * @param settings The settings.
     * @param competition The competition.
     * @param competitionControl The competition control.
     */
    public SourceDirectoryChooser(
        final Settings settings,
        final Competition competition,
        final CompetitionControl competitionControl
    ) {
        this.settings = settings;
        this.competition = competition;
        this.competitionControl = competitionControl;
        this.setLayout(new GridLayout(3, 1));
        this.setBorder(BorderFactory.createTitledBorder("Source Directory"));
        final JLabel currentFile =
            new JLabel(SourceDirectoryChooser.labelTextFromSourceDirectory(this.settings.getSourceDirectory()));
        final JButton chooseButton = new JButton("BROWSE");
        final JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Choose source directory");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setMultiSelectionEnabled(false);
        chooseButton.addActionListener(
            new ActionListener(){

                @Override
                public void actionPerformed(final ActionEvent event) {
                    if (chooser.showOpenDialog(SourceDirectoryChooser.this) == JFileChooser.APPROVE_OPTION) {
                        try {
                            SourceDirectoryChooser.this.settings.setSourceDirectory(
                                Optional.of(chooser.getSelectedFile())
                            );
                        } catch (final Exception e) {
                            ExceptionDisplay.showException(SourceDirectoryChooser.this, e);
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
                        SourceDirectoryChooser.labelTextFromSourceDirectory(
                            SourceDirectoryChooser.this.settings.getSourceDirectory()
                        )
                    );
                }

            }
        );
        this.competition.addChangeListener(
            new ChangeListener() {

                @Override
                public void stateChanged(final ChangeEvent e) {
                    chooseButton.setEnabled(!SourceDirectoryChooser.this.competition.isRunning());
                }

            }
        );
        final JButton loadButton = new JButton("LOAD");
        loadButton.addActionListener(
            new ActionListener(){

                @Override
                public void actionPerformed(final ActionEvent event) {
                    try {
                        SourceDirectoryChooser.this.competitionControl.loadSnakes();
                    } catch (final Exception e) {
                        ExceptionDisplay.showException(SourceDirectoryChooser.this, e);
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
                        !SourceDirectoryChooser.this.competition.isRunning()
                        && SourceDirectoryChooser.this.settings.getSourceDirectory().isPresent()
                    );
                }

            };
        this.competition.addChangeListener(loadButtonEnabler);
        this.settings.addChangeListener(loadButtonEnabler);
        this.add(currentFile);
        this.add(chooseButton);
        this.add(loadButton);
    }

}
