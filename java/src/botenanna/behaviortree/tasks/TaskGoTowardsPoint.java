package botenanna.behaviortree.tasks;

import botenanna.Situation;
import botenanna.Actions;
import botenanna.ArgumentTranslator;
import botenanna.behaviortree.*;
import botenanna.math.RLMath;
import botenanna.math.Vector3;

import java.util.function.Function;

public class TaskGoTowardsPoint extends Leaf {

    public static final double SLIDE_ANGLE = 1.7;

    private Function<Situation, Object> pointFunc;
    private boolean allowSlide = true;
    private boolean useBoost = false;

    /** <p>The TaskGoTowardsPoint is the simple version of going to a specific point.
     * By default the agent will slide if the angle to the point is too high. This can be toggled through arguments
     * so the agent never slides.
     * By default this will be done without boost. </p>
     *
     * <p>NOTE: The agent will overshoot the point.</p>
     *
     * <p>It's signature is {@code TaskGoTowardsPoint <point:Vector3> [allowSlide:BOOLEAN] [useBoost:BOOLEAN]}</p>*/
    public TaskGoTowardsPoint(String[] arguments) throws IllegalArgumentException {
        super(arguments);

        if (arguments.length == 0 || arguments.length > 3) {
            throw new IllegalArgumentException();
        }

        pointFunc = ArgumentTranslator.get(arguments[0]);

        if (arguments.length >= 2) {
            allowSlide = Boolean.parseBoolean(arguments[1]);
        }

        if(arguments.length == 3){
            useBoost = Boolean.parseBoolean(arguments[2]);
        }
    }

    @Override
    public void reset() {
        // Irrelevant
    }

    @Override
    public NodeStatus run(Situation input) throws MissingNodeException {

        // Get the needed positions and rotations
        Vector3 myPos = input.myCar.position;
        Vector3 myRotation = input.myCar.rotation;
        Vector3 point = (Vector3) pointFunc.apply(input);

        double ang = RLMath.carsAngleToPoint(myPos.asVector2(), myRotation.yaw, point.asVector2());

        // Smooth the angle to a steering amount - this avoids wobbling
        double steering = RLMath.steeringSmooth(ang);

        Actions output;

        if(useBoost)
            output = new Actions().withAcceleration(1).withSteer(steering).withBoost();
        else
            output = new Actions().withAcceleration(1).withSteer(steering);

        if (allowSlide) {
            // Do slide for sharp turning
            if (ang > SLIDE_ANGLE || ang < -SLIDE_ANGLE) {
                output.withSlide();
            }
        }

        return new NodeStatus(Status.RUNNING, output, this);
    }
}
