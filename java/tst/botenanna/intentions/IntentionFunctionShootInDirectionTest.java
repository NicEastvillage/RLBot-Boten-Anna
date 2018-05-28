package botenanna.intentions;

import botenanna.math.Vector3;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class IntentionFunctionShootInDirectionTest {

    @Test
    public void calculateWork01(){
        Vector3 carLocation = new Vector3(0,0,0);
        Vector3 carVelocity = new Vector3(50,0,0);
        Vector3 ballLocation = new Vector3(200,200,0);
        Vector3 ballVelocity = new Vector3(-30,30,0);
        Vector3 shootPoint = new Vector3(500,250,0);
        int timeSpent = 4;

        IntentionFunctionShootInDirection intentionFunction = new IntentionFunctionShootInDirection(s -> shootPoint, 0.2,0.2);

        double workValue = intentionFunction.calculateWork(shootPoint, ballLocation, ballVelocity, carLocation, carVelocity, timeSpent);

        assertEquals(4.6325951900722598711, workValue, 1E-18);
    }

    @Test
    public void calculateWork02(){
        Vector3 carLocation = new Vector3(0,0,0);
        Vector3 carVelocity = new Vector3(50,100,0);
        Vector3 ballLocation = new Vector3(550,-200,0);
        Vector3 ballVelocity = new Vector3(-30,-200,0);
        Vector3 shootPoint = new Vector3(500,-250,0);
        int timeSpent = 4;

        IntentionFunctionShootInDirection intentionFunction = new IntentionFunctionShootInDirection(s -> shootPoint, 0.2,0.2);

        double workValue = intentionFunction.calculateWork(shootPoint, ballLocation, ballVelocity, carLocation, carVelocity, timeSpent);

        assertEquals(5.3076296588486197063, workValue, 1E-19);
    }

    @Test
    public void calculateWork03(){
        Vector3 carLocation = new Vector3(0,0,0);
        Vector3 carVelocity = new Vector3(50,-100,0);
        Vector3 ballLocation = new Vector3(550,200,0);
        Vector3 ballVelocity = new Vector3(-30,200,0);
        Vector3 shootPoint = new Vector3(500,20,0);
        int timeSpent = 4;

        IntentionFunctionShootInDirection intentionFunction = new IntentionFunctionShootInDirection(s -> shootPoint, 0.2,0.2);

        double workValue = intentionFunction.calculateWork(shootPoint, ballLocation, ballVelocity, carLocation, carVelocity, timeSpent);

        assertEquals(5.3109395723007878006, workValue, 1E-19);
    }

    @Test
    public void calculateWorkValueComparison01(){

        Vector3 carLocation1 = new Vector3(0,0,0);
        Vector3 carVelocity1 = new Vector3(50,-100,0);
        Vector3 ballLocation1 = new Vector3(550,200,0);
        Vector3 ballVelocity1 = new Vector3(-30,200,0);
        Vector3 shootPoint = new Vector3(500,20,0);
        int timeSpent1 = 4;

        IntentionFunctionShootInDirection intentionFunction1 = new IntentionFunctionShootInDirection(s -> shootPoint, 0.2,0.2);

        double workValue1 = intentionFunction1.calculateWork(shootPoint, ballLocation1, ballVelocity1, carLocation1, carVelocity1, timeSpent1);

        int timeSpent2 = 1;

        double workValue2 = intentionFunction1.calculateWork(shootPoint, ballLocation1, ballVelocity1, carLocation1, carVelocity1, timeSpent2);

        assertTrue(workValue1 > workValue2);
    }
}