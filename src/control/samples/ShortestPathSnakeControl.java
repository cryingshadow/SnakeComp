package control.samples;

import java.util.*;
import java.util.stream.*;

import control.*;
import model.*;

public class ShortestPathSnakeControl implements SnakeControl {

    private static List<Position> getSurroundingFreePositions(final Maze maze, final Position current) {
        final List<FieldType> collisionTypes =
            List.of(
                FieldType.WALL,
                FieldType.COLLISION_ON_WALL,
                FieldType.SNAKE_BODY,
                FieldType.SNAKE_HEAD,
                FieldType.SNAKE_HEAD_EATING
            );
        return maze
            .getSurroundingPositions(current)
            .stream()
            .filter(pos -> !collisionTypes.contains(maze.getField(pos).type()))
            .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public String getName() {
        return "BFS";
    }

    @Override
    public Direction nextDirection(final Maze maze, final int xPos, final int yPos) {
        final Queue<Position> poss = new LinkedList<Position>();
        final Map<Position, Position> predecessors = new LinkedHashMap<Position, Position>();
        final Position initial = new Position(xPos, yPos);
        Position current = initial;
        predecessors.put(current, null);
        FieldType currentFieldType = maze.getField(current).type();
        while (!currentFieldType.equals(FieldType.FOOD) && !currentFieldType.equals(FieldType.COLLISION_ON_FOOD)) {
            final List<Position> surrounding = ShortestPathSnakeControl.getSurroundingFreePositions(maze, current);
            surrounding.removeAll(predecessors.keySet());
            surrounding.removeAll(poss);
            for (final Position pos : surrounding) {
                poss.offer(pos);
                predecessors.put(pos, current);
            }
            current = poss.poll();
            if (current == null) {
                // no way to food - move to any free field
                final List<Position> free = ShortestPathSnakeControl.getSurroundingFreePositions(maze, initial);
                if (free.isEmpty()) {
                    // well, there is nothing we can do... just die with your head pointing up
                    return Direction.UP;
                }
                return initial.computeDirection(free.getFirst());
            }
            currentFieldType = maze.getField(current).type();
        }
        Position previous = predecessors.get(current);
        while (!previous.equals(initial)) {
            current = previous;
            previous = predecessors.get(current);
        }
        return initial.computeDirection(current);
    }

}
