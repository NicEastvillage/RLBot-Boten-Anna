package tarehart.rlbot.steps.strikes;

import tarehart.rlbot.math.vector.Vector3;
import tarehart.rlbot.math.SpaceTimeVelocity;
import tarehart.rlbot.physics.BallPath;
import tarehart.rlbot.physics.DistancePlot;

public class DirectedKickPlan {
    public BallPath ballPath;
    public DistancePlot distancePlot;
    public SpaceTimeVelocity ballAtIntercept;
    public Vector3 interceptModifier;
    public Vector3 desiredBallVelocity;
    public Vector3 plannedKickForce;

    public Vector3 getCarPositionAtIntercept() {
        return ballAtIntercept.getSpace().plus(interceptModifier);
    }
}
