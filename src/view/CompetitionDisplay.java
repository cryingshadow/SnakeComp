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
     * The snake controls.
     */
    private final SnakeControls snakeControls;

    /**
     * @param competition The competition.
     * @param snakeControls The snake controlss.
     * @param competitionControl The competition control.
     */
    public CompetitionDisplay(
        final Competition competition,
        final SnakeControls snakeControls,
        final CompetitionControl competitionControl
    ) {
        this.competition = competition;
        this.snakeControls = snakeControls;
        this.competitionControl = competitionControl;
        this.setLayout(new GridLayout(2, 1));
        this.setBorder(BorderFactory.createTitledBorder("Competition"));
        this.addTurnDisplay();
        this.addControlButton();
    }

    /**
     * Adds the control button.
     */
    private void addControlButton() {
        final JButton controlButton = new JButton();
        controlButton.setText(this.getControlButtonLabel());
        controlButton.addActionListener(
            new ActionListener(){

                @Override
                public void actionPerformed(final ActionEvent event) {
                    try {
                        if (CompetitionDisplay.this.competition.isRunning()) {
                            CompetitionDisplay.this.competition.setRunning(false);
                        } else {
                            CompetitionDisplay.this.competitionControl.startCompetition();
                        }
                    } catch (final Exception e) {
                        ExceptionDisplay.showException(CompetitionDisplay.this, e);
                    }
                }

            }
        );
        controlButton.setEnabled(!this.snakeControls.getSnakeControls().isEmpty());
        this.snakeControls.addChangeListener(
            new ChangeListener() {

                @Override
                public void stateChanged(final ChangeEvent e) {
                    controlButton.setEnabled(!CompetitionDisplay.this.snakeControls.getSnakeControls().isEmpty());
                }

            }
        );
        this.competition.addChangeListener(
            new ChangeListener() {

                @Override
                public void stateChanged(final ChangeEvent e) {
                    controlButton.setText(CompetitionDisplay.this.getControlButtonLabel());
                }

            }
        );
        this.add(controlButton);
    }

    /**
     * Adds the turn display.
     */
    private void addTurnDisplay() {
        final JLabel turnDisplay = new JLabel();
        turnDisplay.setText("Turns: " + this.competition.getTurns());
        this.competition.addChangeListener(
            new ChangeListener() {

                @Override
                public void stateChanged(final ChangeEvent e) {
                    turnDisplay.setText("Turns: " + CompetitionDisplay.this.competition.getTurns());
                }

            }
        );
        this.add(turnDisplay);
    }

    /**
     * @return The label text for the control button.
     */
    private String getControlButtonLabel() {
        if (this.competition.isRunning()) {
            return "ABORT";
        } else {
            return "START";
        }
    }

}

