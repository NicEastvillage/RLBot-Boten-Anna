package botenanna.fitness;

import botenanna.AgentInput;
import botenanna.math.Vector3;
import botenanna.physics.Path;

/** This class is used when you want a fitness value for "Shoot in direction". */
public class FitnessShootInDirection implements FitnessInterface {

    private final double ANGLE_SCALE = 5.756462;
    private final int DIST_SCALE = 450;

    private Path shootTowardsPoint;
    private double angleDeviation;
    private double distDeviation;

    /** @param shootTowardsPoint the points to shoot towards
     *  @param angleDeviation an value that the angle is allowed to deviate.
     *  @param distDeviation an value that the distance is allowed to deviate. */
    public FitnessShootInDirection(Path shootTowardsPoint, double angleDeviation, double distDeviation){
        this.shootTowardsPoint = shootTowardsPoint;
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
    public double calculateFitness(AgentInput situation, double timeSpent) {
        return calculateFitnessValue(situation.ball.getPosition(), situation.ball.getVelocity(), situation.myCar.position, situation.myCar.velocity, timeSpent);
    }

    /**	Takes the needed information and calculates the fitness value.
     *  @param myPosition my cars position.
     *  @param myDirection my cars direction.
     *  @param timeSpent the seconds used since origin of the situation.
     *  @return a fitness value for the given situation. */

    /** Takes the needed information and calculates the fitness value.
     *  @param ballLocation ball location
     *  @param ballVelocity ball velocity
     *  @param carLocation the cars location.
     *  @param carVelocity the cars velocity.
     *  @param timeSpent the seconds used since origin of the situation.
     *  @return a fitness value for the given situation. */
    double calculateFitnessValue(Vector3 ballLocation, Vector3 ballVelocity, Vector3 carLocation, Vector3 carVelocity, double timeSpent){

        Vector3 desiredShotDirection = shootTowardsPoint.evaluate(timeSpent).minus(carLocation); //From car to desiredPoint
        Vector3 currentShotDirection = ballVelocity.plus(carVelocity);

        double distanceToBall = ballLocation.getDistanceTo(carLocation);
        double angleDiffernce = desiredShotDirection.getAngleTo(currentShotDirection);

        return (Math.pow(Math.E, -(timeSpent + (distanceToBall / DIST_SCALE) + angleDiffernce * ANGLE_SCALE)));
    }

    /** Checks if the deviations are fulfilled.
     *  @param situation the situation to be evaluated.
     *  @param timeSpent the time spend since origin.
     *  @return true if the variables are less or equal to the deviation. */
    @Override
    public boolean isDeviationFulfilled(AgentInput situation, double timeSpent) {

        //Calculate function variables
        double distToBall = situation.myCar.position.getDistanceTo(shootTowardsPoint.evaluate(timeSpent)); // Distance

        Vector3 desiredShotDirection = shootTowardsPoint.evaluate(timeSpent).minus(situation.myCar.position); //From car to desiredPoint
        Vector3 currentShotDirection = situation.ball.getVelocity().plus(situation.myCar.velocity);
        double angleDifference = desiredShotDirection.getAngleTo(currentShotDirection);

        if(distToBall <= distDeviation){
            if(angleDifference <= angleDeviation)
                return true;
        }

        return false;
    }
}
