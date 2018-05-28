package botenanna.intentions;

import botenanna.math.Vector3;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class IntentionFunctionDriveOverPointWithAngleTest {


    @Test
    public void calculateWork01(){ //stopOnPoint = true

        Vector3 destinationPoint = new Vector3(500,500,0);
        Vector3 nextPoint = new Vector3(750,1250,0);
        Vector3 velocity = new Vector3(500,500,0);


        IntentionFunctionDriveOverPointWithAngle intentionFunction = new IntentionFunctionDriveOverPointWithAngle(s -> destinationPoint, s -> nextPoint, 0.2,0.2, true);

        Vector3 myDirection = new Vector3(10,0,0);
        Vector3 myPos = new Vector3(0,0,0);
        int timeSpent = 10;

        double workValue = intentionFunction.calculateWork(destinationPoint, nextPoint, myPos, myDirection,timeSpent, velocity);

        assertEquals(12.052511093524665322, workValue,1E-14);

    }

    @Test
    public void calculateWork02(){ //stopOnPoint = true

        Vector3 destinationPoint = new Vector3(-100,-100,0);
        Vector3 nextPoint = new Vector3(750,1250,0);
        Vector3 velocity = new Vector3(1400,0,0);

        IntentionFunctionDriveOverPointWithAngle intentionFunction = new IntentionFunctionDriveOverPointWithAngle(s -> destinationPoint, s -> nextPoint, 0.2,0.2, true);

        Vector3 myDirection = new Vector3(10,0,0);
        Vector3 myPos = new Vector3(20,20,0);
        int timeSpent = 4;

        double workValue = intentionFunction.calculateWork(destinationPoint, nextPoint, myPos, myDirection,timeSpent, velocity);

        assertEquals(5.3183387840641124401, workValue,1E-14);
    }

    @Test
    public void calculateWork03(){ //stopOnPoint = false

        Vector3 destinationPoint = new Vector3(500,500,0);
        Vector3 nextPoint = new Vector3(750,1250,0);
        Vector3 velocity = new Vector3(500,500,0);


        IntentionFunctionDriveOverPointWithAngle intentionFunction = new IntentionFunctionDriveOverPointWithAngle(s -> destinationPoint, s -> nextPoint, 0.2,0.2, false);

        Vector3 myDirection = new Vector3(10,0,0);
        Vector3 myPos = new Vector3(0,0,0);
        int timeSpent = 10;

        double workValue = intentionFunction.calculateWork(destinationPoint, nextPoint, myPos, myDirection, timeSpent, velocity);

        assertEquals(11.109702051942601956, workValue,1E-14);

    }

    @Test
    public void calculateWork04(){ //stopOnPoint = false

        Vector3 destinationPoint = new Vector3(-100,-100,0);
        Vector3 nextPoint = new Vector3(750,1250,0);
        Vector3 velocity = new Vector3(1400,0,0);

        IntentionFunctionDriveOverPointWithAngle intentionFunction = new IntentionFunctionDriveOverPointWithAngle(s -> destinationPoint, s -> nextPoint, 0.2,0.2, false);

        Vector3 myDirection = new Vector3(10,0,0);
        Vector3 myPos = new Vector3(20,20,0);
        int timeSpent = 4;

        double workValue = intentionFunction.calculateWork(destinationPoint, nextPoint, myPos, myDirection, timeSpent, velocity);

        assertEquals(3.4516721173974457734, workValue,1E-14);
    }

    @Test
    public void calculateWorkValueComparison01(){

        Vector3 destinationPoint1 = new Vector3(-100,-100,0);
        Vector3 nextPoint1 = new Vector3(750,1250,0);
        Vector3 myCarVelocity1 = new Vector3(1400,0,0);

        IntentionFunctionDriveOverPointWithAngle intentionFunction1 = new IntentionFunctionDriveOverPointWithAngle(s -> destinationPoint1, s -> nextPoint1, 0.2,0.2, true);

        Vector3 myDirection1 = new Vector3(10,0,0);
        Vector3 myPos1 = new Vector3(20,20,0);
        int timeSpent1 = 4;

        double workValue1 = intentionFunction1.calculateWork(destinationPoint1, nextPoint1, myPos1, myDirection1, timeSpent1, myCarVelocity1);

        Vector3 destinationPoint2 = new Vector3(-100,-100,0);
        Vector3 nextPoint2 = new Vector3(750,1250,0);

        IntentionFunctionDriveOverPointWithAngle intentionFunction2 = new IntentionFunctionDriveOverPointWithAngle(s -> destinationPoint2, s -> nextPoint2, 0.2,0.2, true);

        Vector3 myDirection2 = new Vector3(10,0,0);
        Vector3 myPos2 = new Vector3(0,0,0);
        Vector3 myCarVelocity2 = new Vector3(500,0,0);
        int timeSpent2 = 3;

        double workValue2 = intentionFunction2.calculateWork(destinationPoint2, nextPoint2, myPos2, myDirection2, timeSpent2, myCarVelocity2);

        assertTrue(workValue1 > workValue2);
    }
}