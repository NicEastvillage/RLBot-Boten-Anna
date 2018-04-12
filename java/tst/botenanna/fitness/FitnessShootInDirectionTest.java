package botenanna.fitness;

import botenanna.math.Vector3;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FitnessShootInDirectionTest {

    @Test
    public void calculateFitnessValue01(){
        Vector3 carLocation = new Vector3(0,0,0);
        Vector3 carVelocity = new Vector3(50,0,0);
        Vector3 ballLocation = new Vector3(200,200,0);
        Vector3 ballVelocity = new Vector3(-30,30,0);
        Vector3 shootPoint = new Vector3(500,250,0);
        int timeSpent = 4;

        FitnessShootInDirection fitness = new FitnessShootInDirection(shootPoint);

        double fitnessValue = fitness.calculateFitnessValue(ballLocation, ballVelocity, carLocation, carVelocity, timeSpent);

        assertEquals(0.00049202342040168509727, fitnessValue, 1E-18);
    }

    @Test
    public void calculateFitnessValue02(){
        Vector3 carLocation = new Vector3(0,0,0);
        Vector3 carVelocity = new Vector3(50,100,0);
        Vector3 ballLocation = new Vector3(550,-200,0);
        Vector3 ballVelocity = new Vector3(-30,-200,0);
        Vector3 shootPoint = new Vector3(500,-250,0);
        int timeSpent = 4;

        FitnessShootInDirection fitness = new FitnessShootInDirection(shootPoint);

        double fitnessValue = fitness.calculateFitnessValue(ballLocation, ballVelocity, carLocation, carVelocity, timeSpent);

        assertEquals(0.000026523439794358941165, fitnessValue, 1E-19);
    }

    @Test
    public void calculateFitnessValue03(){
        Vector3 carLocation = new Vector3(0,0,0);
        Vector3 carVelocity = new Vector3(50,-100,0);
        Vector3 ballLocation = new Vector3(550,200,0);
        Vector3 ballVelocity = new Vector3(-30,200,0);
        Vector3 shootPoint = new Vector3(500,20,0);
        int timeSpent = 4;

        FitnessShootInDirection fitness = new FitnessShootInDirection(shootPoint);

        double fitnessValue = fitness.calculateFitnessValue(ballLocation, ballVelocity, carLocation, carVelocity, timeSpent);

        assertEquals(0.0000023145026193631957119, fitnessValue, 1E-19);
    }

    @Test
    public void calculateFitnessValueComparison01(){

        Vector3 carLocation1 = new Vector3(0,0,0);
        Vector3 carVelocity1 = new Vector3(50,-100,0);
        Vector3 ballLocation1 = new Vector3(550,200,0);
        Vector3 ballVelocity1 = new Vector3(-30,200,0);
        Vector3 shootPoint1 = new Vector3(500,20,0);
        int timeSpent1 = 4;

        FitnessShootInDirection fitness1 = new FitnessShootInDirection(shootPoint1);

        double fitnessValue1 = fitness1.calculateFitnessValue(ballLocation1, ballVelocity1, carLocation1, carVelocity1, timeSpent1);

        int timeSpent2 = 1;

        double fitnessValue2 = fitness1.calculateFitnessValue(ballLocation1, ballVelocity1, carLocation1, carVelocity1, timeSpent2);

        assertTrue(fitnessValue1 < fitnessValue2);
    }
}
