package botenanna.behaviortree.tasks;

import botenanna.AgentInput;
import botenanna.AgentOutput;
import botenanna.ArgumentTranslator;
import botenanna.behaviortree.*;
import botenanna.math.RLMath;
import botenanna.math.Vector2;
import botenanna.math.Vector3;

import java.util.function.Function;

public class TaskGoTowardsPoint extends Leaf {

    private Function<AgentInput, Object> pointFunc;

    /** The TaskGoTowardsPoint is the simple version of going to a specific point.
     * In the current version the agent won’t slide and it will overshoot the point.
     *
     * It's signature is {@code TaskGoTowardsPoint <point:Vector3>} */
    public TaskGoTowardsPoint(String[] arguments) throws IllegalArgumentException {
        super(arguments);

        if (arguments.length != 1) {
            throw new IllegalArgumentException();
        }

        pointFunc = ArgumentTranslator.get(arguments[0]);
    }

    @Override
    public void reset() {
        // Irrelevant
    }

    @Override
    public NodeStatus run(AgentInput input) throws MissingNodeException {
        // TODO For now we always to full throttle forwards, though that not be the shortest route. Maybe we should slide in some cases?
        // TODO Also, the bot will overshoot. In some cases we want the bot to stop, or get to pointFunc at a specific time (e.g. when ball lands)

        int playerIndex = input.myPlayerIndex;

        // Get the needed positions and rotations
        Vector3 myPos = input.myLocation;
        Vector3 myRotation = input.myRotation;
        Vector3 point = (Vector3) pointFunc.apply(input);

        double ang = RLMath.carsAngleToPoint(myPos.asVector2(), myRotation.yaw, point.asVector2());

        // Smooth the angle to a steering amount - this avoids wobbling
        double steering = RLMath.steeringSmooth(ang);

        return new NodeStatus(Status.RUNNING, new AgentOutput().withAcceleration(1).withSteer(steering), this);
    }
}
