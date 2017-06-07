package control;

import javax.swing.event.*;

import model.*;

/**
 * Control for invoking turns.
 * @author cryingshadow
 */
public class TurnControl implements Runnable {

    /**
     * The competition.
     */
    private final Competition competition;

    /**
     * The competition control.
     */
    private final CompetitionControl control;

    /**
     * Are we on manual speed?
     */
    private boolean manual;

    /**
     * The settings.
     */
    private final Settings settings;

    /**
     * @param settings The settings.
     * @param competition The competition.
     * @param control The competition control.
     */
    public TurnControl(
        final Settings settings,
        final Competition competition,
        final CompetitionControl control
    ) {
        this.settings = settings;
        this.competition = competition;
        this.control = control;
        this.manual = settings.getSpeed().equals(Speed.MANUAL);
        this.settings.addChangeListener(
            new ChangeListener() {

                @Override
                public void stateChanged(final ChangeEvent e) {
                    if (TurnControl.this.manual && !settings.getSpeed().equals(Speed.MANUAL)) {
                        synchronized (TurnControl.this) {
                            TurnControl.this.manual = false;
                            TurnControl.this.notifyAll();
                        }
                    }

                }

            }
        );
    }

    @Override
    public void run() {
        try {
            while (this.competition.isRunning()) {
                final Speed speed = this.settings.getSpeed();
                if (speed.equals(Speed.MANUAL)) {
                    synchronized (this) {
                        this.manual = true;
                        this.wait();
                    }
                    continue;
                }
                this.control.turn();
                Thread.sleep(speed.getSleepTime());
            }
        } catch (final InterruptedException e) {
            e.printStackTrace();
        }
    }

}
