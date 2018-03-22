package botenanna.physics;

import org.junit.Test;

import static org.junit.Assert.*;

public class SteppedTimeLineTest {

    @Test(expected = NullPointerException.class)
    public void timeline01() {
        SteppedTimeLine<String> steppedTimeLine = new SteppedTimeLine<>();
        steppedTimeLine.evaluate(0);
    }

    @Test
    public void evaluate01() {
        SteppedTimeLine<String> steppedTimeLine = new SteppedTimeLine<>();
        steppedTimeLine.addTimeStep(0, "Hello");
        steppedTimeLine.addTimeStep(2, "World");
        steppedTimeLine.addTimeStep(2.3, "Foo");
        steppedTimeLine.addTimeStep(9.34, "Bar");
        assertEquals("Hello", steppedTimeLine.evaluate(0));
        assertEquals("Hello", steppedTimeLine.evaluate(1));
        assertEquals("World", steppedTimeLine.evaluate(2));
        assertEquals("Foo", steppedTimeLine.evaluate(3));
        assertEquals("Foo", steppedTimeLine.evaluate(7));
        assertEquals("Bar", steppedTimeLine.evaluate(9.34));
        assertEquals("Bar", steppedTimeLine.evaluate(14));
    }

    @Test
    public void evaluate02() {
        SteppedTimeLine<String> steppedTimeLine = new SteppedTimeLine<>();
        steppedTimeLine.addTimeStep(5, "Foo");
        steppedTimeLine.addTimeStep(10, "Bar");
        assertEquals("Foo", steppedTimeLine.evaluate(0));
        assertEquals("Foo", steppedTimeLine.evaluate(7));
        assertEquals("Bar", steppedTimeLine.evaluate(15));
    }

    @Test
    public void evaluate03() {
        SteppedTimeLine<Integer> steppedTimeLine = new SteppedTimeLine<>();
        steppedTimeLine.addTimeStep(0, -40);
        steppedTimeLine.addTimeStep(1, -10);
        assertEquals(Integer.valueOf(-10), steppedTimeLine.evaluate(2));
        assertEquals(Integer.valueOf(-40), steppedTimeLine.evaluate(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void evaluate05() {
        SteppedTimeLine<String> steppedTimeLine = new SteppedTimeLine<>();
        steppedTimeLine.addTimeStep(-1, "Foo");
    }

    @Test(expected = IllegalArgumentException.class)
    public void evaluate06() {
        SteppedTimeLine<String> steppedTimeLine = new SteppedTimeLine<>();
        steppedTimeLine.addTimeStep(0, "Foo");
        steppedTimeLine.evaluate(-1);
    }

    @Test
    public void evaluateUp01() {
        SteppedTimeLine<String> steppedTimeLine = new SteppedTimeLine<>();
        steppedTimeLine.addTimeStep(0, "Hello");
        steppedTimeLine.addTimeStep(2, "World");
        steppedTimeLine.addTimeStep(2.3, "Foo");
        steppedTimeLine.addTimeStep(9.34, "Bar");
        assertEquals("World", steppedTimeLine.evaluateUp(0));
        assertEquals("World", steppedTimeLine.evaluateUp(1.9));
        assertEquals("Foo", steppedTimeLine.evaluateUp(2));
        assertEquals("Bar", steppedTimeLine.evaluateUp(3));
        assertEquals("Bar", steppedTimeLine.evaluateUp(7));
        assertEquals("Bar", steppedTimeLine.evaluateUp(14));
    }

    @Test
    public void evaluateUp02() {
        SteppedTimeLine<String> steppedTimeLine = new SteppedTimeLine<>();
        steppedTimeLine.addTimeStep(5, "Foo");
        steppedTimeLine.addTimeStep(10, "Bar");
        assertEquals("Foo", steppedTimeLine.evaluateUp(0));
        assertEquals("Bar", steppedTimeLine.evaluateUp(7));
        assertEquals("Bar", steppedTimeLine.evaluateUp(15));
    }

    @Test
    public void evaluateUp03() {
        SteppedTimeLine<Integer> steppedTimeLine = new SteppedTimeLine<>();
        steppedTimeLine.addTimeStep(0, -40);
        steppedTimeLine.addTimeStep(1, -10);
        assertEquals(Integer.valueOf(-10), steppedTimeLine.evaluateUp(2));
        assertEquals(Integer.valueOf(-10), steppedTimeLine.evaluateUp(0));
    }

    @Test(expected = NullPointerException.class)
    public void evaluateUp04() {
        SteppedTimeLine<String> steppedTimeLine = new SteppedTimeLine<>();
        steppedTimeLine.evaluateUp(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void evaluateUp06() {
        SteppedTimeLine<String> steppedTimeLine = new SteppedTimeLine<>();
        steppedTimeLine.addTimeStep(0, "Foo");
        steppedTimeLine.evaluateUp(-1);
    }
}