package botenanna.math.zone;

import botenanna.math.Vector3;
import org.junit.Test;

import static org.junit.Assert.*;

public class BoxTest {
    @Test
    public void isPointInBoxArea01 () {
        Box testBox = new Box(new Vector3(-720 , 5200 , 0), new Vector3(720 , 4000 , 1000));
        Vector3 testPoint = new Vector3(38.89, 4577.51, 500);

        assertTrue(testBox.isPointInBoxArea(testPoint));
    }

    @Test
    public void isPointInBoxArea02 () {
        Box testBox = new Box(new Vector3(-720 , 5200 , 0), new Vector3(720 , 4000 , 1000));
        Vector3 testPoint = new Vector3(38.89, 4577.51, 1500);

        assertFalse(testBox.isPointInBoxArea(testPoint));
    }
}
