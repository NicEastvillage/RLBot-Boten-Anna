package botenanna.intentions;

import botenanna.math.Vector3;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class IntentionFunctionArriveAtPointAtTimeTest {

    @Test
    public void calculateWorkValue01(){

        Vector3 point = new Vector3(1000,1000,0);
        int arrivalTime = 10;
        int timeSpent = 5;

        IntentionFunctionArriveAtPointAtTime intentionFunction = new IntentionFunctionArriveAtPointAtTime(s -> point, arrivalTime, 0.2,0.2);

        Vector3 myVelocity = new Vector3(10,0,0);
        Vector3 myPosition = new Vector3(0,0,0);

        double workValue = intentionFunction.calculateWork(point, myPosition, myVelocity,timeSpent);

        assertEquals(1.5680150693034389431, workValue, 1E-15);
    }

    @Test
    public void calculateWorkValue02(){

        Vector3 point = new Vector3(2500,2500,0);
        int arrival = 5;
        int timeSpent = 2;

        IntentionFunctionArriveAtPointAtTime intentionFunction = new IntentionFunctionArriveAtPointAtTime(s -> point, arrival, 0.2,0.2);

        Vector3 myVelocity = new Vector3(100,0,0);
        Vector3 myPosition = new Vector3(100,100,0);

        double workValue = intentionFunction.calculateWork(point, myPosition, myVelocity, timeSpent);

        assertEquals(2.9903222663959361041, workValue, 1E-15);
    }

    @Test
    public void calculateWorkValue03(){

        Vector3 point = new Vector3(-3000,1000,0);
        int arrival = 5;
        int timeSpent = 5;

        IntentionFunctionArriveAtPointAtTime intentionFunction = new IntentionFunctionArriveAtPointAtTime(s -> point, arrival, 0.2,0.2);

        Vector3 myVelocity = new Vector3(-300,-300,0);
        Vector3 myPosition = new Vector3(200,0,0);

        double workValue = intentionFunction.calculateWork(point, myPosition, myVelocity, timeSpent);

        assertEquals(7.1674037827432521584, workValue, 1E-15);
    }

    @Test
    public void calculateWorkValueComparison01(){

        Vector3 point1 = new Vector3(-3000,1000,0);
        int arrival1 = 5;
        int timeSpent1 = 5;

        IntentionFunctionArriveAtPointAtTime intentionFunction1 = new IntentionFunctionArriveAtPointAtTime(s -> point1, arrival1, 0.2,0.2);

        Vector3 myVelocity1 = new Vector3(-300,-300,0);
        Vector3 myPosition1 = new Vector3(200,0,0);

        double workValue1 = intentionFunction1.calculateWork(point1, myPosition1, myVelocity1, timeSpent1);



        Vector3 point2 = new Vector3(-1000,1000,0);
        int arrival2 = 5;
        int timeSpent2 = 5;

        IntentionFunctionArriveAtPointAtTime intentionFunction2 = new IntentionFunctionArriveAtPointAtTime(s -> point2, arrival2, 0.2,0.2);

        Vector3 myVelocity2 = new Vector3(-300,-300,0);
        Vector3 myPosition2 = new Vector3(200,0,0);

        double workValue2 = intentionFunction2.calculateWork(point2, myPosition2, myVelocity2, timeSpent2);

        assertTrue(workValue1 > workValue2);
    }

    //TODO: deviation

}
