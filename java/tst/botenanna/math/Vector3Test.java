package botenanna.math;

import org.junit.Test;
import rlbot.api.GameData;

import java.util.Vector;

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
    public void getAngleTo01() { //90 grader following axis
        double angle = new Vector3(0, 0, 1).getAngleTo(new Vector3(1, 0, 0));
        assertEquals(Math.PI / 2, angle, 1E-100);
    }

    @Test
    public void getAngleTo02() {//90 grader not follow axis
        double angle = new Vector3(1, 1, 0).getAngleTo(new Vector3(1, -1, 0));
        assertEquals(Math.PI / 2, angle, 1E-100);
    }

    @Test
    public void getAngleTo03() {//0 degree angle
        double angle = new Vector3(1, 1, 0).getAngleTo(new Vector3(2, 2, 0));
        assertEquals(0, angle, 1E-5);
    }

    @Test
    public void getAngleTo04() { //Small angle, not following axis
        double angle = new Vector3(5, 1, 0).getAngleTo(new Vector3(5, 2, 0));
        assertEquals(0.18311081726248412793, angle, 1E-15);
    }

    @Test
    public void getAngleTo05() {
        double angle = new Vector3(1, 0, 0).getAngleTo(new Vector3(-1, 0, 0));
        assertEquals(Math.PI, angle, 1E-20);
    }

    @Test
    public void getAngleTo06() {
        double angle = new Vector3(1, 0, 0).getAngleTo(new Vector3(-1, 1, 0));
        assertEquals(Math.PI * (double) 3 / 4, angle, 1E-20);
    }

    @Test
    public void getProjectionOnto01() {
        Vector3 vectorA = new Vector3(5, 0, 0);
        Vector3 vectorB = new Vector3(2, 1, 0);

        Vector3 projectedVector = vectorB.getProjectionOnto(vectorA);

        assertEquals(new Vector3(2, 0, 0), projectedVector);
    }

    @Test
    public void getProjectionOnto02() {
        Vector3 vectorA = new Vector3(-10, -20, -50);
        Vector3 vectorB = new Vector3(14, 20, -12);

        Vector3 projectedVector = vectorB.getProjectionOnto(vectorA);

        assertEquals(0, projectedVector.minus(new Vector3(-0.2, -0.4, -1)).getMagnitude(), 1E-6);
    }

    @Test
    public void getProjectionOnto03() {
        Vector3 vectorA = new Vector3(-10, -20, -50);
        Vector3 vectorB = new Vector3(10, 20, -10);

        Vector3 projectedVector = vectorB.getProjectionOnto(vectorA);

        assertEquals(0, projectedVector.minus(new Vector3(0, 0, 0)).getMagnitude(), 1E-6);
    }

    @Test
    public void getProjectionOnto04() {
        Vector3 vectorA = new Vector3(-10, -20, -50);
        Vector3 vectorB = new Vector3(10, 200, -10);

        Vector3 projectedVector = vectorB.getProjectionOnto(vectorA);

        assertEquals(0, projectedVector.minus(new Vector3(12, 24, 60)).getMagnitude(), 1E-6);
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

    @Test
    public void lerp01() {
        Vector3 res = Vector3.lerp(new Vector3(0, 0, 0), new Vector3(1, 2, 3), 1);
        assertEquals(new Vector3(1, 2, 3), res);
    }

    @Test
    public void lerp02() {
        Vector3 res = Vector3.lerp(new Vector3(0, 1, 2), new Vector3(10, 10, 10), 0);
        assertEquals(new Vector3(0, 1, 2), res);
    }

    @Test
    public void lerp03() {
        Vector3 res = Vector3.lerp(new Vector3(2, 0, -2), new Vector3(4, 4, 4), 0.5);
        assertEquals(new Vector3(3, 2, 1), res);
    }

    @Test
    public void lerpTo01() {
        Vector3 firstVector = new Vector3(0, 0, 0);
        Vector3 secondVector = new Vector3(1, 2, 3);

        assertEquals(new Vector3(1, 2, 3), firstVector.lerpTo(secondVector, 1));
    }

    @Test
    public void lerpTo02() {
        Vector3 firstVector = new Vector3(0, 1, 2);
        Vector3 secondVector = new Vector3(10, 10, 10);

        assertEquals(new Vector3(0, 1, 2), firstVector.lerpTo(secondVector, 0));
    }

    @Test
    public void lerpTo03() {
        Vector3 firstVector = new Vector3(2, 0, -2);
        Vector3 secondVector = new Vector3(4, 4, 4);

        assertEquals(new Vector3(3, 2, 1), firstVector.lerpTo(secondVector, 0.5));
    }

    @Test
    public void asVector201() {
        Vector3 testVector = new Vector3(1, 2, 3);
        Vector2 expectedVector = new Vector2(1, 2);

        assertEquals(expectedVector, testVector.asVector2());
    }

    @Test
    public void plus01() {
        Vector3 testVector = new Vector3(1, 2, 3);
        Vector3 otherVector = new Vector3(4, 5, 6);

        Vector3 expectedVector = new Vector3(5, 7, 9);

        assertEquals(expectedVector, testVector.plus(otherVector));
    }

    @Test
    public void getDistanceToSqr01() {
        Vector3 testVector = new Vector3(1, 2, 3);
        Vector3 otherVector = new Vector3(4, 5, 6);

        assertEquals(27, testVector.getDistanceToSqr(otherVector), 1E-8);
    }

    @Test
    public void getDistanceTo01() {
        Vector3 testVector = new Vector3(1, 2, 3);
        Vector3 otherVector = new Vector3(4, 5, 6);

        assertEquals(5.19615242271, testVector.getDistanceTo(otherVector), 1E-8);
    }

    @Test
    public void hashCode01() {
        Vector3 testVector = new Vector3(1, 2, 3);
        Vector3 otherVector = new Vector3(1, 2, 3);

        assertTrue(testVector.hashCode() == otherVector.hashCode());
    }

    @Test
    public void hashCode02() {
        Vector3 testVector = new Vector3(1, 2, 3);
        Vector3 otherVector = new Vector3(5, 5, 5);

        assertFalse(testVector.hashCode() == otherVector.hashCode());
    }

    @Test
    public void toString01() {
        Vector3 firstVector = new Vector3(1500, 3000, 2500);
        String expectedString = "Vec3(1500.0, 3000.0, 2500.0)";

        assertEquals(expectedString, firstVector.toString());
    }

    @Test
    public void toStringFixedSize01 () {
        Vector3 firstVector = new Vector3(1500, 3000, 2500);
        String expectedString = "( 1500.00,  3000.00,  2500.00)";

        assertEquals(expectedString, firstVector.toStringFixedSize());
    }

    @Test
    public void constructor01 () {
        Vector3 actualVector = new Vector3(1,2);
        Vector3 expectedVector = new Vector3(1,2,0);

        assertEquals(expectedVector,actualVector);
    }


}