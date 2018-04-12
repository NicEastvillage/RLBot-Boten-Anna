package botenanna.fitness;

import botenanna.game.Situation;
import botenanna.math.Vector3;
import botenanna.physics.Path;

/** This class is used when you want a fitness value for "Drive over a point with a specific angle". */
public class FitnessDriveOverPointWithAngle implements FitnessInterface {

    private final int DIST_SCALE = 450;
    private final double ANGLE_SCALE = 5.09299;

    private double angleDeviation;
    private double distDeviation;
    private Path destinationPoint;
    private Path nextPoint;
    private boolean stopOnPoint;

    /** @param destinationPoint the destination point.
     *  @param nextPoint the direction to drive in when destination point is reached.
     *  @param angleDeviation an value that the angle is allowed to deviate.
     *  @param distDeviation an value that the distance is allowed to deviate.
     *  @param stopOnPoint should the car stop on the point or drive over. */
    public FitnessDriveOverPointWithAngle(Path destinationPoint, Path nextPoint, double angleDeviation, double distDeviation, boolean stopOnPoint) {
        this.destinationPoint = destinationPoint;
        this.nextPoint = nextPoint;
        this.angleDeviation = angleDeviation;
        this.distDeviation = distDeviation;
        this.stopOnPoint = stopOnPoint;
    }

    /**	Takes a situation and time spent and returns a fitness value of that situation.
     *  This method extracts needed data from Situation and passes in on to calculation.
     *  This is done to make the method testable.
     *  @param situation the situation to be evaluated.
     *  @param timeSpent the seconds used since origin of situation.
     *  @return a fitness value for the given situation. */
    @Override
    public double calculateFitness(Situation situation, double timeSpent){
        return calculateFitnessValue(situation.myCar.getPosition(), situation.myCar.frontVector, timeSpent, situation.myCar.getVelocity());
    }

    /**	Takes the needed information and calculates the fitness value.
     *  @param myPosition my cars position.
     *  @param myDirection my cars direction.
     *  @param timeSpent the seconds used since origin of the situation.
     *  @return a fitness value for the given situation. */
    double calculateFitnessValue(Vector3 myPosition, Vector3 myDirection, double timeSpent, Vector3 carVelocity){

        Vector3 currentDestPoint = destinationPoint.evaluate(timeSpent);
        Vector3 currentNextPoint = nextPoint.evaluate(timeSpent);

        double distanceToPoint = myPosition.getDistanceTo(currentDestPoint);
        Vector3 desiredDirectionVector = currentNextPoint.minus(currentDestPoint);
        double angleDifference = myDirection.getAngleTo(desiredDirectionVector);
        double velocity = carVelocity.getMagnitude();

        if(stopOnPoint){
            return (Math.pow(Math.E, -(timeSpent + Math.abs(angleDifference * ANGLE_SCALE) + (distanceToPoint / DIST_SCALE))))
                    * -(2300/ (Math.abs((velocity == 0) ? 1 : velocity)));
        }else{
            return (Math.pow(Math.E, -(timeSpent + Math.abs(angleDifference * ANGLE_SCALE) + (distanceToPoint / DIST_SCALE))))
                    * 2300/ (Math.abs((velocity == 0) ? 1 : velocity));
        }


    }

    /** Checks if the deviations are fulfilled.
     *  @param situation the situation to be evaluated.
     *  @param timeSpent the time spend since origin.
     *  @return true if the variables are less or equal to the deviation. */
    @Override
    public boolean isDeviationFulfilled(Situation situation, double timeSpent) {

        //Calculate function variables
        double distToPoint = situation.myCar.getPosition().getDistanceTo(destinationPoint.evaluate(timeSpent)); // Distance
        double angToPoint = situation.myCar.getPosition().getAngleTo(destinationPoint.evaluate(timeSpent)); // Angle

        if(distToPoint <= distDeviation){
            if(angToPoint <= angleDeviation)
                return true;
        }

        return false;
    }
}
