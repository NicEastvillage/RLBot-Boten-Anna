package botenanna.math;

import org.junit.Test;

import static org.junit.Assert.*;

public class RLMathTest {

    @Test
    public void lerp01() {
        double res = RLMath.lerp(0, 10, 0);
        assertEquals(0, res, 1E-8);
    }

    @Test
    public void lerp02() {
        double res = RLMath.lerp(1, 4, 1);
        assertEquals(4, res, 1E-8);
    }

    @Test
    public void lerp03() {
        double res = RLMath.lerp(4, 8, 0.5);
        assertEquals(6, res, 1E-8);
    }

    @Test
    public void lerp04() {
        double res = RLMath.lerp(0, 2, -1);
        assertEquals(-2, res, 1E-8);
    }

    @Test
    public void lerp05() {
        double res = RLMath.lerp(10, 12, 2);
        assertEquals(14, res, 1E-8);
    }

    @Test
    public void lerp06() {
        double res = RLMath.lerp(5, 0, 0);
        assertEquals(5, res, 1E-8);
    }

    @Test
    public void carsAngleToPoint01() {
        Vector2 testCarPosition = new Vector2(-2353, 5923);
        Vector2 testPointPosition = new Vector2(0, 0);
        double testCarYaw = 1;

        double expectedAngDiff = -2.1926500163942313;

        assertEquals(expectedAngDiff, RLMath.carsAngleToPoint(testCarPosition, testCarYaw, testPointPosition), 1E-8);
    }

    @Test
    public void carUpVector01() {
        Vector3 testCarRotation = new Vector3(0.6687197685241699, 0.0607839897274971, -0.5040085911750793);
        Vector3 actualUpVector = new Vector3(-0.2576777829967365, 0.5659077511608301, 0.7831670175166846);

        assertEquals(actualUpVector, RLMath.carUpVector(testCarRotation));
    }

    @Test
    public void carUpVector02() {
        Vector3 testCarRotation = new Vector3(3.1343061923980713, 0.0019174759509041905, 1.6191166639328003);
        Vector3 actualUpVector = new Vector3(0.007370506548906733, 0.0015632417044240848, -0.9999716155514546);

        assertEquals(actualUpVector, RLMath.carUpVector(testCarRotation));
    }

    @Test
    public void carFrontVector01() {
        Vector3 testCarRotation = new Vector3(3.1343061923980713, 0.0019174759509041905, 1.6191166639328003);
        Vector3 actualUpVector = new Vector3(-0.04830144704087104, 0.9988309634288686, 0.0019174747759026205);

        assertEquals(actualUpVector, RLMath.carFrontVector(testCarRotation));
    }

    @Test
    public void carFrontVector02() {
        Vector3 testCarRotation = new Vector3(0.0, 0.0, 1.6191166639328003);
        Vector3 actualUpVector = new Vector3(-0.04830153583631089, 0.9988327996395862, 0.0);

        assertEquals(actualUpVector, RLMath.carFrontVector(testCarRotation));
    }

    @Test
    public void carSideVector01() {
        Vector3 testCarRotation = new Vector3(0.0, 0.0, 1.6191166639328003);
        Vector3 actualUpVector = new Vector3(-0.9988327996395862, -0.04830153583631089, 0.0);

        assertEquals(actualUpVector, RLMath.carSideVector(testCarRotation));
    }

    @Test
    public void carSideVector02() {
        Vector3 testCarRotation = new Vector3(0.0, 0.0, 0.0);
        Vector3 actualUpVector = new Vector3(0, 1, 0);

        assertEquals(actualUpVector, RLMath.carSideVector(testCarRotation));
    }


}