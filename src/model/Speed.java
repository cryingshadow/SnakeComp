package model;

/**
 * Execution speed of the competition.
 * @author cryingshadow
 */
public enum Speed {

    /**
     * Five turns per second.
     */
    FAST(200),

    /**
     * Execution by manual clicks.
     */
    MANUAL(-1),

    /**
     * Three turns per second.
     */
    NORMAL(300),

    /**
     * Ten turns per second.
     */
    RACING(100),

    /**
     * Two turns per second.
     */
    SLOW(500),

    /**
     * One turn per second.
     */
    SLOWEST(1000);

    /**
     * The time to sleep between turns in milliseconds.
     */
    private final long sleepTime;

    /**
     * @param sleepTime The time to sleep between turns in milliseconds.
     */
    private Speed(final long sleepTime) {
        this.sleepTime = sleepTime;
    }

    /**
     * @return The time to sleep between turns in milliseconds.
     */
    public long getSleepTime() {
        return this.sleepTime;
    }

}
