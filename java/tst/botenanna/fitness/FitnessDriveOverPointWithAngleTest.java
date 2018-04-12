package botenanna.fitness;

import botenanna.math.Vector3;
import botenanna.physics.Path;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FitnessDriveOverPointWithAngleTest {


    @Test
    public void calculateFitnessValue01(){ //stopOnPoint = true

        Vector3 destinationPoint = new Vector3(500,500,0);
        Vector3 nextPoint = new Vector3(750,1250,0);
        Vector3 velocity = new Vector3(500,500,0);


        FitnessDriveOverPointWithAngle fitness = new FitnessDriveOverPointWithAngle(new Path(destinationPoint), new Path(nextPoint), 0.2,0.2, true);

        Vector3 myDirection = new Vector3(10,0,0);
        Vector3 myPos = new Vector3(0,0,0);

        double fitnessValue = fitness.calculateFitnessValue(myPos, myDirection,10, velocity);

        assertEquals(-5.2985724020139235189E-8, fitnessValue,1E-20);

    }

    @Test
    public void calculateFitnessValue02(){ //stopOnPoint = true

        Vector3 destinationPoint = new Vector3(-100,-100,0);
        Vector3 nextPoint = new Vector3(750,1250,0);
        Vector3 velocity = new Vector3(1400,0,0);

        FitnessDriveOverPointWithAngle fitness = new FitnessDriveOverPointWithAngle(new Path(destinationPoint), new Path(nextPoint), 0.2,0.2, true);

        Vector3 myDirection = new Vector3(10,0,0);
        Vector3 myPos = new Vector3(20,20,0);

        double fitnessValue = fitness.calculateFitnessValue(myPos, myDirection,4, velocity);

        assertEquals(-0.00012110219072383959279, fitnessValue,1E-18);
    }

    @Test
    public void calculateFitnessValue03(){ //stopOnPoint = false

        Vector3 destinationPoint = new Vector3(500,500,0);
        Vector3 nextPoint = new Vector3(750,1250,0);
        Vector3 velocity = new Vector3(500,500,0);


        FitnessDriveOverPointWithAngle fitness = new FitnessDriveOverPointWithAngle(new Path(destinationPoint), new Path(nextPoint), 0.2,0.2, false);

        Vector3 myDirection = new Vector3(10,0,0);
        Vector3 myPos = new Vector3(0,0,0);

        double fitnessValue = fitness.calculateFitnessValue(myPos, myDirection,10, velocity);

        assertEquals(5.2985724020139235189E-8, fitnessValue,1E-20);

    }

    @Test
    public void calculateFitnessValue04(){ //stopOnPoint = false

        Vector3 destinationPoint = new Vector3(-100,-100,0);
        Vector3 nextPoint = new Vector3(750,1250,0);
        Vector3 velocity = new Vector3(1400,0,0);

        FitnessDriveOverPointWithAngle fitness = new FitnessDriveOverPointWithAngle(new Path(destinationPoint), new Path(nextPoint), 0.2,0.2, false);

        Vector3 myDirection = new Vector3(10,0,0);
        Vector3 myPos = new Vector3(20,20,0);

        double fitnessValue = fitness.calculateFitnessValue(myPos, myDirection,4, velocity);

        assertEquals(0.00012110219072383959279, fitnessValue,1E-18);
    }

    @Test
    public void calculateFitnessValueComparison01(){

        Vector3 destinationPoint1 = new Vector3(-100,-100,0);
        Vector3 nextPoint1 = new Vector3(750,1250,0);
        Vector3 myCarVelocity1 = new Vector3(1400,0,0);

        FitnessDriveOverPointWithAngle fitness1 = new FitnessDriveOverPointWithAngle(new Path(destinationPoint1), new Path(nextPoint1), 0.2,0.2, true);

        Vector3 myDirection1 = new Vector3(10,0,0);
        Vector3 myPos1 = new Vector3(20,20,0);

        double fitnessValue1 = fitness1.calculateFitnessValue(myPos1, myDirection1,4, myCarVelocity1);

        Vector3 destinationPoint2 = new Vector3(-100,-100,0);
        Vector3 nextPoint2 = new Vector3(750,1250,0);

        FitnessDriveOverPointWithAngle fitness2 = new FitnessDriveOverPointWithAngle(new Path(destinationPoint2), new Path(nextPoint2), 0.2,0.2, true);

        Vector3 myDirection2 = new Vector3(10,0,0);
        Vector3 myPos2 = new Vector3(0,0,0);
        Vector3 myCarVelocity2 = new Vector3(500,0,0);

        double fitnessValue2 = fitness2.calculateFitnessValue(myPos2, myDirection2,3, myCarVelocity2);

        assertTrue(fitnessValue1 > fitnessValue2);
    }


    //TODO DEVIATION
}
