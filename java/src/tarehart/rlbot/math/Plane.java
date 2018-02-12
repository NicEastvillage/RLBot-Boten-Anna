package tarehart.rlbot.math;

import tarehart.rlbot.math.vector.Vector3;

public class Plane {

    public Vector3 normal;
    public Vector3 position;

    public Plane(Vector3 normal, Vector3 position) {
        this.normal = normal;
        this.position = position;
    }
}
