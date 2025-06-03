package control.samples;

import java.util.*;
import java.util.stream.*;

import control.*;
import model.*;

public class ImprovedShortestPathSnakeControl implements SnakeControl {

    private static final List<FieldType> SNAKE_HEADS = List.of(FieldType.SNAKE_HEAD, FieldType.SNAKE_HEAD_EATING);

    private static List<Position> getSurroundingFreePositions(
        final Maze maze,
        final Position current,
        final boolean avoidNearHeads
    ) {
        return maze
            .getSurroundingPositions(current)
            .stream()
            .filter(pos -> !maze.getField(pos).type().isObstacle)
            .filter(
                pos ->
                    !avoidNearHeads
                    || !maze
                    .getSurroundingPositions(pos)
                    .stream()
                    .anyMatch(
                        nextPos ->
                            !nextPos.equals(current)
                            && ImprovedShortestPathSnakeControl.SNAKE_HEADS.contains(maze.getField(nextPos).type())
                    )
            ).collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public String getName() {
        return "IBFS";
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
            final List<Position> surrounding =
                ImprovedShortestPathSnakeControl.getSurroundingFreePositions(maze, current, true);
            surrounding.removeAll(predecessors.keySet());
            surrounding.removeAll(poss);
            for (final Position pos : surrounding) {
                poss.offer(pos);
                predecessors.put(pos, current);
            }
            current = poss.poll();
            if (this.noWayToFood(current)) {
                final List<Position> free =
                    ImprovedShortestPathSnakeControl.getSurroundingFreePositions(maze, initial, true);
                if (free.isEmpty()) {
                    final List<Position> unsaveFree =
                        ImprovedShortestPathSnakeControl.getSurroundingFreePositions(maze, initial, false);
                    if (unsaveFree.isEmpty()) {
                        return Direction.UP;
                    }
                    return initial.computeDirection(unsaveFree.getFirst());
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

    private boolean noWayToFood(final Position current) {
        return current == null;
    }

}
