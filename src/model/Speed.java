package model;

/**
 * Execution speed of the competition.
 * @author cryingshadow
 */
public enum Speed {

    /**
     * Three turns per second.
     */
    FAST(300),

    /**
     * Execution by manual clicks.
     */
    MANUAL(-1),

    /**
     * Two turns per second.
     */
    NORMAL(500),

    /**
     * Ten turns per second.
     */
    RACING(100),

    /**
     * One turn per second.
     */
    SLOW(1000);

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
