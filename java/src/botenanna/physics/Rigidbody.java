package botenanna.physics;

import botenanna.math.Vector3;

/** A physical object with position, velocity and acceleration */
public class Rigidbody {

    public static final Vector3 GRAVITY = Vector3.DOWN.scale(650);

    private Vector3 position = new Vector3();
    private Vector3 velocity = new Vector3();
    private Vector3 acceleration = new Vector3();
    private Vector3 rotation = new Vector3();

    private boolean affectedByGravity = false;

    /**
     * Update position and velocity as time changes to simulate movement through space.
     * @param timeDelta time passed
     */
    public void step(double timeDelta) {

        Vector3 actualAcceleration = affectedByGravity ? acceleration.plus(GRAVITY) : acceleration;

        // new_position = p + (1/2 * a * t^2) + (v * t)
        position = position.plus(actualAcceleration.scale(0.5 * timeDelta * timeDelta)).plus(velocity.scale(timeDelta));
        velocity = velocity.plus(actualAcceleration.scale(timeDelta));

        // TODO Collision?
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

    public Vector3 getAcceleration() {
        return acceleration;
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
