package botenanna.math;

import botenanna.AgentInput;

import java.util.Vector;

/** A helper class for all math related to Rocket League */
public class RLMath {

    /** Calculate the angle between a cars forward direction and the direction to the point from the cars position.
     * @param position the cars position
     * @param yaw the car's yaw
     * @param point the point (e.g. the ball)
     * @return the angle in radians between the car and the point (Between -PI and +PI) */
    public static double carsAngleToPoint(Vector2 position, double yaw, Vector2 point) {
        // Find the difference between the cars location and the point
        Vector2 diff = point.minus(position);

        // Calculate the angle between the cars front and direction to the point
        double atan = Math.atan2(diff.y, diff.x);
        double angDiff = atan - yaw;
        if (angDiff > Math.PI) angDiff -= 2 * Math.PI; // Fix ang between -PI and +PI

        return angDiff;
    }

    /** Translate an angular difference to a smooth steering to avoid wobbly driving. A positive
     * angle will result in a positive steering, and negative will result in negative steering.
     * @param angle how far the car is off; an angle in radians. Preferably in the range -PI to +PI
     * @return the amount of steering needed to smoothly adjust the angle. */
    public static double steeringSmooth(double angle) {
        return 2.0 / (1 + Math.pow(2.71828182845 , -5 * angle)) - 1; // 2/(1+e^(-5x)) - 1
    }

    /** Takes the car rotation and create a vector pointing up relative to the car.
     *  @param carRotation the rotation vector for the car.
     *  @return a vector pointing up relative to the car. */
    public static Vector3 carUpVector(Vector3 carRotation) {
        double roofX = Math.cos(carRotation.roll) * Math.sin(carRotation.pitch) * Math.cos(carRotation.yaw) + Math.sin(carRotation.roll) * Math.sin(carRotation.yaw);
        double roofY = Math.cos(carRotation.yaw) * Math.sin(carRotation.roll) - Math.cos(carRotation.roll) * Math.sin(carRotation.pitch) * Math.sin(carRotation.yaw);
        double roofZ = Math.cos(carRotation.roll) * Math.cos(carRotation.pitch);

        return new Vector3(roofX, roofY, roofZ);
    }

    /** Takes the car rotation and create a vector pointing forward relative to the car.
     *  @param carRotation the rotation vector for the car.
     *  @return a vector pointing forward relative to the car. */
    public static Vector3 carFrontVector(Vector3 carRotation){
        double noseX = -1 * Math.cos(carRotation.pitch) * Math.cos(carRotation.yaw);
        double noseY = Math.cos(carRotation.pitch) * Math.sin(carRotation.yaw);
        double noseZ = Math.sin(carRotation.pitch);

        return new Vector3(noseX, noseY, noseZ);
    }

    /** Takes the car rotation and creates a vector pointing to the side relative to the car.
     *  @param carRotation the rotation vector for the car.
     *  @return a vector pointing to the side relative to the car. */
    public static Vector3 carSideVector(Vector3 carRotation){
        return carUpVector(carRotation).cross(carFrontVector(carRotation));
    }

    /** Takes the car and ball data and creates a double for  the time for collision
     * @param ballV the ball Velocity
     * @param ballLocation the ball location
     * @param myVelocity the car Velocity
     * @param myLocation the car location
     * @param myFrontVector the front vector of the car
     * @return a double representing the time needed to scale the position of the ball.*/
        public  static double predictSeconds(Vector3 ballV, Vector3 ballLocation, Vector3 myVelocity, Vector3 myLocation, Vector3 myFrontVector){
        Vector3 expectedBall;
        double predictSeconds = 0;
        double predict = 0.02;
        double counter = 0.02;
        double velocity;
        boolean isBallStill = false;

        //If the ball is really slow or still, skip the loop and don't predict.
        if(10 > ballV.getMagnitude()){
            isBallStill = true;
        }

        //The loop will find a spot where the distance of expected ball to car minus the carvelocity multiplied by predict is between -25 and 25.
        //That way the agent should always be able to choose the right amount of prediction seconds, although this will probably change a little bit every tick as
        //the carvelocity changes.
        while(predictSeconds < 0.1 && counter <= 5 && !isBallStill){
            expectedBall = ballLocation.plus(ballV.scale(predict));

            // If the car is not really driving, it should overextend its prediction to the future.
            if (myVelocity.getMagnitude() < 800){
                velocity = 800;
            }
            else velocity = myVelocity.getMagnitude();

            if (-25 < expectedBall.minus(myLocation.plus(myFrontVector.scale(70))).getMagnitude() - velocity*predict && expectedBall.minus(myLocation.plus(myFrontVector.scale(70))).getMagnitude() - velocity*predict < 25) {
                predictSeconds = predict;
            }

            predict += 0.02;
            counter += 0.02;
        }

        // If it runs through loop without choosing one, then don't predict (Probably not needed)
        if(counter > 5) {
            predictSeconds = 0;
        }

        // if ball is still, don't predict
        if (isBallStill){
            predictSeconds = 0;
        }
        return predictSeconds;
    }




}
