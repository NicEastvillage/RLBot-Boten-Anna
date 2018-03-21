package botenanna;

import botenanna.math.Vector3;
import botenanna.physics.TimeLine;

import java.util.ArrayList;

public class BallPath {

    private double duration;
    private TimeLine<Vector3> points;

    public BallPath(TimeLine<Vector3> points) {
        this.points = points;
        duration = points.lastTimeStampTime();
    }
}
