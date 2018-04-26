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
}