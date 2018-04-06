package botenanna.math;

import org.junit.Test;

import static org.junit.Assert.*;

public class Vector3Test {

    @Test
    public void dot01() {
        double dot = new Vector3(2, 5, 9).dot(new Vector3(5, 1, -2));
        assertEquals(dot, -3, 1E-6);
    }

    @Test
    public void dot02() {
        double dot = new Vector3(-3, 4, 100).dot(new Vector3(50, 6, 0));
        assertEquals(dot, -126, 1E-6);
    }

    @Test
    public void cross01() {
        Vector3 cross = new Vector3(2, 3, 4).cross(new Vector3(5, 6, 7));
        assertTrue(cross.equals(new Vector3(-3, 6, -3)));
    }

    @Test
    public void cross02() {
        Vector3 cross = new Vector3(1, 0, 0).cross(new Vector3(0, 1, 0));
        assertTrue(cross.equals(new Vector3(0, 0, 1)));
    }

    @Test
    public void getMagnitudeSqr01() {
        double len = new Vector3(3, 4, 0).getMagnitudeSqr();
        assertEquals(len, 25, 1E-6);
    }

    @Test
    public void getMagnitudeSqr02() {
        double len = new Vector3(-1, -1, -1).getMagnitudeSqr();
        assertEquals(len, 3, 1E-5);
    }

    @Test
    public void getMagnitudeSqr03() {
        double len = new Vector3(1, 2, 3).getMagnitudeSqr();
        assertEquals(len, 14, 1E-5);
    }

    @Test
    public void getAngleTo01(){
        double angle = new Vector3(0,0,1).getAngleTo(new Vector3(1,0,0));
        assertEquals(angle, Math.PI/2, 1E-100);
    }

    @Test
    public void getNormalized01() {
        Vector3 normal = new Vector3(100, 0, 0).getNormalized();
        assertTrue(normal.equals(new Vector3(1, 0, 0)));
    }

    @Test
    public void getNormalized02() {
        Vector3 normal = new Vector3(0, 0, 0).getNormalized();
        assertTrue(normal.equals(new Vector3()));
    }

    @Test
    public void getNormalized03() {
        Vector3 normal = new Vector3(0, 5, 5).getNormalized();
        // assertTrue(normal.equals(new Vector3(0, Math.sin(Math.PI * 0.25), Math.sin(Math.PI * 0.25))));
        // There might be rounding errors, so we check if the difference is pretty much the same
        assertEquals(normal.minus(new Vector3(0, Math.sin(Math.PI * 0.25), Math.sin(Math.PI * 0.25))).getMagnitude(), 0, 1E-6);
    }

    @Test
    public void equals01() {
        assertTrue(new Vector3(1, 2, 3).equals(new Vector3(1, 2, 3)));
    }

    @Test
    public void equals02() {
        assertTrue(new Vector3(-1, -2, -3).equals(new Vector3(-1, -2, -3)));
    }

    @Test
    public void equals03() {
        assertTrue(new Vector3(6, 0, 1).equals(new Vector3(6, 0, 1)));
    }
}