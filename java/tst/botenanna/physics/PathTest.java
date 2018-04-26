package botenanna.physics;

import botenanna.Ball;
import botenanna.math.Vector3;
import org.junit.Test;

import static org.junit.Assert.*;

public class PathTest {

    @Test
    public void path01() {
        Path path = new Path();
        path.addTimeStep(0, new Vector3(0, 0, 0));
        path.addTimeStep(1, new Vector3(10, 20, 30));
        path.addTimeStep(2, new Vector3(20, 20, 0));

        assertEquals(0, path.evaluate(0.5).getDistanceTo(new Vector3(5, 10, 15)), 1E-10);
        assertEquals(0, path.evaluate(1.5).getDistanceTo(new Vector3(15, 20, 15)), 1E-10);
    }

    @Test
    public void path02() {
        Rigidbody ball = new Rigidbody();
        ball.setPosition(new Vector3(2400, 1000, 500));
        ball.setVelocity(new Vector3(900, -400, 1000));

        Path path = BallPhysics.getPath(ball, 10, 1);

        for (SteppedTimeLine.TimeStep timeStep : path.getTimeStep()) {
            Vector3 pos = (Vector3)timeStep.item;
            assertFalse(Double.isNaN(pos.x));
            assertFalse(Double.isNaN(pos.y));
            assertFalse(Double.isNaN(pos.z));
        }
    }

    @Test
    public void path03() {
        Rigidbody ball = new Rigidbody();
        ball.setPosition(new Vector3(-100, 490, 1200));
        ball.setVelocity(new Vector3(800, 800, 1000));

        Path path = BallPhysics.getPath(ball, 50, 1);

        for (SteppedTimeLine.TimeStep timeStep : path.getTimeStep()) {
            Vector3 pos = (Vector3)timeStep.item;
            assertFalse(Double.isNaN(pos.x));
            assertFalse(Double.isNaN(pos.y));
            assertFalse(Double.isNaN(pos.z));
        }
    }

    @Test
    public void path04() {
        Vector3 point = new Vector3(50, 50, 50);
        Path path = new Path(point);

        assertTrue(path.evaluate(0).equals(point));
        assertTrue(path.evaluate(5).equals(point));
        assertTrue(path.evaluate(100).equals(point));
    }
}