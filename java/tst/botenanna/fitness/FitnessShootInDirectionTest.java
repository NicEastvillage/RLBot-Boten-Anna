package botenanna.fitness;

import botenanna.math.Vector3;
import botenanna.physics.Path;
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

        FitnessShootInDirection fitness = new FitnessShootInDirection(s -> shootPoint, 0.2,0.2);

        double fitnessValue = fitness.calculateFitnessValue(shootPoint, ballLocation, ballVelocity, carLocation, carVelocity, timeSpent);

        assertEquals(-7.6169842401624769146, fitnessValue, 1E-18);
    }

    @Test
    public void calculateFitnessValue02(){
        Vector3 carLocation = new Vector3(0,0,0);
        Vector3 carVelocity = new Vector3(50,100,0);
        Vector3 ballLocation = new Vector3(550,-200,0);
        Vector3 ballVelocity = new Vector3(-30,-200,0);
        Vector3 shootPoint = new Vector3(500,-250,0);
        int timeSpent = 4;

        FitnessShootInDirection fitness = new FitnessShootInDirection(s -> shootPoint, 0.2,0.2);

        double fitnessValue = fitness.calculateFitnessValue(shootPoint, ballLocation, ballVelocity, carLocation, carVelocity, timeSpent);

        assertEquals(-10.537481695388022083, fitnessValue, 1E-19);
    }

    @Test
    public void calculateFitnessValue03(){
        Vector3 carLocation = new Vector3(0,0,0);
        Vector3 carVelocity = new Vector3(50,-100,0);
        Vector3 ballLocation = new Vector3(550,200,0);
        Vector3 ballVelocity = new Vector3(-30,200,0);
        Vector3 shootPoint = new Vector3(500,20,0);
        int timeSpent = 4;

        FitnessShootInDirection fitness = new FitnessShootInDirection(s -> shootPoint, 0.2,0.2);

        double fitnessValue = fitness.calculateFitnessValue(shootPoint, ballLocation, ballVelocity, carLocation, carVelocity, timeSpent);

        assertEquals(-12.976315744756912022, fitnessValue, 1E-19);
    }

    @Test
    public void calculateFitnessValueComparison01(){

        Vector3 carLocation1 = new Vector3(0,0,0);
        Vector3 carVelocity1 = new Vector3(50,-100,0);
        Vector3 ballLocation1 = new Vector3(550,200,0);
        Vector3 ballVelocity1 = new Vector3(-30,200,0);
        Vector3 shootPoint = new Vector3(500,20,0);
        int timeSpent1 = 4;

        FitnessShootInDirection fitness1 = new FitnessShootInDirection(s -> shootPoint, 0.2,0.2);

        double fitnessValue1 = fitness1.calculateFitnessValue(shootPoint, ballLocation1, ballVelocity1, carLocation1, carVelocity1, timeSpent1);

        int timeSpent2 = 1;

        double fitnessValue2 = fitness1.calculateFitnessValue(shootPoint, ballLocation1, ballVelocity1, carLocation1, carVelocity1, timeSpent2);

        assertTrue(fitnessValue1 < fitnessValue2);
    }
}