package botenanna.math;

import rlbot.api.GameData;

import java.util.Objects;

/** A vector with three components: x, y, z */
public class Vector3 {

    public static final Vector3 UP = new Vector3(0, 0, 1);
    public static final Vector3 RIGHT = new Vector3(1, 0, 0);
    public static final Vector3 FORWARD = new Vector3(0, 1, 0);
    public static final Vector3 BACKWARDS = new Vector3(0, -1, 0);
    public static final Vector3 LEFT = new Vector3(-1, 0, 0);
    public static final Vector3 DOWN = new Vector3(0, 0, -1);

    public final double x;
    public final double y;
    public final double z;

    // Vector3 are also used to represent rotations, but these variables are the same as x, y, z
    /** Angle of counterclockwise rotation around the x-axis */
    public double pitch;
    /** Angle of counterclockwise rotation around the y-axis */
    public double roll;
    /** Angle of counterclockwise rotation around the z-axis */
    public double yaw;


    public Vector3() {
        this(0, 0, 0);
    }

    public Vector3(double x, double y) {
        this(x, y, 0);
    }

    public Vector3(Vector2 vec) {
        this(vec.x, vec.y, 0);
    }

    public Vector3(double x, double y, double z) {
        this.x = this.roll = x;
        this.y = this.pitch = y;
        this.z = this.yaw = z;
    }

    /** Convert to Vector2 */
    public Vector2 asVector2() {
        return new Vector2(this);
    }

    /** @return this vector plus the other vector */
    public Vector3 plus(Vector3 other) {
        return new Vector3(this.x + other.x, this.y + other.y, this.z + other.z);
    }

    /** @return this vector minus the other vector */
    public Vector3 minus(Vector3 other) {
        return new Vector3(this.x - other.x, this.y - other.y, this.z - other.z);
    }

    /** @return the dot product of this and the other vector. */
    public double dot(Vector3 other) {
        return (this.x * other.x + this.y * other.y + this.z * other.z);
    }

    /** @return the cross vector of this vector and the other vector. A cross vector is orthogonal on both this and
     * the other vector (using the right-hand thumb rule). It's length is equal to the length of this vector times
     * the length of the other vector times Cosine(ang), where ang is the angle between this and the other vector */
    public Vector3 cross(Vector3 other) {
        double x = (this.y * other.z) - (this.z * other.y);
        double y = (this.z * other.x) - (this.x * other.z);
        double z = (this.x * other.y) - (this.y * other.x);
        return new Vector3(x, y, z);
    }

    /** @return a copy of this vector scaled by scalar. A scalar equal to -1 will give you a vector in the opposite direction. */
    public Vector3 scale(double scalar) {
        return new Vector3(this.x * scalar, this.y * scalar, this.z * scalar);
    }

    /** @return the magnitude (length) of this vector squared. Sometimes you don't have to find the square root, then this is faster. */
    public double getMagnitudeSqr() {
        return (Math.pow(this.x, 2) + Math.pow(this.y, 2) + Math.pow(this.z, 2));
    }

    /** @return the magnitude (length) of this vector. */
    public double getMagnitude() {
        return Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.y, 2) + Math.pow(this.z, 2));
    }

    /** @return the distance to another vector squared. Sometimes you don't have to find the square root, then this is faster. */
    public double getDistanceToSqr(Vector3 vector){
        return this.minus(vector).getMagnitudeSqr();
    }

    /** @return the distance to another vector */
    public double getDistanceTo(Vector3 vector){
        return this.minus(vector).getMagnitude();
    }

    /** @return the angle to another vector */
    public double getAngleTo(Vector3 other){
        return Math.acos((this.dot(other)) / (this.getMagnitude() * other.getMagnitude()));
    }

    /** @return the projection vector. This projected onto other. */
    public Vector3 getProjectionOnto(Vector3 other){
        return other.scale(((other.dot(this)) / Math.pow(other.getMagnitude(), 2)));
    }

    /** @return a vector with the same direction, but a length of one. If this is a zero vector, this returns a new zero vector. */
    public Vector3 getNormalized() {
        if (isZero()) return new Vector3();
        return scale(1.0 / getMagnitude());
    }

    /** @return whether this vector has a magnitude (length) of zero (all components are zero) */
    public boolean isZero() {
        return x == 0 && y == 0 && z == 0;
    }

    /** Linearly interpolate from {@code this} to {@code other} with time {@code t}, such that {@code t = 0} will return
     * {@code this} and {@code t = 1} will return {@code other}.
     * @return a Vector3 that is linearly interpolated from {@code this} to {@code other} with time {@code t}.*/
    public Vector3 lerpTo(Vector3 other, double time) {
        return lerp(this, other, time);
    }

    /** Compare two vectors.
     * @return whether the vectors are identical. */
    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;

        Vector3 that = (Vector3) other;

        return this.minus(that).isZero();
    }

    /** Generate a hash for this vector. */
    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

    /** @return "Vec3(x, y, z)" */
    @Override
    public String toString() {
        return String.format("Vec3(" + x + ", " + y + ", " + z + ")");
    }

    /** @return "(x, y, z)" where each number is formatted "% 7.2f"*/
    public String toStringFixedSize() {
        return String.format("(% 7.2f, % 7.2f, % 7.2f)", x, y, z);
    }

    /** Convert from GameData to our Vector3. */
    public static Vector3 convert(GameData.Vector3 vec) {
        return new Vector3(vec.getX(), vec.getY(), vec.getZ());
    }

    /** Convert from a GameData Rotator */
    public static Vector3 convert(GameData.Rotator rot) {
        return new Vector3(rot.getRoll(), rot.getPitch(), rot.getYaw());
    }

    /** Linearly interpolate from {@code A} to {@code B} with time {@code t}, such that {@code t = 0} will return
     * {@code A} and {@code t = 1} will return {@code B}.
     * @return a Vector3 that is linearly interpolated from {@code A} to {@code B} with time {@code t}.*/
    public static Vector3 lerp(Vector3 A, Vector3 B, double t) {
        return new Vector3(
                RLMath.lerp(A.x, B.x, t),
                RLMath.lerp(A.y, B.y, t),
                RLMath.lerp(A.z, B.z, t)
        );
    }
}

