package botenanna.fitness;

import botenanna.AgentInput;
import botenanna.math.Vector3;

/** This class is used when you want a fitness value for "Drive over a point with a specific angle. */
public class FitnessDriveOverPointWithAngle implements FitnessInterface {

    private final int DIST_SCALE = 450;
    private final double ANGLE_SCALE = 5.09299;

    private double angleDeviation;
    private double distDeviation;
    private Vector3 destinationPoint;
    private Vector3 nextPoint;

    /** @param destinationPoint the destination point.
     *  @param nextPoint the direction to drive in when destination point is reached.
     *  @param angleDeviation an value that the angle is allowed to deviate.
     *  @param distDeviation an value that the distance is allowed to deviate. */
    public FitnessDriveOverPointWithAngle(Vector3 destinationPoint, Vector3 nextPoint, double angleDeviation, double distDeviation) {
        this.destinationPoint = destinationPoint;
        this.nextPoint = nextPoint;
        this.angleDeviation = angleDeviation;
        this.distDeviation = distDeviation;
    }

    /**	Takes a situation and time spent and returns a fitness value of that situation.
     *  This method extracts needed data from agentInput and passes in on to calculation.
     *  This is done to make the method testable.
     *  @param situation the situation to be evaluated.
     *  @param timeSpent the seconds used since origin of situation.
     *  @return a fitness value for the given situation. */
    @Override
    public double calculateFitness(AgentInput situation, double timeSpent){

        return calculateFitnessValue(situation.myCar.position, situation.myCar.frontVector, timeSpent);
    }

    /**	Takes the needed information and calculates the fitness value.
     *  @param myPosition my cars position.
     *  @param myDirection my cars direction.
     *  @param timeSpent the seconds used since origin of the situation.
     *  @return a fitness value for the given situation. */
    double calculateFitnessValue(Vector3 myPosition, Vector3 myDirection, double timeSpent){

        double distanceToPoint = myPosition.getDistanceTo(destinationPoint);
        Vector3 desiredDirectionVector = nextPoint.minus(destinationPoint);
        double angleDifference = myDirection.getAngleTo(desiredDirectionVector);

        return Math.pow(Math.E, -(timeSpent + Math.abs(angleDifference * ANGLE_SCALE) + (distanceToPoint / DIST_SCALE)));
    }

    /** Checks if the deviations are fulfilled.
     *  @param situation the situation to be evaluated.
     *  @param timeSpent the time spend since origin.
     *  @return true if the variables are less or equal to the deviation. */
    @Override
    public boolean isDeviationFulfilled(AgentInput situation, double timeSpent) {

        //Calculate function variables
        double distToPoint = situation.myCar.position.getDistanceTo(destinationPoint); // Distance
        double angToPoint = situation.myCar.position.getAngleTo(destinationPoint); // Angle

        if(distToPoint <= distDeviation){
            if(angToPoint <= angleDeviation)
                return true;
        }

        return false;
    }
}
