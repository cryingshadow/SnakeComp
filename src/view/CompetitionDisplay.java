package view;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;

import control.*;
import model.*;

/**
 * Display for the competition controls and meta data.
 * @author cryingshadow
 */
public class CompetitionDisplay extends JPanel {

    /**
     * For serialization.
     */
    private static final long serialVersionUID = -5836132895179451950L;

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
    public CompetitionDisplay(
        final Settings settings,
        final Competition competition,
        final CompetitionControl competitionControl
    ) {
        this.settings = settings;
        this.competition = competition;
        this.competitionControl = competitionControl;
        this.setLayout(new GridLayout(1, 1));
        this.setBorder(BorderFactory.createTitledBorder("Competition"));
        this.addControlButton();
    }

    /**
     * Adds the control button.
     */
    private void addControlButton() {
        final JButton controlButton = new JButton(this.getControlButtonLabel());
        controlButton.addActionListener(
            new ActionListener(){

                @Override
                public void actionPerformed(final ActionEvent event) {
                    try {
                        CompetitionDisplay.this.competitionControl.startCompetition();
                    } catch (final Exception e) {
                        ExceptionDisplay.showException(CompetitionDisplay.this, e);
                    }
                }

            }
        );
        controlButton.setEnabled(!this.competition.isRunning() && this.settings.getSourceDirectory().isPresent());
        final ChangeListener startButtonEnabler =
            new ChangeListener() {

                @Override
                public void stateChanged(final ChangeEvent e) {
                    controlButton.setEnabled(
                        !CompetitionDisplay.this.competition.isRunning()
                        && CompetitionDisplay.this.settings.getSourceDirectory().isPresent()
                    );
                }

            };
        this.competition.addChangeListener(startButtonEnabler);
        this.settings.addChangeListener(startButtonEnabler);
        this.add(controlButton);
    }

    /**
     * @return The label text for the control button.
     */
    private String getControlButtonLabel() {
        if (this.competition.isRunning()) {
            if (this.settings.getSpeed().equals(Speed.MANUAL)) {
                return "NEXT TURN";
            } else {
                return "ABORT";
            }
        } else {
            return "START";
        }
    }

}

