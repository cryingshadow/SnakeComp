package control.samples;

import java.awt.*;
import java.util.Optional;

import org.testng.*;
import org.testng.annotations.*;

import control.*;
import model.*;

public class ImprovedShortestPathSnakeControlTest {

    private static final SnakeControl CONTROL = new ImprovedShortestPathSnakeControl();

    @DataProvider
    public Object[][] nextDirectionData() {
        return new Object[][] {
            {
                new Maze(new Field[][] {
                    {new Field(FieldType.FREE), new Field(FieldType.FREE), new Field(FieldType.SNAKE_HEAD, Optional.of(Color.RED)), new Field(FieldType.FREE), new Field(FieldType.FOOD)},
                    {new Field(FieldType.FREE), new Field(FieldType.FREE), new Field(FieldType.FOOD), new Field(FieldType.FREE), new Field(FieldType.FREE)},
                    {new Field(FieldType.FREE), new Field(FieldType.FREE), new Field(FieldType.SNAKE_HEAD, Optional.of(Color.BLUE)), new Field(FieldType.FREE), new Field(FieldType.FREE)}
                }),
                new Position(2, 0),
                Direction.RIGHT
            }
        };
    }

    @Test(dataProvider="nextDirectionData")
    public void nextDirectionTest(final Maze maze, final Position position, final Direction expected) {
        Assert.assertEquals(
            ImprovedShortestPathSnakeControlTest.CONTROL.nextDirection(maze, position.getX(), position.getY()),
            expected
        );
    }

}
