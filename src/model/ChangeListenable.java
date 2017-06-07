package model;

import java.util.*;

import javax.swing.event.*;

/**
 * Abstract class for models with change listeners.
 * @author cryingshadow
 */
public abstract class ChangeListenable {

    /**
     * Listeners for changes to this model.
     */
    private final List<ChangeListener> listeners;

    /**
     * Constructor.
     */
    public ChangeListenable() {
        this.listeners = new LinkedList<ChangeListener>();
    }

    /**
     * Adds the specified change listener.
     * @param listener A change listener.
     */
    public void addChangeListener(final ChangeListener listener) {
        this.listeners.add(listener);
    }

    /**
     * Notify all registered change listeners that the settings have changed.
     */
    public void notifyChangeListeners() {
        for (final ChangeListener listener : this.listeners) {
            listener.stateChanged(new ChangeEvent(this));
        }
    }

    /**
     * Removes the specified change listener.
     * @param listener A change listener.
     * @return True if a change listener was actually removed.
     */
    public boolean removeChangeListener(final ChangeListener listener) {
        return this.listeners.remove(listener);
    }

}
