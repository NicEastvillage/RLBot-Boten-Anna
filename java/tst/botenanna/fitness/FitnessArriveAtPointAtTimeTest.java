package botenanna.fitness;

import botenanna.math.Vector3;
import botenanna.physics.Path;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FitnessArriveAtPointAtTimeTest {

    @Test
    public void calculateFitnessValue01(){

        Vector3 point = new Vector3(1000,1000,0);
        int arrivalTime = 10;
        int timeSpent = 5;

        FitnessArriveAtPointAtTime fitness = new FitnessArriveAtPointAtTime(s -> point, arrivalTime, 0.2,0.2);

        Vector3 myVelocity = new Vector3(10,0,0);
        Vector3 myPosition = new Vector3(0,0,0);

        double fitnessValue = fitness.calculateFitnessValue(point, myPosition, myVelocity,timeSpent);

        assertEquals(0.020530494001221870335, fitnessValue, 1E-15);
    }

    @Test
    public void calculateFitnessValue02(){

        Vector3 point = new Vector3(2500,2500,0);
        int arrival = 5;
        int timeSpent = 2;

        FitnessArriveAtPointAtTime fitness = new FitnessArriveAtPointAtTime(s -> point, arrival, 0.2,0.2);

        Vector3 myVelocity = new Vector3(100,0,0);
        Vector3 myPosition = new Vector3(100,100,0);

        double fitnessValue = fitness.calculateFitnessValue(point, myPosition, myVelocity, timeSpent);

        assertEquals(0.00012860523176404759589, fitnessValue, 1E-15);
    }

    @Test
    public void calculateFitnessValue03(){

        Vector3 point = new Vector3(-3000,1000,0);
        int arrival = 5;
        int timeSpent = 5;

        FitnessArriveAtPointAtTime fitness = new FitnessArriveAtPointAtTime(s -> point, arrival, 0.2,0.2);

        Vector3 myVelocity = new Vector3(-300,-300,0);
        Vector3 myPosition = new Vector3(200,0,0);

        double fitnessValue = fitness.calculateFitnessValue(point, myPosition, myVelocity, timeSpent);

        assertEquals(0.00006968211737342007798, fitnessValue, 1E-15);
    }

    @Test
    public void calculateFitnessValueComparison01(){

        Vector3 point1 = new Vector3(-3000,1000,0);
        int arrival1 = 5;
        int timeSpent1 = 5;

        FitnessArriveAtPointAtTime fitness1 = new FitnessArriveAtPointAtTime(s -> point1, arrival1, 0.2,0.2);

        Vector3 myVelocity1 = new Vector3(-300,-300,0);
        Vector3 myPosition1 = new Vector3(200,0,0);

        double fitnessValue1 = fitness1.calculateFitnessValue(point1, myPosition1, myVelocity1, timeSpent1);



        Vector3 point2 = new Vector3(-1000,1000,0);
        int arrival2 = 5;
        int timeSpent2 = 5;

        FitnessArriveAtPointAtTime fitness2 = new FitnessArriveAtPointAtTime(s -> point2, arrival2, 0.2,0.2);

        Vector3 myVelocity2 = new Vector3(-300,-300,0);
        Vector3 myPosition2 = new Vector3(200,0,0);

        double fitnessValue2 = fitness2.calculateFitnessValue(point2, myPosition2, myVelocity2, timeSpent2);

        assertTrue(fitnessValue1 < fitnessValue2);
    }

    //TODO: deviation

}
