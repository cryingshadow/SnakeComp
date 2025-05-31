package view;

import java.awt.*;
import java.util.*;
import java.util.List;

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
     * Configures the specified label.
     * @param label The label to be configured.
     * @param list The list of entries to configure such a label for.
     * @param index The index of the entry in the list for which to configure the label.
     * @param color The foreground color of the label.
     */
    private static void configureLabel(final JLabel label, final JList<? extends Snake> list, final int index, final Color color) {
        label.setFont(new Font("Serif", Font.BOLD, 24));
        label.setForeground(color);
        label.setBackground(MazeDisplay.BACKGROUND);
        label.setOpaque(true);
        label.setBorder(
            new EmptyBorder(
                index == 0 ? SnakesDisplay.BORDER_SIZE: 0,
                SnakesDisplay.BORDER_SIZE,
                index == list.getModel().getSize() - 1 ? SnakesDisplay.BORDER_SIZE : 0,
                SnakesDisplay.BORDER_SIZE
            )
        );
    }

    /**
     * Data model for snakes.
     */
    private final SnakeListModel model;

    /**
     * The settings.
     */
    private final Settings settings;

    /**
     * @param settings The settings.
     * @param snakes The snakes.
     */
    public SnakesDisplay(final Settings settings, final Snakes snakes) {
        this.settings = settings;
        this.model = new SnakeListModel(snakes);
        this.settings.addChangeListener(
            new ChangeListener() {

                @Override
                public void stateChanged(final ChangeEvent e) {
                    SnakesDisplay.this.repaint();
                }

            }
        );
        this.model.addListDataListener(
            new ListDataListener() {

                @Override
                public void contentsChanged(final ListDataEvent e) {
                    SnakesDisplay.this.repaint();
                }

                @Override
                public void intervalAdded(final ListDataEvent e) {
                    this.contentsChanged(e);
                }

                @Override
                public void intervalRemoved(final ListDataEvent e) {
                    this.contentsChanged(e);
                }

            }
        );
        final ListCellRenderer<Snake> renderer = new ListCellRenderer<Snake>() {

            @Override
            public Component getListCellRendererComponent(
                final JList<? extends Snake> list,
                final Snake value,
                final int index,
                final boolean isSelected,
                final boolean cellHasFocus
            ) {
                if (value == null) {
                    final JLabel res = new JLabel("no snakes");
                    SnakesDisplay.configureLabel(res, list, index, Color.WHITE);
                    return res;
                }
                final StringBuilder text = new StringBuilder();
                text.append(value.getName());
                text.append(" (");
                text.append(value.getCurrentLength());
                if (SnakesDisplay.this.settings.isRespawning()) {
                    text.append("/");
                    text.append(value.getMaxLength());
                }
                text.append("): ");
                text.append(value.isAlive() ? "ALIVE" : "DEAD");
                final JLabel res = new JLabel(text.toString());
                SnakesDisplay.configureLabel(res, list, index, value.getColor());
                return res;
            }

        };
        final JList<Snake> list = new JList<Snake>(this.model);
        list.setSelectionModel(new NoSelectionModel());
        list.setCellRenderer(renderer);
        list.setOpaque(false);
        this.add(list);
        this.setBorder(BorderFactory.createTitledBorder("Snake Status"));
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
         * The number of snakes.
         */
        private int size;

        /**
         * The snakes.
         */
        private final Snakes snakes;

        /**
         * @param snakes The snakes.
         */
        public SnakeListModel(final Snakes snakes) {
            this.snakes = snakes;
            this.size = snakes.getAllSnakes().size();
            this.snakes.addChangeListener(
                new ChangeListener() {

                    @Override
                    public void stateChanged(final ChangeEvent event) {
                        try {
                            final int newSize = snakes.getAllSnakes().size();
                            final int oldSize = SnakeListModel.this.size;
                            SnakeListModel.this.size = newSize;
                            SnakeListModel.this.notify(
                                new ListDataEvent(
                                    event.getSource(),
                                    ListDataEvent.CONTENTS_CHANGED,
                                    0,
                                    Math.max(oldSize, newSize) - 1
                                )
                            );
                        } catch (final Exception e) {
                            ExceptionDisplay.showException(null, e);
                        }
                    }

                }
            );
        }

        @Override
        public void addListDataListener(final ListDataListener l) {
           this.listeners.add(l);
        }

        @Override
        public Snake getElementAt(final int index) {
            if (this.size == 0) {
                return null;
            }
            return this.snakes.getAllSnakes().get(index);
        }

        @Override
        public int getSize() {
            return this.size == 0 ? 1 : this.size;
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

    }

}
