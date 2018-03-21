package botenanna.physics;

import org.junit.Test;

import static org.junit.Assert.*;

public class TimeLineTest {

    @Test(expected = NullPointerException.class)
    public void timeline01() {
        TimeLine<String> timeLine = new TimeLine<>();
        timeLine.evaluate(0);
    }

    @Test
    public void evaluate01() {
        TimeLine<String> timeLine = new TimeLine<>();
        timeLine.addTimeStamp(0, "Hello");
        timeLine.addTimeStamp(2, "World");
        timeLine.addTimeStamp(2.3, "Foo");
        timeLine.addTimeStamp(9.34, "Bar");
        assertEquals("Hello", timeLine.evaluate(0));
        assertEquals("Hello", timeLine.evaluate(1));
        assertEquals("World", timeLine.evaluate(2));
        assertEquals("Foo", timeLine.evaluate(3));
        assertEquals("Foo", timeLine.evaluate(7));
        assertEquals("Bar", timeLine.evaluate(9.34));
        assertEquals("Bar", timeLine.evaluate(14));
    }

    @Test
    public void evaluate02() {
        TimeLine<String> timeLine = new TimeLine<>();
        timeLine.addTimeStamp(5, "Foo");
        timeLine.addTimeStamp(10, "Bar");
        assertEquals("Foo", timeLine.evaluate(0));
        assertEquals("Foo", timeLine.evaluate(7));
        assertEquals("Bar", timeLine.evaluate(15));
    }

    @Test
    public void evaluate03() {
        TimeLine<Integer> timeLine = new TimeLine<>();
        timeLine.addTimeStamp(0, -40);
        timeLine.addTimeStamp(1, -10);
        assertEquals(Integer.valueOf(-10), timeLine.evaluate(2));
        assertEquals(Integer.valueOf(-40), timeLine.evaluate(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void evaluate05() {
        TimeLine<String> timeLine = new TimeLine<>();
        timeLine.addTimeStamp(-1, "Foo");
    }

    @Test(expected = IllegalArgumentException.class)
    public void evaluate06() {
        TimeLine<String> timeLine = new TimeLine<>();
        timeLine.addTimeStamp(0, "Foo");
        timeLine.evaluate(-1);
    }

    @Test
    public void evaluateUp01() {
        TimeLine<String> timeLine = new TimeLine<>();
        timeLine.addTimeStamp(0, "Hello");
        timeLine.addTimeStamp(2, "World");
        timeLine.addTimeStamp(2.3, "Foo");
        timeLine.addTimeStamp(9.34, "Bar");
        assertEquals("World", timeLine.evaluateUp(0));
        assertEquals("World", timeLine.evaluateUp(1.9));
        assertEquals("Foo", timeLine.evaluateUp(2));
        assertEquals("Bar", timeLine.evaluateUp(3));
        assertEquals("Bar", timeLine.evaluateUp(7));
        assertEquals("Bar", timeLine.evaluateUp(14));
    }

    @Test
    public void evaluateUp02() {
        TimeLine<String> timeLine = new TimeLine<>();
        timeLine.addTimeStamp(5, "Foo");
        timeLine.addTimeStamp(10, "Bar");
        assertEquals("Foo", timeLine.evaluateUp(0));
        assertEquals("Bar", timeLine.evaluateUp(7));
        assertEquals("Bar", timeLine.evaluateUp(15));
    }

    @Test
    public void evaluateUp03() {
        TimeLine<Integer> timeLine = new TimeLine<>();
        timeLine.addTimeStamp(0, -40);
        timeLine.addTimeStamp(1, -10);
        assertEquals(Integer.valueOf(-10), timeLine.evaluateUp(2));
        assertEquals(Integer.valueOf(-10), timeLine.evaluateUp(0));
    }

    @Test(expected = NullPointerException.class)
    public void evaluateUp04() {
        TimeLine<String> timeLine = new TimeLine<>();
        timeLine.evaluateUp(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void evaluateUp06() {
        TimeLine<String> timeLine = new TimeLine<>();
        timeLine.addTimeStamp(0, "Foo");
        timeLine.evaluateUp(-1);
    }
}