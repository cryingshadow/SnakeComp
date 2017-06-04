package generators;

import java.awt.Color;
import java.util.*;

import control.*;
import model.*;

/**
 * Generator for initial positions of snakes in a maze.
 * @author cryingshadow
 */
public class SnakeGenerator {

    /**
     * The colors for snakes.
     */
    private static final Color[] AVAILABLE_COLORS = {Color.RED, Color.GREEN, Color.BLUE};

    /**
     * Random number generator.
     */
    private final Random random = new Random();

    /**
     * @param snakeControls The snake controls to create snakes for.
     * @param maze The maze.
     * @param settings The settings.
     * @return A collection of snakes for the specified snake controls.
     */
    public Collection<Snake> generateSnakes(
        final Collection<SnakeControl> snakeControls,
        final Maze maze,
        final Settings settings
    ) {
        final int numOfSnakes = snakeControls.size();
        if (SnakeGenerator.AVAILABLE_COLORS.length < numOfSnakes) {
            throw new IllegalArgumentException("Not enough colors available!");
        }
        final Collection<Snake> res = new ArrayList<Snake>();
        final List<Position> freePositions = maze.getFreePositions();
        if (freePositions.size() < numOfSnakes) {
            throw new IllegalArgumentException("Not enough free positions available!");
        }
        int colorIndex = 0;
        for (final SnakeControl control : snakeControls) {
            final Position pos = freePositions.remove(this.random.nextInt(freePositions.size()));
            res.add(
                new Snake(
                    pos,
                    settings.getInitialSnakeLength(),
                    settings.getMaxHunger(),
                    SnakeGenerator.AVAILABLE_COLORS[colorIndex++],
                    control
                )
            );
        }
        return res;
    }

}
