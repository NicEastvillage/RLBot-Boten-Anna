package botenanna.physics;

import botenanna.math.Vector3;

public class InterpolatedTimeLine {

    private double duration;
    private SteppedTimeLine<Vector3> points;

    public InterpolatedTimeLine(SteppedTimeLine<Vector3> points) {
        this.points = points;
        duration = points.getLastTime();
    }


}
