package botenanna.intentions;

import botenanna.game.Car;
import botenanna.game.Situation;
import botenanna.math.Vector3;

import java.util.function.Function;

/** This class is used when you want an intention value for "Drive over a point with a specific angle". */
public class IntentionFunctionDriveOverPointWithAngle implements IntentionFunction {

    private final double DIST_SCALE = 1/450d;
    private final double ANGLE_SCALE = 1/128d;
    private final double VEL_SCALE = 1/1500d;

    private double angleDeviation;
    private double distDeviation;
    private Function<Situation, Vector3> destinationPointFunc;
    private Function<Situation, Vector3> nextPointFunc;
    private boolean stopOnPoint;

    /** @param destinationPointFunc the destination point.
     *  @param nextPointFunc the direction to drive in when destination point is reached.
     *  @param angleDeviation an value that the angle is allowed to deviate.
     *  @param distDeviation an value that the distance is allowed to deviate.
     *  @param stopOnPoint should the car stop on the point or drive over. */
    public IntentionFunctionDriveOverPointWithAngle(Function<Situation, Vector3> destinationPointFunc, Function<Situation,
            Vector3> nextPointFunc, double angleDeviation, double distDeviation, boolean stopOnPoint) {

        this.destinationPointFunc = destinationPointFunc;
        this.nextPointFunc = nextPointFunc;
        this.angleDeviation = angleDeviation;
        this.distDeviation = distDeviation;
        this.stopOnPoint = stopOnPoint;
    }

    /**	Takes a situation and time spent and returns an intention value of that situation.
     *  This method extracts needed data from Situation and passes in on to calculation.
     *  This is done to make the method testable.
     *  @param situation the situation to be evaluated.
     *  @param timeSpent the seconds used since origin of situation.
     *  @return an intention value for the given situation. */
    @Override
    public double compute(Situation situation, double timeSpent){
        Car myCar = situation.getMyCar();
        return calculateWork(destinationPointFunc.apply(situation), nextPointFunc.apply(situation),
                myCar.getPosition(), myCar.getFrontVector(), timeSpent, myCar.getVelocity());
    }

    /**	Takes the needed information and calculates the intention value.
     *  @param myPosition my cars position.
     *  @param myDirection my cars direction.
     *  @param timeSpent the seconds used since origin of the situation.
     *  @return an intention value for the given situation. */
    double calculateWork(Vector3 dest, Vector3 next, Vector3 myPosition, Vector3 myDirection, double timeSpent, Vector3 carVelocity){

        double distanceToPoint = myPosition.getDistanceTo(dest);
        Vector3 desiredDirectionVector = next.minus(dest);
        double angleDifference = myDirection.getAngleTo(desiredDirectionVector);
        double velocity = carVelocity.getMagnitude();

        // Avoid divide by zero error
        if (velocity == 0)
            return Double.MAX_VALUE;

        double work = timeSpent + angleDifference*ANGLE_SCALE + distanceToPoint*DIST_SCALE;

        if (stopOnPoint) {
            return work + velocity*VEL_SCALE;
        } else {
            return work - velocity*VEL_SCALE;
        }
    }

    /** Checks if the deviations are fulfilled.
     *  @param situation the situation to be evaluated.
     *  @param timeSpent the time spend since origin.
     *  @return true if the variables are less or equal to the deviation. */
    @Override
    public boolean isDeviationFulfilled(Situation situation, double timeSpent) {

        Vector3 myPos = situation.getMyCar().getPosition();
        Vector3 dest = destinationPointFunc.apply(situation);

        //Calculate function variables
        double distToPoint = myPos.getDistanceTo(dest); // Distance
        double angToPoint = myPos.getAngleTo(dest); // Angle

        return distToPoint <= distDeviation && angToPoint <= angleDeviation;
    }
}
