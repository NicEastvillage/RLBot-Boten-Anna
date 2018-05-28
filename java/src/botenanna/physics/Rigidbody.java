package botenanna.physics;

import botenanna.math.Vector3;

/** A physical object with position, velocity and acceleration */
public class Rigidbody implements Cloneable {

    private Vector3 position = new Vector3();
    private Vector3 velocity = new Vector3();
    private Vector3 acceleration = new Vector3();
    private Vector3 rotation = new Vector3();
    private Vector3 angularVelocity = new Vector3();

    /** Clone this Rigidbody.
     * @return a Rigidbody with the same position, velocity, acceleration, rotation and angularVelocity. */
    @Override
    public Rigidbody clone() {
        Rigidbody copy = new Rigidbody();
        copy.setPosition(position);
        copy.setVelocity(velocity);
        copy.setAcceleration(acceleration);
        copy.setRotation(rotation);
        copy.setAngularVelocity(angularVelocity);
        return copy;
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

    /** @return the acceleration, excluding gravity even this RigidBody is affected by gravity. */
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

    public Vector3 getAngularVelocity() {
        return angularVelocity;
    }

    public void setAngularVelocity(Vector3 angularVelocity) {
        this.angularVelocity = angularVelocity;
    }
}
