package botenanna.intentions;

import botenanna.game.Situation;
import botenanna.math.Vector3;

import java.util.function.Function;

/** This class is used when you want a intention value for "Arrive at a point at a specific time. */
public class IntentionFunctionArriveAtPointAtTime implements IntentionFunction {

    private final double DIST_SCALE = 1/450d;
    private final double VEL_SCALE = 1/1500d;

    private double distDeviation;
    private double velDeviation;
    private Function<Situation, Vector3> pointFunc;
    private double arrivalTime;

    /** @param pointFunc the destination point.
     *  @param arrivalTime the desired time of arrival. Cannot be 0 or negative.
     *  @param distDeviation the deviation in distance to desired point.
     *  @param velDeviation the deviation in velocity. */
    public IntentionFunctionArriveAtPointAtTime(Function<Situation, Vector3> pointFunc, double arrivalTime, double distDeviation, double velDeviation) {
        this.distDeviation = distDeviation;
        this.velDeviation = velDeviation;
        this.pointFunc = pointFunc;
        this.arrivalTime = arrivalTime;
    }

    /**	Takes a situation and time spent and returns a intention value of that situation.
     *  This method extracts needed data from Situation and passes in on to calculation.
     *  This is done to make the method testable.
     *  @param situation the situation to be evaluated.
     *  @param timeSpent the seconds used since origin of situation.
     *  @return an intention value for the given situation. */
    @Override
    public double compute(Situation situation, double timeSpent) {

        return calculateWork(pointFunc.apply(situation), situation.getMyCar().getPosition(), situation.getMyCar().getVelocity(), timeSpent);
    }

    /** Takes the needed information and calculates the intention value.
     * @param myPosition my cars position.
     * @param myVelocity my cars velocity.
     * @param timeSpent the seconds used since origin of the situation.
     * @return an intention value for the given situation. */
    double calculateWork(Vector3 point, Vector3 myPosition, Vector3 myVelocity, double timeSpent){

        //Calculate function variables
        double distToPoint = myPosition.getDistanceTo(point); // Distance
        double velocity = myVelocity.getMagnitude(); // Velocity
        double timeValue = (arrivalTime <= timeSpent) ? -(timeSpent/arrivalTime) + 2 : timeSpent / arrivalTime;

        return (distToPoint*DIST_SCALE - velocity*VEL_SCALE) * timeValue;
    }

    /** Checks if the deviations are fulfilled.
     *  @param situation the situation to be evaluated
     *  @param timeSpent the time spend since origin.
     *  @return true if the variables are less or equal to the deviation. */
    @Override
    public boolean isDeviationFulfilled(Situation situation, double timeSpent) {

        //Calculate function variables
        double distToPoint = situation.getMyCar().getPosition().getDistanceTo(pointFunc.apply(situation)); // Distance
        double velocity = situation.getMyCar().getVelocity().getMagnitude(); // Velocity

        return distToPoint <= distDeviation && velocity <= velDeviation;
    }
}
