package botenanna.fitness;

import botenanna.math.Vector3;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FitnessArriveAtPointAtTimeTest {

    @Test
    public void calculateFitnessValue01(){

        Vector3 point = new Vector3(1000,1000,0);
        int arrivalTime = 10;

        FitnessArriveAtPointAtTime fitness = new FitnessArriveAtPointAtTime(0.2,0.2, point, arrivalTime);

        Vector3 myVelocity = new Vector3(10,0,0);
        Vector3 myPosition = new Vector3(0,0,0);

        double fitnessValue = fitness.calculateFitnessValue(myPosition, myVelocity,1);

        assertEquals(0.004106098800244374067, fitnessValue, 1E-3); //TODO: MIKKEL FIX THIS! NOT PRECISE ENOUGH!
    }
}
