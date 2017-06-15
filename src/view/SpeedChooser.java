package view;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;

import control.*;
import model.*;

/**
 * Panel for choosing the competition execution speed.
 * @author cryingshadow
 */
public class SpeedChooser extends JPanel {

    /**
     * For serialization.
     */
    private static final long serialVersionUID = 1012068315514296270L;

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
     * @param competitionControl The competition control.
     */
    public SpeedChooser(final Settings settings, final CompetitionControl competitionControl) {
        this.settings = settings;
        this.competitionControl = competitionControl;
        this.setLayout(new GridLayout(2, 1));
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
                        SpeedChooser.this.settings.setSpeed((Speed)speedChooser.getSelectedItem());
                    } catch (final Exception e) {
                        ExceptionDisplay.showException(SpeedChooser.this, e);
                    }
                }

            }
        );
        this.setBorder(BorderFactory.createTitledBorder("Speed"));
        this.add(speedChooser);
        this.add(turnButton);
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
                        SpeedChooser.this.competitionControl.turn();
                    } catch (final Exception e) {
                        ExceptionDisplay.showException(SpeedChooser.this, e);
                    }
                }

            }
        );
        button.setEnabled(this.settings.getSpeed().equals(Speed.MANUAL));
        this.settings.addChangeListener(
            new ChangeListener() {

                @Override
                public void stateChanged(final ChangeEvent e) {
                    button.setEnabled(SpeedChooser.this.settings.getSpeed().equals(Speed.MANUAL));
                }

            }
        );
        return button;
    }

}
