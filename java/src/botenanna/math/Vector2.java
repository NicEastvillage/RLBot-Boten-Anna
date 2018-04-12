package botenanna.math;


import java.util.Objects;
import java.util.Vector;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

/** A vector with two components: x and y */
public class Vector2 {

    public static final Vector2 RIGHT = new Vector2(1,0);
    public static final Vector2 FORWARD = new Vector2(0,1);
    public static final Vector2 BACKWARDS = new Vector2(0,-1);
    public static final Vector2 LEFT = new Vector2(-1,0);

    public final double x;
    public final double y;

    public Vector2(){
        this(0, 0);
    }

    public Vector2(Vector3 vec) {
        this(vec.x, vec.y);
    }

    public Vector2(double x, double y){
        this.x = x;
        this.y = y;
    }

    /** Convert to Vector3 */
    public Vector3 asVector3() {
        return new Vector3(this);
    }

    /** @return this vector plus the other vector */
    public Vector2 plus(Vector2 other){
        return new Vector2(this.x + other.x, this.y + other.y);
    }

    /** @return this vector minus the other vector */
    public Vector2 minus(Vector2 other) {
        return new Vector2(this.x - other.x, this.y - other.y);
    }

    /** @return the dot product of this and the other vector. */
    public double dot(Vector2 other) {
        return (this.x * other.x + this.y * other.y);
    }

    /** @return a copy of this vector scaled by scalar. A scalar equal to -1 will give you a vector in the opposite direction. */
    public Vector2 scale(double scalar) {
        return new Vector2(this.x * scalar, this.y * scalar);
    }

    /** @return the magnitude (length) of this vector squared. Sometimes you don't have to find the square root, then this is faster. */
    public double getMagnitudeSqr() {
        return (Math.pow(this.x, 2) + Math.pow(this.y, 2));
    }

    /** @return the magnitude (length) of this vector. */
    public double getMagnitude() {
        return Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.y, 2));
    }

    /** @return the distance to another vector squared. Sometimes you don't have to find the square root, then this is faster. */
    public double getDistanceToSqr(Vector2 vector){
        return this.minus(vector).getMagnitudeSqr();
    }

    /** @return the distance to another vector */
    public double getDistanceTo(Vector2 vector) {
        return this.minus(vector).getMagnitude();
    }

    /** @return a vector with the same direction, but a length of one. If this is a zero vector, this returns a new zero vector. */
    public Vector2 getNormalized() {
        if (isZero()) return new Vector2();
        return scale(1.0 / getMagnitude());
    }

    /** @return whether this vector has a magnitude (length) of zero (all components are zero) */
    public boolean isZero() {
        return x == 0 && y == 0;
    }

    /** Linearly interpolate from {@code this} to {@code other} with time {@code t}, such that {@code t = 0} will return
     * {@code this} and {@code t = 1} will return {@code other}.
     * @return a Vector3 that is linearly interpolated from {@code this} to {@code other} with time {@code t}.*/
    public Vector2 lerp(Vector2 other, double time) {
        return lerp(this, other, time);
    }

    /** Compare two vectors.
     * @return whether the vectors are identical. */
    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;

        Vector2 that = (Vector2) other;

        return this.minus(that).isZero();
    }

    /** Turns a vector radians
     * @return a turned vector2 */
    public Vector2 turn(double inputRadians){
        return  new Vector2(x*cos(inputRadians)-y*sin(inputRadians),x*sin(inputRadians)+y*cos(inputRadians));
    }

    /** Generate a hash for this vector. */
    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    /** @return "Vec2(x, y)" */
    @Override
    public String toString() {
        return String.format("Vec2(" + x + ", " + y + ")");
    }

    /** Linearly interpolate from {@code A} to {@code B} with time {@code t}, such that {@code t = 0} will return
     * {@code A} and {@code t = 1} will return {@code B}.
     * @return a Vector3 that is linearly interpolated from {@code A} to {@code B} with time {@code t}.*/
    public static Vector2 lerp(Vector2 A, Vector2 B, double t) {
        return new Vector2(
                RLMath.lerp(A.x, B.x, t),
                RLMath.lerp(A.y, B.y, t)
        );
    }
}
