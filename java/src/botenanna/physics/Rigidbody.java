package botenanna.physics;

import botenanna.math.Vector3;

/** A physical object with position, velocity and acceleration */
public class Rigidbody implements Cloneable {

    public static final Vector3 GRAVITY = Vector3.DOWN.scale(650);

    private Vector3 position = new Vector3();
    private Vector3 velocity = new Vector3();
    private Vector3 acceleration = new Vector3();
    private Vector3 rotation = new Vector3();

    private boolean affectedByGravity = false;

    /**
     * Update position and velocity as time changes to simulate movement through space.
     * @param timeDelta time passed in seconds
     */
    public void step(double timeDelta) {

        Vector3 actualAcceleration = getActualAcceleration();

        // new_position = p + (1/2 * a * t^2) + (v * t)
        position = position.plus(actualAcceleration.scale(0.5 * timeDelta * timeDelta)).plus(velocity.scale(timeDelta));
        velocity = velocity.plus(actualAcceleration.scale(timeDelta));

        // TODO Collision?
    }

    /** Make a copy which is moved {@code timeDelta} seconds into the future. Useful when
     * you want to know where this Rigidbody will be, but you don't want to change the state of this Rigidbody.
     * @param timeDelta time passed in seconds
     * @return a copy of this, but {@code timeDelta} seconds into the future.
     * @see #step(double) */
    public Rigidbody stepped(double timeDelta) {
        Rigidbody copy = clone();
        copy.step(timeDelta);
        return copy;
    }

    //region PREDICT_MOVEMENT
    /** <p>Calculate when this rigidbody will be at a specific {@code height} with its current position, velocity and acceleration.
     * Will return NaN if {@code height} is never reached. This function only cares about movement
     * along the z-axis. Acceleration cannot be positive. Gravity will be included, if this is affected by gravity.</p>
     *
     * <p>List of possible cases (t refers to return value):
     * <ul>
     *     <li>If already at {@code height}, then t = 0.</li>
     *     <li>If acceleration is 0:</li>
     *     <ul>
     *         <li>If velocity is 0, then t = NaN.</li>
     *         <li>If velocity is away from {@code height}, then t = NaN.</li>
     *         <li>If velocity is towards {@code height}, then t > 0.</li>
     *     </ul>
     *     <li>If acceleration is negative:</li>
     *     <ul>
     *         <li>If {@code height} is below current z-coordinate, then t > 0.</li>
     *         <li>If the rigidbody's turning point lower than {@code height}, then t = NaN.</li>
     *         <li>If the rigidbody's turning point above {@code height}, then t > 0.</li>
     *     </ul>
     * </ul>
     * </p>
     *
     * @param height the height.
     * @return the expected time till the rigidbody will be at height in seconds, always positive, or NaN if {@code height} is never reached. */
    public double predictArrivalAtHeight(double height) {

        // If already at height, return 0
        if (height == position.z) return 0;

        // Check if there is acceleration involved
        Vector3 actualAcceleration = getActualAcceleration();
        if (actualAcceleration.z == 0) {
            // Only velocity is relevant
            return predictArrivalAtHeightLinear(height);

        } else {
            // Acceleration must be taken into account
            return predictArrivalAtHeightQuadratic(height, actualAcceleration.z);
        }
    }

    /** Helper function for {@link #predictArrivalAtHeight(double)} for when acceleration is relevant.
     * @return the expected time till arrival, or NaN if the rigidbody will never arrive at {@code height} */
    private double predictArrivalAtHeightQuadratic(double height, double actualZAcceleration) {

        assert actualZAcceleration < 0;

        // Check if height is above current z, because then the rigidbody may never get there
        if (height > position.z) {

            // Elapsed time when arriving at the turning point
            double turningTime = -velocity.z / actualZAcceleration;

            // This is in the past?? -> acceleration must have been negative
            if (turningTime < 0) return Double.NaN;

            // Height at turning point
            double turningHeight = 0.5 * actualZAcceleration * turningTime * turningTime + velocity.z * turningTime + position.z;

            // Return null if height is never reached
            if (turningHeight < height) return Double.NaN;

            // The height is reached on the way up!
            if (position.z < height) {
                // See technical documents for this equation : t = (-v + sqrt(2*a*h - 2*a*p + v^2) / a
                return (-velocity.z + Math.sqrt(2 * actualZAcceleration * height - 2 * actualZAcceleration * position.z + velocity.z * velocity.z)) / actualZAcceleration;
            }

            // No cases hit, everything is fine
        }

        // See technical documents for this equation : t = -(v + sqrt(2*a*h - 2*a*p + v^2) / a
        return -(velocity.z + Math.sqrt(2 * actualZAcceleration * height - 2 * actualZAcceleration * position.z + velocity.z * velocity.z)) / actualZAcceleration;
    }

    /** Helper function for {@link #predictArrivalAtHeight(double)} for when there is no acceleration, only velocity.
     * @return the expected time till arrival, or NaN if the rigidbody will never arrive at {@code height} */
    private double predictArrivalAtHeightLinear(double height) {

        if (velocity.z == 0) return Double.NaN; // no velocity

        // time of arrival
        double arrivalTime = (height - position.z) / velocity.z;

        if (arrivalTime < 0) return Double.NaN; // time is in the past -> will never get there

        return arrivalTime;
    }
    //endregion

    /** Clone this Rigidbody.
     * @return a Rigidbody with the same position, velocity, acceleration and gravity */
    @Override
    public Rigidbody clone() {
        Rigidbody copy = new Rigidbody();
        copy.setPosition(position);
        copy.setVelocity(velocity);
        copy.setAcceleration(acceleration);
        copy.setAffectedByGravity(affectedByGravity);
        return copy;
    }

    /** Whether gravity should affect this body as well as acceleration. */
    public void setAffectedByGravity(boolean s) {
        affectedByGravity = s;
    }

    /** @return whether gravity affects this body as well as acceleration. */
    public boolean getAffectedByGravity() {
        return affectedByGravity;
    }

    public Vector3 getPosition() {
        return position;
    }

    public void setPosition(Vector3 position) {
        this.position = position;
    }

    public Vector3 getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector3 velocity) {
        this.velocity = velocity;
    }

    /** @return the acceleration, excluding gravity even this RigidBody is affected by gravity.
     * @see #getActualAcceleration() */
    public Vector3 getAcceleration() {
        return acceleration;
    }

    /** @return the acceleration including gravity if affected by if */
    public Vector3 getActualAcceleration() {
        return affectedByGravity ? acceleration.plus(GRAVITY) : acceleration;
    }

    public void setAcceleration(Vector3 acceleration) {
        this.acceleration = acceleration;
    }

    public Vector3 getRotation() {
        return rotation;
    }

    public void setRotation(Vector3 rotation) {
        this.rotation = rotation;
    }
}
