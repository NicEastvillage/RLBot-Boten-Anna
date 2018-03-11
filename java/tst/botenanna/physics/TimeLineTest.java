package botenanna.physics;

import org.junit.Test;

import static org.junit.Assert.*;

public class TimeLineTest {

    @Test
    public void timeline01() {
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
    public void timeline02() {
        TimeLine<String> timeLine = new TimeLine<>();
        timeLine.addTimeStamp(5, "Foo");
        timeLine.addTimeStamp(10, "Bar");
        assertEquals("Foo", timeLine.evaluate(0));
        assertEquals("Foo", timeLine.evaluate(7));
        assertEquals("Bar", timeLine.evaluate(15));
    }

    @Test
    public void timeline03() {
        TimeLine<Integer> timeLine = new TimeLine<>();
        timeLine.addTimeStamp(0, -40);
        timeLine.addTimeStamp(1, -10);
        assertEquals(Integer.valueOf(-10), timeLine.evaluate(2));
        assertEquals(Integer.valueOf(-40), timeLine.evaluate(0));
    }

    @Test(expected = NullPointerException.class)
    public void timeline04() {
        TimeLine<String> timeLine = new TimeLine<>();
        timeLine.evaluate(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void timeline05() {
        TimeLine<String> timeLine = new TimeLine<>();
        timeLine.addTimeStamp(-1, "Foo");
    }

    @Test(expected = IllegalArgumentException.class)
    public void timeline06() {
        TimeLine<String> timeLine = new TimeLine<>();
        timeLine.addTimeStamp(0, "Foo");
        timeLine.evaluate(-1);
    }
}