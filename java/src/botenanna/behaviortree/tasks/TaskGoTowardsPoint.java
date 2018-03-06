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

    public static final double SLIDE_ANGLE = 1.7;

    private Function<AgentInput, Object> pointFunc;

    /** <p>The TaskGoTowardsPoint is the simple version of going to a specific point.
     * By default the agent will slide if the angle to the point is too high. This can be toggled through arguments
     * so the agent never slides.</p>
     *
     * <p>NOTE: The agent will overshoot the point.</p>
     *
     * <p>It's signature is {@code TaskGoTowardsPoint <point:Vector3> [allowSlide:boolean]}</p>*/
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

        // Get the needed positions and rotations
        Vector3 myPos = input.myLocation;
        Vector3 myRotation = input.myRotation;
        Vector3 point = (Vector3) pointFunc.apply(input);

        double ang = RLMath.carsAngleToPoint(myPos.asVector2(), myRotation.yaw, point.asVector2());

        // Smooth the angle to a steering amount - this avoids wobbling
        double steering = RLMath.steeringSmooth(ang);

        AgentOutput outout = new AgentOutput().withAcceleration(1).withSteer(steering);

        // Do slide for sharp turning
        if (ang > SLIDE_ANGLE || ang < -SLIDE_ANGLE) {
            outout.withSlide();
        }

        return new NodeStatus(Status.RUNNING, outout, this);
    }
}
