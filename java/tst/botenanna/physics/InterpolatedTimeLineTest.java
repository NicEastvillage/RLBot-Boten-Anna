package botenanna.physics;

import botenanna.math.RLMath;
import org.junit.Test;

import static org.junit.Assert.*;

public class InterpolatedTimeLineTest {

    @Test
    public void evaluate01() {
        InterpolatedTimeLine<Double> line = new InterpolatedTimeLine<>(RLMath::lerp);
        line.addTimeStep(1, 100d);
        line.addTimeStep(2, 300d);
        assertEquals(100d, line.evaluate(1), 1E-8);
        assertEquals(200d, line.evaluate(1.5), 1E-8);
        assertEquals(300d, line.evaluate(2), 1E-8);
    }

    @Test
    public void evaluate02() {
        InterpolatedTimeLine<Double> line = new InterpolatedTimeLine<>(RLMath::lerp);
        line.addTimeStep(1, 100d);
        line.addTimeStep(2, 200d);
        assertEquals(-100d, line.evaluate(-1), 1E-8);
        assertEquals(0d, line.evaluate(0), 1E-8);
        assertEquals(500d, line.evaluate(5), 1E-8);
    }

    @Test
    public void evaluate03() {
        InterpolatedTimeLine<Double> line = new InterpolatedTimeLine<>(RLMath::lerp);
        line.addTimeStep(1, 100d);
        assertEquals(100d, line.evaluate(0), 1E-8);
        assertEquals(100d, line.evaluate(1), 1E-8);
        assertEquals(100d, line.evaluate(5), 1E-8);
    }

    @Test
    public void evaluate04() {
        InterpolatedTimeLine<Double> line = new InterpolatedTimeLine<>(RLMath::lerp);
        line.addTimeStep(1, 100d);
        line.addTimeStep(10, 100d);
        assertEquals(100d, line.evaluate(0), 1E-8);
        assertEquals(100d, line.evaluate(10), 1E-8);
        assertEquals(100d, line.evaluate(15), 1E-8);
    }

    @Test(expected = NullPointerException.class)
    public void evaluate05() {
        InterpolatedTimeLine<Double> line = new InterpolatedTimeLine<>(RLMath::lerp);
        line.evaluate(10);
    }

    @Test
    public void isEmpty01() {
        InterpolatedTimeLine<Double> line = new InterpolatedTimeLine<>(RLMath::lerp);
        assertEquals(true, line.isEmpty());
        line.addTimeStep(1, 50d);
        assertEquals(false, line.isEmpty());
    }
}