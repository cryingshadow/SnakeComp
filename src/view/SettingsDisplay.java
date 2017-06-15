package view;

import java.awt.*;

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
     * @param settings The settings.
     * @param competition The competition.
     * @param competitionControl The competition control.
     */
    public SettingsDisplay(
        final Settings settings,
        final Competition competition,
        final CompetitionControl competitionControl
    ) {
        this.setLayout(new GridBagLayout());
        final GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        c.gridx = 0;
        c.gridy = 0;
        this.add(new MazeSettingsDisplay(settings, competition, competitionControl), c);
        c.gridy = 1;
        this.add(new SourceDirectoryChooser(settings, competition, competitionControl), c);
        c.gridy = 2;
        this.add(new SpeedChooser(settings, competitionControl), c);
    }

}
