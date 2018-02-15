package botenanna.math;

import rlbot.api.GameData;

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

    public Vector3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
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
        double y = (this.x * other.z) - (this.z * other.x);
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

    /** @return a vector with the same direction, but a length of one. */
    public Vector3 getNormalized(double scalar) {
        return scale(1.0 / getMagnitude());
    }

    /** Convert a vector from GameData to our Vector3. */
    public static Vector3 convert(GameData.Vector3 vec) {
        return new Vector3(vec.getX(), vec.getY(), vec.getZ());
    }
}

