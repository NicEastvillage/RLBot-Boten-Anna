package botenanna.behaviortree.tasks;

import botenanna.AgentInput;
import botenanna.AgentOutput;
import botenanna.behaviortree.*;
import botenanna.math.RLMath;
import botenanna.math.Vector2;
import botenanna.math.Vector3;
import rlbot.api.GameData;

/** Leaf for going towards a specific point */
public class TaskGoTowardsPoint extends Leaf {

    /** Leaf for going towards a specific point */
    public TaskGoTowardsPoint(String[] arguments) {
        super(arguments);
    }

    @Override
    public void reset() {
        // Irrelevant
    }

    @Override
    public NodeStatus run(AgentInput input) throws MissingNodeException {
        // TODO For now we always to full throttle forwards, though that not be the shortest route. Maybe we should slide in some cases?
        // TODO Also, the bot will overshoot. In some cases we want the bot to stop, or get to point at a specific time (e.g. when ball lands)

        int playerIndex = input.myPlayerIndex;

        // Get the needed positions and rotations
        Vector3 myPos = input.myLocation;
        Vector3 myRotation = input.myRotation;
        Vector2 point = input.ballLocation.asVector2(); // TODO Go towards ball for now

        double ang = RLMath.carsAngleToPoint(myPos.asVector2(), myRotation.yaw, point);

        // Smooth the angle to a steering amount - this avoids wobbling
        double steering = RLMath.steeringSmooth(ang);

        return new NodeStatus(Status.RUNNING, new AgentOutput().withAcceleration(1).withSteer(steering), this);
    }
}
