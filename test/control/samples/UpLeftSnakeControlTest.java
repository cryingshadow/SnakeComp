package control.samples;

import org.testng.*;
import org.testng.annotations.Test;

import model.*;

public class UpLeftSnakeControlTest {

    @Test
    public void behaviorTest() {
        final UpLeftSnakeControl snake = new UpLeftSnakeControl();
        Assert.assertEquals(snake.nextDirection(null, 0, 0), Direction.UP);
        Assert.assertEquals(snake.nextDirection(null, 0, 0), Direction.LEFT);
        Assert.assertEquals(snake.nextDirection(null, 0, 0), Direction.UP);
        Assert.assertEquals(snake.nextDirection(null, 0, 0), Direction.LEFT);
    }

}
