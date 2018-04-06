package botenanna.fitness;

import botenanna.game.Situation;
import botenanna.math.Vector3;

/** This class is used when you want a fitness value for "Arrive at a point with a specific angle. */
public class fitnessDriveOverPointWithAngle implements fitnessInterface {

    private final int DIST_SCALE = 450;
    private final double ANGLE_SCALE = 5.09299;

    private double angleDeviation;
    private double distDeviation;
    private Vector3 point;

     /**@param point the destination point.
     *  @param angleDeviation an value that the angle is allowed to deviate.
     *  @param distDeviation an value that the distance is allowed to deviate. */
    public fitnessDriveOverPointWithAngle(Vector3 point, double angleDeviation, double distDeviation) {
        this.point = point;
        this.angleDeviation = angleDeviation;
        this.distDeviation = distDeviation;
    }

    /**	Takes a situation and time spent and returns a fitness value of that situation.
     *  @param situation the situation to be evaluated.
     *  @param timeSpent the seconds used since origin of situation.
     *  @return a fitness value for the given situation. */
    @Override
    public double calculateFitness(Situation situation, double timeSpent){

        //Calculate function variables
        double angToPoint = situation.myCar.position.getAngleTo(point); // Angle
        double distToPoint = situation.myCar.position.getDistanceTo(point); // Distance

        //Calculate and return the fitness
        return Math.pow(Math.E, -(timeSpent + Math.abs(angToPoint * ANGLE_SCALE) + (distToPoint * DIST_SCALE)));
    }

    /** Checks if the deviations are fulfilled.
     *  @param situation the one to be evaluated.
     *  @param timeSpent the time spend since origin.
     *  @return true if the variables are less or equal to the deviation. */
    @Override
    public boolean isDeviationFulfilled(Situation situation, double timeSpent) {

        //Calculate function variables
        double distToPoint = situation.myCar.position.getDistanceTo(point); // Distance
        double angToPoint = situation.myCar.position.getAngleTo(point); // Angle

        if(distToPoint <= distDeviation){
            if(angToPoint <= angleDeviation)
                return true;
        }

        return false;
    }
}
