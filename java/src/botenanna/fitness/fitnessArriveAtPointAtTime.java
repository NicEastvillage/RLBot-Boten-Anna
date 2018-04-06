package botenanna.fitness;

import botenanna.AgentInput;
import botenanna.math.Vector3;

public class fitnessArriveAtPointAtTime implements fitnessInterface {

    private final int DIST_SCALE = 450;
    private final int VEL_SCALE = 200;

    private double distDeviation;
    private double velDeviation;
    private Vector3 point;
    private int arrivalTime;

    public fitnessArriveAtPointAtTime(double distDeviation, double velDeviation, Vector3 point, int arrivalTime) {
        this.distDeviation = distDeviation;
        this.velDeviation = velDeviation;
        this.point = point;
        this.arrivalTime = arrivalTime;
    }

    @Override
    public double calculateFitness(AgentInput situation, double timeSpent) {

        //Calculate function variables
        double distToPoint = situation.myCar.position.getDistanceTo(point); // Distance
        double velocity = situation.myCar.velocity.getMagnitude(); // Velocity
        double timeValue = (timeSpent < arrivalTime) ? timeSpent / arrivalTime : -(timeSpent/arrivalTime) + 2;

        //Calculate and return the fitness
        return Math.pow(Math.E, -(distToPoint * DIST_SCALE) + (velocity * VEL_SCALE)) * timeValue;
    }

    @Override
    public boolean isDeviationFulfilled(AgentInput situation, double timeSpent) {

        //Calculate function variables
        double distToPoint = situation.myCar.position.getDistanceTo(point); // Distance
        double velocity = situation.myCar.velocity.getMagnitude(); // Velocity

        if(distToPoint <= distDeviation){
            if(velocity <= velDeviation)
                return true;
        }

        return false;
    }
}
