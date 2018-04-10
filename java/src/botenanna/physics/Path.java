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
}
