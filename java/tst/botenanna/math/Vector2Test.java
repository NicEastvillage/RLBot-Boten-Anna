package botenanna.math;


import org.junit.Test;

import static org.junit.Assert.*;

public class Vector2Test {

    @Test
    public void constructor01() {
        Vector2 testVector = new Vector2();

        assertTrue(testVector.isZero());
    }

    @Test
    public void constructor02() {
        Vector3 testVector3 = new Vector3(100, 200, 300);
        Vector2 convertedVector = new Vector2(testVector3);

        Vector2 actualVector2 = new Vector2(100, 200);

        assertEquals(actualVector2, convertedVector);
    }

    @Test
    public void turn01() {
        double inputRadian = Math.PI / 30;
        Vector2 direction = new Vector2(-123, 450);
        Vector2 test = new Vector2(-169.36, 434.6778519);

        Vector2 turned = direction.turn(inputRadian);
        assertEquals(0, turned.minus(test).getMagnitude(), 1E-2);
    }

    @Test
    public void turn02() {
        Vector2 direction = new Vector2(8, 25);
        Vector2 test = new Vector2(-8, -25);
        double inputRadian = Math.PI;

        assertEquals(0, direction.turn(inputRadian).minus(test).getMagnitude(), 1E-8);
    }

    @Test
    public void plus01() {
        Vector2 firstVector = new Vector2(-2500, 3500);
        Vector2 secondVector = new Vector2(0, 800);

        assertEquals(24740000, firstVector.plus(secondVector).getMagnitudeSqr(), 1E-8);
    }

    @Test
    public void dot01() {
        Vector2 firstVector = new Vector2(10, 10);
        Vector2 secondVector = new Vector2(20, 20);

        assertEquals(400, firstVector.dot(secondVector), 1E-8);
    }

    @Test
    public void scale01() {
        Vector2 testVector = new Vector2(10, 10);

        assertEquals(20000, testVector.scale(10).getMagnitudeSqr(), 1E-8);
    }

    @Test
    public void scale02() {
        Vector2 testVector = new Vector2(10, 10);

        assertEquals(20000, testVector.scale(-10).getMagnitudeSqr(), 1E-8);
    }

    @Test
    public void getDistanceToSqr01() {
        Vector2 firstVector = new Vector2(10, 10);
        Vector2 secondVector = new Vector2(20, 20);

        assertEquals(200, firstVector.getDistanceToSqr(secondVector), 1E-8);
    }

    @Test
    public void getDistanceTo01() {
        Vector2 firstVector = new Vector2(10, 10);
        Vector2 secondVector = new Vector2(20, 20);

        assertEquals(14.1421356237, firstVector.getDistanceTo(secondVector), 1E-8);
    }

    @Test
    public void isZero01() {
        Vector2 firstVector = new Vector2(0, 0);

        assertTrue(firstVector.isZero());
    }

    @Test
    public void isZero02() {
        Vector2 firstVector = new Vector2(2150, 3520);

        assertFalse(firstVector.isZero());
    }

    @Test
    public void equals01() {
        Vector2 firstVector = new Vector2(2150, 3520);
        Vector2 secondVector = new Vector2(2150, 3520);

        assertTrue(firstVector.equals(secondVector));
    }

    @Test
    public void equals02() {
        Vector2 firstVector = new Vector2(2150, 3520);
        String testString = "Testing";

        assertFalse(firstVector.equals(testString));
    }

    @Test
    public void hashCode01() {
        Vector2 firstVector = new Vector2(2150, 3520);
        Vector2 secondVector = new Vector2(2150, 3520);

        assertTrue(firstVector.hashCode() == secondVector.hashCode());
    }

    @Test
    public void hashCode02() {
        Vector2 firstVector = new Vector2(2150, 3520);
        Vector2 secondVector = new Vector2(1500, 2503);

        assertFalse(firstVector.hashCode() == secondVector.hashCode());
    }

    @Test
    public void toString01() {
        Vector2 firstVector = new Vector2(1500, 3000);
        String actualString = firstVector.toString();

        String expectedString = "Vec2(1500.0, 3000.0)";

        assertEquals(expectedString, actualString);
    }

    @Test
    public void asVector301() {
        Vector3 actualVector = new Vector3(0, 0, 0);
        Vector2 testVector = new Vector2(0, 0);

        assertEquals(actualVector, testVector.asVector3());
    }

    @Test
    public void getNormalized01() {
        Vector2 testVector = new Vector2(100, 200);

        assertEquals(1.0, testVector.getNormalized().getMagnitudeSqr(), 1E-8);
    }

    @Test
    public void lerp01() {
        Vector2 testVector = new Vector2(10, 20);
        Vector2 otherVector = new Vector2(50, 60);

        Vector2 expectedVector = new Vector2(90, 100);

        assertEquals(expectedVector, testVector.lerp(otherVector, 2));
    }

    @Test
    public void lerp02() {
        Vector2 testVector = new Vector2(10, 20);
        Vector2 otherVector = new Vector2(50, 60);

        Vector2 expectedVector = new Vector2(-190, -180);

        assertEquals(expectedVector, testVector.lerp(otherVector, -5));
    }

}
