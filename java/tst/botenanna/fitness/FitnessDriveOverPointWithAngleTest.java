package botenanna.fitness;

import botenanna.math.Vector3;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FitnessDriveOverPointWithAngleTest {

    @Test
    public void calculateFitnessValue01(){

        Vector3 destinationPoint = new Vector3(1000,1000,0);
        Vector3 nextPoint = new Vector3(750,1250,0);

        FitnessDriveOverPointWithAngle fitness = new FitnessDriveOverPointWithAngle(destinationPoint, nextPoint, 0.2,0.2);

        Vector3 myDirection = new Vector3(10,0,0);
        Vector3 myPos = new Vector3(0,0,0);

        double fitnessValue = fitness.calculateFitnessValue(myPos, myDirection,10);

        System.out.println(fitnessValue);

        assertEquals(1.2040179327137668225e-11, fitnessValue,1E-23);

    }

    @Test
    public void calculateFitnessValue02(){

        Vector3 destinationPoint = new Vector3(-100,-100,0);
        Vector3 nextPoint = new Vector3(750,1250,0);

        FitnessDriveOverPointWithAngle fitness = new FitnessDriveOverPointWithAngle(destinationPoint, nextPoint, 0.2,0.2);

        Vector3 myDirection = new Vector3(10,0,0);
        Vector3 myPos = new Vector3(20,20,0);

        double fitnessValue = fitness.calculateFitnessValue(myPos, myDirection,4);

        System.out.println(fitnessValue);

        assertEquals(0.000073714376962337143437, fitnessValue,1E-15);
    }

    @Test
    public void calculateFitnessValueComparison01(){

        Vector3 destinationPoint1 = new Vector3(-100,-100,0);
        Vector3 nextPoint1 = new Vector3(750,1250,0);

        FitnessDriveOverPointWithAngle fitness1 = new FitnessDriveOverPointWithAngle(destinationPoint1, nextPoint1, 0.2,0.2);

        Vector3 myDirection1 = new Vector3(10,0,0);
        Vector3 myPos1 = new Vector3(20,20,0);

        double fitnessValue1 = fitness1.calculateFitnessValue(myPos1, myDirection1,4);

        Vector3 destinationPoint2 = new Vector3(-100,-100,0);
        Vector3 nextPoint2 = new Vector3(750,1250,0);

        FitnessDriveOverPointWithAngle fitness2 = new FitnessDriveOverPointWithAngle(destinationPoint2, nextPoint2, 0.2,0.2);

        Vector3 myDirection2 = new Vector3(10,0,0);
        Vector3 myPos2 = new Vector3(0,0,0);

        double fitnessValue2 = fitness2.calculateFitnessValue(myPos2, myDirection2,3);

        assertTrue(fitnessValue1 < fitnessValue2);
    }
}
