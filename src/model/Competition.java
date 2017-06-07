package model;

/**
 * Model for a snake competition.
 * @author cryingshadow
 */
public class Competition extends ChangeListenable {

    /**
     * Is the competition running?
     */
    private boolean running;

    /**
     * The number of turns.
     */
    private int turns;

    /**
     * Constructor.
     */
    public Competition() {
        this.reset();
    }

    /**
     * @return The number of turns.
     */
    public int getTurns() {
        return this.turns;
    }

    /**
     * Increases the number of turns by one.
     */
    public void increaseTurns() {
        this.turns++;
        this.notifyChangeListeners();
    }

    /**
     * @return Is the competition running?
     */
    public boolean isRunning() {
        return this.running;
    }

    /**
     * Stops and resets the competition to zero turns.
     */
    public void reset() {
        this.running = false;
        this.turns = 0;
        this.notifyChangeListeners();
    }

    /**
     * @param running Is the competition running?
     */
    public void setRunning(final boolean running) {
        this.running = running;
        this.notifyChangeListeners();
    }

}
