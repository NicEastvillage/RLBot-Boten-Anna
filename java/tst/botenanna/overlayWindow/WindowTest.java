package botenanna.overlayWindow;

import botenanna.math.Vector3;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class WindowTest {

    static Window statusWindow = null;

    @BeforeClass
    public static void beforeAll(){
        statusWindow = new Window();
    }

    @Test
    public void updateLabelCarLocation01(){
        Vector3 carLocation = new Vector3(1.1231231516,2.124512398123,3.12512984375 );
        statusWindow.updateLabelCarLocation(carLocation);
    }

    @Test
    public void updateLabelCarVelocity01(){
        Vector3 carVelocity = new Vector3(1.1231231516,2.124512398123,3.12512984375 );
        statusWindow.updateLabelCarLocation(carVelocity);
    }

    @Test
    public void updateLabelCarRotation01(){
        Vector3 carRotation = new Vector3(1.1231231516,2.124512398123,3.12512984375 );
        statusWindow.updateLabelCarLocation(carRotation);
    }

    @Test
    public void updateLabelBallLocation01(){
        Vector3 ballLocation = new Vector3(1.1231231516,2.124512398123,3.12512984375 );
        statusWindow.updateLabelCarLocation(ballLocation);
    }

    @Test
    public void updateLabelBallVelocity01(){
        Vector3 ballVelocity = new Vector3(1.1231231516,2.124512398123,3.12512984375 );
        statusWindow.updateLabelCarLocation(ballVelocity);
    }
}