package control;

import java.io.*;
import java.util.*;

import control.samples.*;
import model.*;
import view.*;

/**
 * Main class. Starts the GUI and possibly pre-loads snake controls if specified by arguments.
 * @author cryingshadow
 */
public class Main {

    /**
     * Snake controls to be used when the argument "samples" is being specified for the main method.
     */
    private static final List<SnakeControl> PRE_DEFINED_SNAKE_CONTROLS =
        Arrays.asList(
            new ShortestPathSnakeControl(),
            new GreedySnakeControl(),
            new RandomSnakeControl(),
            new RotatingSnakeControl(),
            new UpLeftSnakeControl()
        );

    /**
     * @param args Can specify the folder from where to pre-load the snake controls or to use the snake controls stored
     *             in this class by specifying "samples" as the first argument. If empty, the folder for snake controls
     *             can be specified from the UI.
     */
    public static void main(final String[] args) {
        final Settings settings = new Settings();
        final Maze maze = new Maze(settings);
        final Snakes snakes = new Snakes();
        final SnakeControls snakeControls = new SnakeControls();
        final Competition competition = new Competition();
        final CompetitionControl control = new CompetitionControl(settings, maze, snakes, snakeControls, competition);
        final MainFrame frame = new MainFrame(maze, snakes, snakeControls, competition, settings, control);
        if (args.length > 0) {
            if (args[0].equals("samples")) {
                control.initSnakes(Main.PRE_DEFINED_SNAKE_CONTROLS);
            } else {
                Main.preload(args[0], settings, control);
            }
        }
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * Preloads the snake controls in the specified path.
     * @param path The path.
     * @param settings The settings.
     * @param control The competition control.
     */
    private static void preload(final String path, final Settings settings, final CompetitionControl control) {
        settings.setSourceDirectory(Optional.of(new File(path)));
        control.loadSnakes();
    }

}
