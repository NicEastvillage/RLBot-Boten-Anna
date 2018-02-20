package botenanna.math;

/** A helper class for all math related to Rocket League */
public class RLMath {

    /** Calculate the angle between a cars forward direction and the direction to the point from the cars position.
     * @param position the cars position
     * @param yaw the cars yaw
     * @param point the point
     * @return the angle between the car and the point (Between -PI and +PI) */
    public static double carsAngleToPoint(Vector2 position, double yaw, Vector2 point) {
        // Find the difference between the cars location and the point
        Vector2 diff = point.minus(position);

        // Calculate the angle between the cars front and direction to the point
        double atan = Math.atan2(diff.y, diff.x);
        double angDiff = atan - yaw;
        if (angDiff > Math.PI) angDiff -= 2 * Math.PI; // Fix ang between -PI and +PI

        return angDiff;
    }

    /** Translate an angular difference to a smooth steering to avoid wobbly driving
     * @param ang the angle of which the car should adjust
     * @return the amount of steering needed to turn to the right angle */
    public static double steeringSmooth(double ang) {
        return 2.0 / (1 + Math.pow(2.71828182845 , -5 * ang)) - 1; // 2/(1+e^(-5x)) - 1
    }
}
