package model;

import java.util.*;

import control.*;

/**
 * List of snake controls.
 * @author cryingshadow
 */
public class SnakeControls extends ChangeListenable {

    /**
     * The snake controls.
     */
    private List<SnakeControl> controls;

    /**
     * Constructor.
     */
    public SnakeControls() {
        this.controls = Collections.emptyList();
    }

    /**
     * @return The snake controls.
     */
    public List<SnakeControl> getSnakeControls() {
        return this.controls;
    }

    /**
     * @param controls The snake controls.
     */
    public void setSnakeControls(final List<SnakeControl> controls) {
        this.controls = controls;
        this.notifyChangeListeners();
    }

}
