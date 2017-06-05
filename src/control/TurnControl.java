package control;

import model.*;
import view.*;

/**
 * Control for invoking turns.
 * @author cryingshadow
 */
public class TurnControl implements Runnable {

    /**
     * The competition control.
     */
    private final CompetitionControl control;

    /**
     * The maze display.
     */
    private final MazeDisplay mazeDisplay;

    /**
     * The settings.
     */
    private final Settings settings;

    /**
     * The snake status display.
     */
    private final SnakesDisplay snakesDisplay;

    /**
     * @param settings The settings.
     * @param control The competition control.
     * @param mazeDisplay The maze display.
     * @param snakesDisplay The snake status display.
     */
    public TurnControl(
        final Settings settings,
        final CompetitionControl control,
        final MazeDisplay mazeDisplay,
        final SnakesDisplay snakesDisplay
    ) {
        this.settings = settings;
        this.control = control;
        this.mazeDisplay = mazeDisplay;
        this.snakesDisplay = snakesDisplay;
    }

    @Override
    public void run() {
        try {
            while (!this.control.over()) {
                final Speed speed = this.settings.getSpeed();
                if (speed.equals(Speed.MANUAL)) {
                    synchronized (this) {
                        this.wait();
                    }
                    continue;
                }
                this.turn();
                Thread.sleep(speed.getSleepTime());
            }
        } catch (final InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Execute a turn.
     */
    public void turn() {
        this.control.turn();
        this.mazeDisplay.setMaze(this.control.getCurrentMaze());
        this.snakesDisplay.setSnakes(this.control.getSnakes());
    }

}
