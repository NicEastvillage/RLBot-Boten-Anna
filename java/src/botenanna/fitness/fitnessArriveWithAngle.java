package botenanna.fitness;

import botenanna.AgentInput;
import botenanna.math.Vector3;

/** This class is used when you want a fitness value for "Arrive at a point with a specific angle. */
public class fitnessArriveWithAngle implements fitnessInterface {

    public static final int DIST_SCALE = 450;
    public static final double ANGLE_SCALE = 5.09299;

    public double angleDeviation;
    public double distDeviation;

    public Vector3 point;

     /**@param point the destination point.
     *  @param angleDeviation an value that the angle is allowed to deviate.
     *  @param distDeviation an value that the distance is allowed to deviate. */
    public fitnessArriveWithAngle(Vector3 point, double angleDeviation, double distDeviation) {
        this.point = point;
        this.angleDeviation = angleDeviation;
        this.distDeviation = distDeviation;
    }

    /**	Takes a situation and time spent and returns a fitness value of that situation.
     *  @param situation the situation to be evaluated.
     *  @param timeSpent the seconds used since origin of situation.
     *  @return a fitness value for the given situation. */
    @Override
    public double calculateFitness(AgentInput situation, double timeSpent){

        //Calculate function variables
        double angToPoint = situation.myCar.position.getAngleTo(point); //ang
        double distToPoint = situation.myCar.position.getDistanceTo(point); //dist

        //Calculate the fitness function
        return calculateFormula(timeSpent, angToPoint * ANGLE_SCALE, distToPoint * DIST_SCALE);
    }

    /** Calculate the fitness.
     *  @param timeSpent the time spend since origin.
     *  @param angScaled angle to a point scaled.
     *  @param distScaled distance to a point scaled.
     *  @return the fitness value*/
    @Override
    public double calculateFormula(double timeSpent, double angScaled, double distScaled){
        return Math.pow(Math.E, -(timeSpent + Math.abs(angScaled) + distScaled));
    }

    /** Checks if the deviations are fulfilled.
     *  @param situation the one to be evaluated.
     *  @param timeSpent the time spend since origin.
     *  @return true if the variables are less or equal to the deviation. */
    @Override
    public boolean isDeviationFulfilled(AgentInput situation, double timeSpent) {

        //Calculate function variables
        double distToPoint = situation.myCar.position.getDistanceTo(point); //dist
        double angToPoint = situation.myCar.position.getAngleTo(point); //ang

        if(distToPoint <= distDeviation){
            if(angToPoint <= angleDeviation)
                return true;
        }

        return false;
    }
}
