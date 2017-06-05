package view;

import java.awt.Font;
import java.awt.Component;
import java.util.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;

import model.*;

/**
 * Display for the status of the snakes.
 * @author cryingshadow
 */
public class SnakesDisplay extends JPanel {

    /**
     * The size of the padding around the snake status display.
     */
    private static final int BORDER_SIZE = 10;

    /**
     * For serialization.
     */
    private static final long serialVersionUID = 2351787889773240171L;

    /**
     * Data model for snakes.
     */
    private final SnakeListModel model;

    /**
     * @param initialSnakes The snakes.
     */
    public SnakesDisplay(final Snakes initialSnakes) {
        this.model = new SnakeListModel(new ArrayList<Snake>(initialSnakes.getAllSnakes()));
        final ListCellRenderer<Snake> renderer = new ListCellRenderer<Snake>() {

            @Override
            public Component getListCellRendererComponent(
                final JList<? extends Snake> list,
                final Snake value,
                final int index,
                final boolean isSelected,
                final boolean cellHasFocus
            ) {
                final StringBuilder text = new StringBuilder();
                text.append(value.getName());
                text.append(" (");
                text.append(value.getLength());
                text.append("): ");
                text.append(value.isAlive() ? "ALIVE" : "DEAD");
                final JLabel res = new JLabel(text.toString());
                res.setFont(new Font("Serif", Font.BOLD, 24));
                res.setForeground(value.getColor());
                res.setBackground(MazeDisplay.BACKGROUND);
                res.setOpaque(true);
                res.setBorder(
                    new EmptyBorder(
                        index == 0 ? SnakesDisplay.BORDER_SIZE: 0,
                        SnakesDisplay.BORDER_SIZE,
                        index == list.getModel().getSize() - 1 ? SnakesDisplay.BORDER_SIZE : 0,
                        SnakesDisplay.BORDER_SIZE
                    )
                );
                return res;
            }

        };
        final JList<Snake> list = new JList<Snake>(this.model);
        list.setSelectionModel(new NoSelectionModel());
        list.setCellRenderer(renderer);
        this.add(list);
    }

    /**
     * @param snakes The snakes.
     */
    public void setSnakes(final Snakes snakes) {
        this.model.setSnakes(new ArrayList<Snake>(snakes.getAllSnakes()));
        this.repaint();
    }

    /**
     * Selection model allowing no selections.
     * @author cryingshadow
     */
    private static class NoSelectionModel extends DefaultListSelectionModel {

        /**
         * For serialization.
         */
        private static final long serialVersionUID = 1997292651280481190L;

        @Override
        public void addSelectionInterval(final int index0, final int index1) {
            super.setSelectionInterval(-1, -1);
        }

        @Override
        public void setSelectionInterval(final int index0, final int index1) {
            super.setSelectionInterval(-1, -1);
        }

    }

    /**
     * List model for snakes.
     * @author cryingshadow
     */
    private static class SnakeListModel implements ListModel<Snake> {

        /**
         * Listeners.
         */
        private final List<ListDataListener> listeners = new LinkedList<ListDataListener>();

        /**
         * The snakes.
         */
        private List<Snake> snakes;

        /**
         * @param snakes The snakes.
         */
        public SnakeListModel(final List<Snake> snakes) {
            this.snakes = snakes;
        }

        @Override
        public void addListDataListener(final ListDataListener l) {
           this.listeners.add(l);
        }

        @Override
        public Snake getElementAt(final int index) {
            return this.snakes.get(index);
        }

        @Override
        public int getSize() {
            return this.snakes.size();
        }

        /**
         * Notify all listeners of the specified event.
         * @param e The event.
         */
        public void notify(final ListDataEvent e) {
            for (final ListDataListener l : this.listeners) {
                l.contentsChanged(e);
            }
        }

        @Override
        public void removeListDataListener(final ListDataListener l) {
            this.listeners.remove(l);
        }

        /**
         * @param snakes The snakes.
         */
        public void setSnakes(final List<Snake> snakes) {
            this.snakes = snakes;
            this.notify(new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, 0, snakes.size() - 1));
        }

    }

}
