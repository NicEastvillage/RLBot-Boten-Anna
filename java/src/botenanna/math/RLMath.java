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

    /** Linearly interpolate from {@code a} to {@code b} with time {@code t}, such that {@code t = 0} will return
     * {@code a} and {@code t = 1} will return {@code b}. */
    public static double lerp(double a, double b, double t) {
        return a + (b - a) * t;
    }

    /** Find the time {@code t} that produces value {@code v} when linearly interpolating from {@code a} to {@code b} with time {@code t}.
     * That means if {@code v = a} this will return {@code 0} and if {@code v = b} this will return {@code 1}. */
    public static double invLerp(double a, double b, double v) {
        return b - a != 0 ? v - a / (b - a) : a;
    }
}
