package botenanna.intentions;

import botenanna.math.Vector3;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FitnessDriveOverPointWithAngleTest {


    @Test
    public void calculateFitnessValue01(){ //stopOnPoint = true

        Vector3 destinationPoint = new Vector3(500,500,0);
        Vector3 nextPoint = new Vector3(750,1250,0);
        Vector3 velocity = new Vector3(500,500,0);


        IntentionFunctionDriveOverPointWithAngle fitness = new IntentionFunctionDriveOverPointWithAngle(s -> destinationPoint, s -> nextPoint, 0.2,0.2, true);

        Vector3 myDirection = new Vector3(10,0,0);
        Vector3 myPos = new Vector3(0,0,0);
        int timeSpent = 10;

        double fitnessValue = fitness.calculateWork(destinationPoint, nextPoint, myPos, myDirection,timeSpent, velocity);

        assertEquals(-58.329620035741783357, fitnessValue,1E-14);

    }

    @Test
    public void calculateFitnessValue02(){ //stopOnPoint = true

        Vector3 destinationPoint = new Vector3(-100,-100,0);
        Vector3 nextPoint = new Vector3(750,1250,0);
        Vector3 velocity = new Vector3(1400,0,0);

        IntentionFunctionDriveOverPointWithAngle fitness = new IntentionFunctionDriveOverPointWithAngle(s -> destinationPoint, s -> nextPoint, 0.2,0.2, true);

        Vector3 myDirection = new Vector3(10,0,0);
        Vector3 myPos = new Vector3(20,20,0);
        int timeSpent = 4;

        double fitnessValue = fitness.calculateWork(destinationPoint, nextPoint, myPos, myDirection,timeSpent, velocity);

        assertEquals(-15.632299441756103443, fitnessValue,1E-14);
    }

    @Test
    public void calculateFitnessValue03(){ //stopOnPoint = false

        Vector3 destinationPoint = new Vector3(500,500,0);
        Vector3 nextPoint = new Vector3(750,1250,0);
        Vector3 velocity = new Vector3(500,500,0);


        IntentionFunctionDriveOverPointWithAngle fitness = new IntentionFunctionDriveOverPointWithAngle(s -> destinationPoint, s -> nextPoint, 0.2,0.2, false);

        Vector3 myDirection = new Vector3(10,0,0);
        Vector3 myPos = new Vector3(0,0,0);
        int timeSpent = 10;

        double fitnessValue = fitness.calculateWork(destinationPoint, nextPoint, myPos, myDirection, timeSpent, velocity);

        assertEquals(58.329620035741783357, fitnessValue,1E-14);

    }

    @Test
    public void calculateFitnessValue04(){ //stopOnPoint = false

        Vector3 destinationPoint = new Vector3(-100,-100,0);
        Vector3 nextPoint = new Vector3(750,1250,0);
        Vector3 velocity = new Vector3(1400,0,0);

        IntentionFunctionDriveOverPointWithAngle fitness = new IntentionFunctionDriveOverPointWithAngle(s -> destinationPoint, s -> nextPoint, 0.2,0.2, false);

        Vector3 myDirection = new Vector3(10,0,0);
        Vector3 myPos = new Vector3(20,20,0);
        int timeSpent = 4;

        double fitnessValue = fitness.calculateWork(destinationPoint, nextPoint, myPos, myDirection, timeSpent, velocity);

        assertEquals(15.632299441756103443, fitnessValue,1E-14);
    }

    @Test
    public void calculateFitnessValueComparison01(){

        Vector3 destinationPoint1 = new Vector3(-100,-100,0);
        Vector3 nextPoint1 = new Vector3(750,1250,0);
        Vector3 myCarVelocity1 = new Vector3(1400,0,0);

        IntentionFunctionDriveOverPointWithAngle fitness1 = new IntentionFunctionDriveOverPointWithAngle(s -> destinationPoint1, s -> nextPoint1, 0.2,0.2, true);

        Vector3 myDirection1 = new Vector3(10,0,0);
        Vector3 myPos1 = new Vector3(20,20,0);
        int timeSpent1 = 4;

        double fitnessValue1 = fitness1.calculateWork(destinationPoint1, nextPoint1, myPos1, myDirection1, timeSpent1, myCarVelocity1);

        Vector3 destinationPoint2 = new Vector3(-100,-100,0);
        Vector3 nextPoint2 = new Vector3(750,1250,0);

        IntentionFunctionDriveOverPointWithAngle fitness2 = new IntentionFunctionDriveOverPointWithAngle(s -> destinationPoint2, s -> nextPoint2, 0.2,0.2, true);

        Vector3 myDirection2 = new Vector3(10,0,0);
        Vector3 myPos2 = new Vector3(0,0,0);
        Vector3 myCarVelocity2 = new Vector3(500,0,0);
        int timeSpent2 = 3;

        double fitnessValue2 = fitness2.calculateWork(destinationPoint2, nextPoint2, myPos2, myDirection2, timeSpent2, myCarVelocity2);

        assertTrue(fitnessValue1 > fitnessValue2);
    }


    //TODO DEVIATION
}