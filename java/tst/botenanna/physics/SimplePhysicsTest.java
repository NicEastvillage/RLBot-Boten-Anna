package botenanna.physics;

import botenanna.math.Vector3;
import org.junit.Test;

import static org.junit.Assert.*;

public class SimplePhysicsTest {

    @Test
    public void predictArrivalAtHeight01() {
        Rigidbody rb = new Rigidbody();
        double time = SimplePhysics.predictArrivalAtHeight(rb, 100, false);
        assertTrue("No velocity or acceleration, still reaching height.", Double.isNaN(time));
    }

    @Test
    public void predictArrivalAtHeight02() {
        Rigidbody rb = new Rigidbody();
        rb.setPosition(Vector3.UP.scale(500));
        double time = SimplePhysics.predictArrivalAtHeight(rb, 0, true);
        assertEquals(time, 1.240347346, 1E-6);
    }

    @Test
    public void predictArrivalAtHeight03() {
        Rigidbody rb = new Rigidbody();
        rb.setPosition(Vector3.UP.scale(100));
        rb.setVelocity(Vector3.UP.scale(200));
        double time = SimplePhysics.predictArrivalAtHeight(rb, 0, true);
        assertEquals(time, 0.9420162502, 1E-6);
    }

    @Test
    public void predictArrivalAtHeight04() {
        Rigidbody rb = new Rigidbody();
        rb.setPosition(Vector3.UP.scale(500));
        double time = SimplePhysics.predictArrivalAtHeight(rb, 500, false);
        assertEquals(time, 0, 1E-6);
    }

    @Test
    public void predictArrivalAtHeight05() {
        Rigidbody rb = new Rigidbody();
        rb.setPosition(Vector3.UP.scale(1000));
        rb.setVelocity(Vector3.UP.scale(-100));
        double time = SimplePhysics.predictArrivalAtHeight(rb, 500, false);
        assertEquals(time, 5, 1E-6);
    }

    @Test
    public void predictArrivalAtHeight06() {
        Rigidbody rb = new Rigidbody();
        rb.setPosition(Vector3.UP.scale(500));
        rb.setVelocity(Vector3.UP.scale(100));
        double time = SimplePhysics.predictArrivalAtHeight(rb, 200, false);
        assertTrue(Double.isNaN(time));
    }

    @Test
    public void predictArrivalAtHeight07() {
        Rigidbody rb = new Rigidbody();
        rb.setPosition(Vector3.UP.scale(200));
        double time = SimplePhysics.predictArrivalAtHeight(rb, 500, true);
        assertTrue(Double.isNaN(time));
    }

    @Test
    public void predictArrivalAtHeight08() {
        Rigidbody rb = new Rigidbody();
        rb.setPosition(Vector3.UP.scale(200));
        rb.setVelocity(Vector3.UP.scale(400));
        double time = SimplePhysics.predictArrivalAtHeight(rb, 500, true);
        assertTrue(Double.isNaN(time));
    }

    @Test
    public void predictArrivalAtHeight09() {
        Rigidbody rb = new Rigidbody();
        rb.setPosition(Vector3.UP.scale(1000));
        rb.setVelocity(Vector3.UP.scale(200));
        double time = SimplePhysics.predictArrivalAtHeight(rb, 1500, true);
        assertTrue(Double.isNaN(time));
    }

    @Test
    public void predictArrivalAtHeight10() {
        Rigidbody rb = new Rigidbody();
        rb.setPosition(Vector3.UP.scale(200));
        rb.setVelocity(Vector3.UP.scale(1000));
        double time = SimplePhysics.predictArrivalAtHeight(rb, 300, true);
        assertEquals(time, 0.1034801457, 1E-6);
    }

    @Test
    public void predictArrivalAtHeight11() {
        Rigidbody rb = new Rigidbody();
        rb.setPosition(Vector3.UP.scale(-500));
        rb.setVelocity(Vector3.UP.scale(2000));
        double time = SimplePhysics.predictArrivalAtHeight(rb, 0, true);
        assertEquals(time, 0.2610761200, 1E-6);
    }
}