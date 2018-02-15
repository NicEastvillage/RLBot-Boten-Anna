package botenanna.math;

public class Vector3 {

    public static final Vector3 UP = new Vector3(0, 0, 1);
    public static final Vector3 RIGHT = new Vector3(1, 0, 0);
    public static final Vector3 FORWARD = new Vector3(0, 1, 0);
    public static final Vector3 BACKWARDS = new Vector3(0,-1,0);
    public static final Vector3 LEFT = new Vector3(-1,0,0);
    public static final Vector3 DOWN = new Vector3(0,0,-1);

    public final double x;
    public final double y;
    public final double z;

    public Vector3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3 plus(Vector3 other) {
        return new Vector3(this.x + other.x, this.y + other.y, this.z + other.z);
    }

    public Vector3 minus(Vector3 other) {
        return new Vector3(this.x - other.x, this.y - other.y, this.z - other.z);
    }

    public double dot(Vector3 other) {
        return (this.x * other.x + this.y * other.y + this.z * other.z);
    }

    public Vector3 cross(Vector3 other) {
        double x = (this.y * other.z) - (this.z * other.y);
        double y = (this.x * other.z) - (this.z * other.x);
        double z = (this.x * other.y) - (this.y * other.x);
        return new Vector3( x, y, z);
    }

    public Vector3 scale(double scalar) {
        return new Vector3(this.x * scalar, this.y * scalar, this.z * scalar);
    }

    public double getMagnitudeSqr() {
        return (Math.pow(this.x, 2) + Math.pow(this.y, 2) + Math.pow(this.z, 2));
    }

    public double getMagnitude() {
        return Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.y, 2) + Math.pow(this.z, 2));
    }

    public Vector3 getNormalized(double scalar) {
        return scale( 1.0 / getMagnitude() );
    }

}

