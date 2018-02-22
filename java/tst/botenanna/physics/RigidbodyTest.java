package botenanna.physics;

import botenanna.math.Vector3;
import org.junit.Test;

import static org.junit.Assert.*;

public class RigidbodyTest {

    @Test
    public void predictArrivalAtHeight01() {
        Rigidbody rb = new Rigidbody();
        double time = rb.predictArrivalAtHeight(100);
        assertTrue("No velocity or acceleration, still reaching height.", Double.isNaN(time));
    }

    @Test
    public void predictArrivalAtHeight02() {
        Rigidbody rb = new Rigidbody();
        rb.setPosition(Vector3.UP.scale(500));
        rb.setAffectedByGravity(true);
        double time = rb.predictArrivalAtHeight(0);
        assertEquals(time, 1.240347346, 1E-6);
    }

    @Test
    public void predictArrivalAtHeight03() {
        Rigidbody rb = new Rigidbody();
        rb.setPosition(Vector3.UP.scale(100));
        rb.setVelocity(Vector3.UP.scale(200));
        rb.setAffectedByGravity(true);
        double time = rb.predictArrivalAtHeight(0);
        assertEquals(time, 0.9420162502, 1E-6);
    }

    @Test
    public void predictArrivalAtHeight04() {
        Rigidbody rb = new Rigidbody();
        rb.setPosition(Vector3.UP.scale(500));
        double time = rb.predictArrivalAtHeight(500);
        assertEquals(time, 0, 1E-6);
    }

    @Test
    public void predictArrivalAtHeight05() {
        Rigidbody rb = new Rigidbody();
        rb.setPosition(Vector3.UP.scale(1000));
        rb.setVelocity(Vector3.UP.scale(-100));
        double time = rb.predictArrivalAtHeight(500);
        assertEquals(time, 5, 1E-6);
    }

    @Test
    public void predictArrivalAtHeight06() {
        Rigidbody rb = new Rigidbody();
        rb.setPosition(Vector3.UP.scale(500));
        rb.setVelocity(Vector3.UP.scale(100));
        double time = rb.predictArrivalAtHeight(200);
        assertTrue(Double.isNaN(time));
    }

    @Test
    public void predictArrivalAtHeight07() {
        Rigidbody rb = new Rigidbody();
        rb.setPosition(Vector3.UP.scale(200));
        rb.setAffectedByGravity(true);
        double time = rb.predictArrivalAtHeight(500);
        assertTrue(Double.isNaN(time));
    }

    @Test
    public void predictArrivalAtHeight08() {
        Rigidbody rb = new Rigidbody();
        rb.setPosition(Vector3.UP.scale(200));
        rb.setVelocity(Vector3.UP.scale(400));
        rb.setAffectedByGravity(true);
        double time = rb.predictArrivalAtHeight(500);
        assertTrue(Double.isNaN(time));
    }

    @Test
    public void predictArrivalAtHeight09() {
        Rigidbody rb = new Rigidbody();
        rb.setPosition(Vector3.UP.scale(1000));
        rb.setVelocity(Vector3.UP.scale(200));
        rb.setAffectedByGravity(true);
        double time = rb.predictArrivalAtHeight(1500);
        assertTrue(Double.isNaN(time));
    }

    @Test
    public void predictArrivalAtHeight10() {
        Rigidbody rb = new Rigidbody();
        rb.setPosition(Vector3.UP.scale(200));
        rb.setVelocity(Vector3.UP.scale(1000));
        rb.setAffectedByGravity(true);
        double time = rb.predictArrivalAtHeight(300);
        // TODO this is the answer for when the Rigidbody has reached this height ON ITS WAY DOWN, NOT UP
        assertEquals(time, 2.973442931, 1E-6);
    }

    @Test
    public void predictArrivalAtHeight11() {
        Rigidbody rb = new Rigidbody();
        rb.setPosition(Vector3.UP.scale(-500));
        rb.setVelocity(Vector3.UP.scale(2000));
        rb.setAffectedByGravity(true);
        double time = rb.predictArrivalAtHeight(0);
        // TODO this is the answer for when the Rigidbody has reached this height ON ITS WAY DOWN, NOT UP
        assertEquals(time, 5.892770034, 1E-6);
    }
}