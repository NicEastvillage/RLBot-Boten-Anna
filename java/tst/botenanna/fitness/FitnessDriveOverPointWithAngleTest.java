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

        assertEquals(58.329620035741777241, fitnessValue,1E-10);

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

        assertEquals(15.632299441756103165, fitnessValue,1E-10);
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

        assertEquals(-58.329620035741777241, fitnessValue,1E-10);

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

        assertEquals(-15.632299441756103165, fitnessValue,1E-10);
    }

    @Test
    public void calculateFitnessValue05(){ //stopOnPoint = false

        Vector3 destinationPoint = new Vector3(0,0,100.4827880);
        Vector3 nextPoint = new Vector3(0,-5000,0);
        Vector3 velocity = new Vector3(0,0,0);

        FitnessDriveOverPointWithAngle fitness = new FitnessDriveOverPointWithAngle(new Path(destinationPoint), new Path(nextPoint), 0.2,0.2, false);

        Vector3 myDirection = new Vector3(1.39070918E-7,0.9999999999,0);
        Vector3 myPos = new Vector3(255.998899,-3839.991455,43.5251007);

        double fitnessValue = fitness.calculateFitnessValue(myPos, myDirection,0, velocity);

        assertEquals(Double.MIN_VALUE, fitnessValue, 1E-100);
    }

    @Test
    public void calculateFitnessValueComparison01(){

        Vector3 destinationPoint1 = new Vector3(750,500,0);
        Vector3 nextPoint1 = new Vector3(750,0,0);
        Vector3 myCarVelocity1 = new Vector3(400,0,0);

        FitnessDriveOverPointWithAngle fitness1 = new FitnessDriveOverPointWithAngle(new Path(destinationPoint1), new Path(nextPoint1), 0.2,0.2, true);

        Vector3 myDirection1 = new Vector3(10,0,0);
        Vector3 myPos1 = new Vector3(0,0,0);

        double fitnessValue1 = fitness1.calculateFitnessValue(myPos1, myDirection1,3, myCarVelocity1);

        Vector3 nextPoint2 = new Vector3(750,100,0);
        Vector3 destinationPoint2 = new Vector3(750,0,0);

        FitnessDriveOverPointWithAngle fitness2 = new FitnessDriveOverPointWithAngle(new Path(destinationPoint2), new Path(nextPoint2), 0.2,0.2, true);

        Vector3 myDirection2 = new Vector3(10,0,0);
        Vector3 myPos2 = new Vector3(0,0,0);
        Vector3 myCarVelocity2 = new Vector3(300,0,0);

        double fitnessValue2 = fitness2.calculateFitnessValue(myPos2, myDirection2,3, myCarVelocity2);

        System.out.println(fitnessValue1);
        System.out.println(fitnessValue2);

        assertTrue(fitnessValue1 < fitnessValue2);
    }


    //TODO DEVIATION
}
