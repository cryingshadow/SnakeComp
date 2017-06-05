package view;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

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
     * The settings.
     */
    private final Settings settings;

    /**
     * The turn control.
     */
    private final TurnControl turnControl;

    /**
     * @param initialSettings The settings.
     * @param turnControl
     */
    public SettingsDisplay(final Settings initialSettings, final TurnControl turnControl) {
        this.settings = initialSettings;
        this.turnControl = turnControl;
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.addSpeedChooser();
        this.addManualTurnButton();
    }

    /**
     * Adds a button for invoking a manual turn.
     */
    private void addManualTurnButton() {
        final JButton button = new JButton("NEXT TURN");
        button.addActionListener(
            new ActionListener(){

                @Override
                public void actionPerformed(final ActionEvent e) {
                    SettingsDisplay.this.turnControl.turn();
                }

            }
        );
        this.add(button);
    }

    /**
     * Adds a speed chooser to this panel.
     */
    private void addSpeedChooser() {
        final JComboBox<Speed> speedChooser =
            new JComboBox<Speed>(new Speed[]{Speed.MANUAL, Speed.SLOW, Speed.NORMAL, Speed.FAST, Speed.RACING});
        speedChooser.setSelectedItem(this.settings.getSpeed());
        speedChooser.addActionListener(
            new ActionListener(){

                @Override
                public void actionPerformed(final ActionEvent e) {
                    final Speed oldSpeed = SettingsDisplay.this.settings.getSpeed();
                    final Speed newSpeed = (Speed)speedChooser.getSelectedItem();
                    SettingsDisplay.this.settings.setSpeed(newSpeed);
                    if (oldSpeed.equals(Speed.MANUAL) && !newSpeed.equals(Speed.MANUAL)) {
                        synchronized (SettingsDisplay.this.turnControl) {
                            SettingsDisplay.this.turnControl.notifyAll();
                        }
                    }
                }

            }
        );
        final JPanel speedChooserPanel = new JPanel(new FlowLayout());
        speedChooserPanel.add(new JLabel("Speed:"));
        speedChooserPanel.add(speedChooser);
        this.add(speedChooserPanel);
    }

}
