package botenanna.fitness;

import botenanna.game.Situation;
import botenanna.math.Vector3;
import botenanna.physics.Path;

/** This class is used when you want a fitness value for "Arrive at a point at a specific time. */
public class FitnessArriveAtPointAtTime implements FitnessInterface {

    private final int DIST_SCALE = 450;
    private final int VEL_SCALE = 200;

    private double distDeviation;
    private double velDeviation;
    private Path point;
    private double arrivalTime;

    /** @param point the destination point.
     *  @param arrivalTime the desired time of arrival. //TODO: Mikkel... might have to be reworked :)
     *  @param distDeviation the deviation in distance to desired point.
     *  @param velDeviation the deviation in velocity. */
    public FitnessArriveAtPointAtTime(Path point, double arrivalTime, double distDeviation, double velDeviation) {
        this.distDeviation = distDeviation;
        this.velDeviation = velDeviation;
        this.point = point;
        this.arrivalTime = arrivalTime;
    }

    /**	Takes a situation and time spent and returns a fitness value of that situation.
     *  This method extracts needed data from Situation and passes in on to calculation.
     *  This is done to make the method testable.
     *  @param situation the situation to be evaluated.
     *  @param timeSpent the seconds used since origin of situation.
     *  @return a fitness value for the given situation. */
    @Override
    public double calculateFitness(Situation situation, double timeSpent) {

        return calculateFitnessValue(situation.getMyCar().getPosition(), situation.getMyCar().getVelocity(), timeSpent);
    }

    /** Takes the needed information and calculates the fitness value.
     * @param myPosition my cars position.
     * @param myVelocity my cars velocity.
     * @param timeSpent the seconds used since origin of the situation.
     * @return a fitness value for the given situation. */
    double calculateFitnessValue(Vector3 myPosition, Vector3 myVelocity, double timeSpent){

        //Calculate function variables
        double distToPoint = myPosition.getDistanceTo(point.evaluate(timeSpent)); // Distance
        double velocity = myVelocity.getMagnitude(); // Velocity
        double timeValue = (timeSpent < arrivalTime) ? timeSpent / arrivalTime : -(timeSpent/arrivalTime) + 2;

        return -((distToPoint / DIST_SCALE) + (velocity / VEL_SCALE)) * timeValue;
    }

    /** Checks if the deviations are fulfilled.
     *  @param situation the situation to be evaluated
     *  @param timeSpent the time spend since origin.
     *  @return true if the variables are less or equal to the deviation. */
    @Override
    public boolean isDeviationFulfilled(Situation situation, double timeSpent) {

        //Calculate function variables
        double distToPoint = situation.getMyCar().getPosition().getDistanceTo(point.evaluate(timeSpent)); // Distance
        double velocity = situation.getMyCar().getVelocity().getMagnitude(); // Velocity

        if(distToPoint <= distDeviation){
            if(velocity <= velDeviation)
                return true;
        }

        return false;
    }
}
