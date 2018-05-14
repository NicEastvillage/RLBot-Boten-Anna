package botenanna.physics;

import botenanna.math.Vector3;

/** A Path is essentially a sequence of points mapped to a time. The Path class allows you to iterate over this
 * sequence and will even fill in missing points by linearly interpolate the nearest known points. */
public class Path extends InterpolatedTimeLine<Vector3> {

    /** A Path is essentially a sequence of points mapped to a time. The Path class allows you to iterate over this
     * sequence and will even fill in missing points by linearly interpolate the nearest known points. */
    public Path() {
        super(Vector3::lerp);
    }

    /** A Path is essentially a sequence of points mapped to a time. This constructor inserts a point at time = 0. If
     * no more points are added, this will create a Path that never changes; An unchanging point though time moves. */
    public Path(Vector3 point) {
        super(Vector3::lerp);
        addTimeStep(0, point);
    }
}
