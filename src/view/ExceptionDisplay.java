package view;

import java.awt.*;

import javax.swing.*;

/**
 * Display for exceptions.
 * @author cryingshadow
 */
public class ExceptionDisplay {

    /**
     * @param parent The parent component for the exception display.
     * @param e The exception.
     */
    public static void showException(final Component parent, final Exception e) {
        JOptionPane.showMessageDialog(parent, e.getClass().getName() + ": " + e.getMessage());
    }

}
