package botenanna.intentions;

import botenanna.math.Vector3;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FitnessArriveAtPointAtTimeTest {

    @Test
    public void calculateFitnessValue01(){

        Vector3 point = new Vector3(1000,1000,0);
        int arrivalTime = 10;
        int timeSpent = 5;

        IntentionFunctionArriveAtPointAtTime fitness = new IntentionFunctionArriveAtPointAtTime(s -> point, arrivalTime, 0.2,0.2);

        Vector3 myVelocity = new Vector3(10,0,0);
        Vector3 myPosition = new Vector3(0,0,0);

        double fitnessValue = fitness.calculateWork(point, myPosition, myVelocity,timeSpent);

        assertEquals(-1.5963484026367722764, fitnessValue, 1E-15);
    }

    @Test
    public void calculateFitnessValue02(){

        Vector3 point = new Vector3(2500,2500,0);
        int arrival = 5;
        int timeSpent = 2;

        IntentionFunctionArriveAtPointAtTime fitness = new IntentionFunctionArriveAtPointAtTime(s -> point, arrival, 0.2,0.2);

        Vector3 myVelocity = new Vector3(100,0,0);
        Vector3 myPosition = new Vector3(100,100,0);

        double fitnessValue = fitness.calculateWork(point, myPosition, myVelocity, timeSpent);

        assertEquals(-3.2169889330626027708, fitnessValue, 1E-15);
    }

    @Test
    public void calculateFitnessValue03(){

        Vector3 point = new Vector3(-3000,1000,0);
        int arrival = 5;
        int timeSpent = 5;

        IntentionFunctionArriveAtPointAtTime fitness = new IntentionFunctionArriveAtPointAtTime(s -> point, arrival, 0.2,0.2);

        Vector3 myVelocity = new Vector3(-300,-300,0);
        Vector3 myPosition = new Vector3(200,0,0);

        double fitnessValue = fitness.calculateWork(point, myPosition, myVelocity, timeSpent);

        assertEquals(-9.5715668387775137414, fitnessValue, 1E-15);
    }

    @Test
    public void calculateFitnessValueComparison01(){

        Vector3 point1 = new Vector3(-3000,1000,0);
        int arrival1 = 5;
        int timeSpent1 = 5;

        IntentionFunctionArriveAtPointAtTime fitness1 = new IntentionFunctionArriveAtPointAtTime(s -> point1, arrival1, 0.2,0.2);

        Vector3 myVelocity1 = new Vector3(-300,-300,0);
        Vector3 myPosition1 = new Vector3(200,0,0);

        double fitnessValue1 = fitness1.calculateWork(point1, myPosition1, myVelocity1, timeSpent1);



        Vector3 point2 = new Vector3(-1000,1000,0);
        int arrival2 = 5;
        int timeSpent2 = 5;

        IntentionFunctionArriveAtPointAtTime fitness2 = new IntentionFunctionArriveAtPointAtTime(s -> point2, arrival2, 0.2,0.2);

        Vector3 myVelocity2 = new Vector3(-300,-300,0);
        Vector3 myPosition2 = new Vector3(200,0,0);

        double fitnessValue2 = fitness2.calculateWork(point2, myPosition2, myVelocity2, timeSpent2);

        assertTrue(fitnessValue1 < fitnessValue2);
    }

    //TODO: deviation

}
