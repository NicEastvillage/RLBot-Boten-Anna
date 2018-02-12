package tarehart.rlbot.planning;

import tarehart.rlbot.math.vector.Vector2;
import tarehart.rlbot.math.vector.Vector3;
import tarehart.rlbot.AgentOutput;
import tarehart.rlbot.input.CarData;
import tarehart.rlbot.math.VectorUtil;

public class SteerPlan {

    public AgentOutput immediateSteer;
    public Vector2 waypoint;

    public SteerPlan(AgentOutput immediateSteer, Vector2 waypoint) {
        this.immediateSteer = immediateSteer;
        this.waypoint = waypoint;
    }

    public SteerPlan(CarData car, Vector3 position) {
        this.immediateSteer = SteerUtil.steerTowardGroundPosition(car, position);
        this.waypoint = position.flatten();
    }
}
