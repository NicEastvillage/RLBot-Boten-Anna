package botenanna.behaviortree.tasks;

import botenanna.game.Situation;
import botenanna.game.Actions;
import botenanna.ArgumentTranslator;
import botenanna.behaviortree.Leaf;
import botenanna.behaviortree.MissingNodeException;
import botenanna.behaviortree.NodeStatus;
import botenanna.behaviortree.Status;
import botenanna.math.RLMath;
import botenanna.math.Vector2;
import botenanna.math.Vector3;

import java.util.function.Function;

public class TaskAdjustAirRotation extends Leaf {

    private static final double ROTATION_STRENGTH = 0.5;
    private static final double ACCEPTABLE_ANGLE = 0.3;

    private boolean shouldFace = false;
    private Function<Situation, Object> faceFunc;

    /** <p>The TaskAdjustAirRotation will make the agent rotate so it lands on its wheels. It assumes the car is in the air.
     * The task can optionally be given a point, which it will try to adjust towards so the car lands facing that point.
     * However, it will always prioritize landing on the wheels, then facing the point.</p>
     * <p>Its signature is: {@code TaskAdjustAirRotation [facing:Vector3]}</p>
     * @see botenanna.behaviortree.guards.GuardIsMidAir*/
    public TaskAdjustAirRotation(String[] arguments) throws IllegalArgumentException {
        super(arguments);

        // Check arguments
        if (arguments.length > 1) throw new IllegalArgumentException();
        if (arguments.length == 1) {
            // What to face
            shouldFace = true;
            faceFunc = ArgumentTranslator.get(arguments[0]);
        }
    }

    @Override
    public void reset() {
        // Irrelevant
    }

    @Override
    public NodeStatus run(Situation input) throws MissingNodeException {
        Actions out = new Actions();

        Vector3 myRot = input.myCar.rotation;

        double smoothPitch = RLMath.steeringSmooth(-myRot.pitch * ROTATION_STRENGTH);
        out.withPitch(smoothPitch);

        // It is not possible to adjust both roll and yaw at the same time
        // If we just want to land on the wheels, we only need to adjust roll
        // We adjust yaw and facing last, because we prioritize landing on the wheels

        if (shouldFace && -ACCEPTABLE_ANGLE < myRot.roll && myRot.roll < ACCEPTABLE_ANGLE) {
            Vector2 target = ((Vector3) faceFunc.apply(input)).asVector2();
            double angleToPoint = RLMath.carsAngleToPoint(myRot.asVector2(), myRot.yaw, target);
            double smoothYaw = RLMath.steeringSmooth(angleToPoint * ROTATION_STRENGTH);
            // Adjust yaw by steering
            out.withSteer(smoothYaw);
        } else {
            double smoothRoll = RLMath.steeringSmooth(-myRot.roll * ROTATION_STRENGTH);
            out.withRoll(smoothRoll);
        }

        return new NodeStatus(Status.RUNNING, out, this);
    }
}
