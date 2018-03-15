package botenanna.behaviortree.tasks;

import botenanna.AgentInput;
import botenanna.AgentOutput;
import botenanna.ArgumentTranslator;
import botenanna.behaviortree.*;
import botenanna.math.RLMath;
import botenanna.math.Vector2;
import botenanna.math.Vector3;

import java.util.function.Function;

public class TaskBallTowardsGoal extends Leaf {

    // private Function<AgentInput, Object> pointFunc;

    /** The TaskGoTowardsPoint is the simple version of going to a specific point.
     * In the current version the agent won’t slide and it will overshoot the point.
     *
     * It's signature is {@code TaskGoTowardsPoint <point:Vector3>} */
    public TaskBallTowardsGoal(String[] arguments) throws IllegalArgumentException {
        super(arguments);

        //if (arguments.length != 0) {
       //     throw new IllegalArgumentException();
      //  }

       // pointFunc = ArgumentTranslator.get(arguments[0]);
    }

    @Override
    public void reset() {
        // Irrelevant
    }

    @Override
    public NodeStatus run(AgentInput input) throws MissingNodeException {
        // TODO For now we always to full throttle forwards, though that not be the shortest route. Maybe we should slide in some cases?
        // TODO Also, the bot will overshoot. In some cases we want the bot to stop, or get to pointFunc at a specific time (e.g. when ball lands)

        Vector3 expectedBallLocation = input.ballLocation.plus(input.ballVelocity.scale(0.4));

        Vector2 ballToRightGoalPostVector = new Vector2(0,0);
        Vector2 ballToLeftGoalPostVector = new Vector2(0,0);

        if (input.myTeam == 1) {
            // Creates Vector needed to adjust shooting depended on left and right goal post
            ballToRightGoalPostVector = AgentInput.BLUE_GOALPOST_RIGHT.minus(expectedBallLocation.asVector2());
            ballToRightGoalPostVector = ballToRightGoalPostVector.getNormalized();
            ballToRightGoalPostVector = ballToRightGoalPostVector.scale(-82);
            ballToRightGoalPostVector = ballToRightGoalPostVector.plus(expectedBallLocation.asVector2());

            // Creates Vector needed to adjust shooting depended on left and right goal post
            ballToLeftGoalPostVector = AgentInput.BLUE_GOALPOST_LEFT.minus(expectedBallLocation.asVector2());
            ballToLeftGoalPostVector = ballToLeftGoalPostVector.getNormalized();
            ballToLeftGoalPostVector = ballToLeftGoalPostVector.scale(-82);
            ballToLeftGoalPostVector = ballToLeftGoalPostVector.plus(expectedBallLocation.asVector2());
        }

        else if (input.myTeam == 0) {
            // Creates Vector needed to adjust shooting depended on left and right goal post
            ballToRightGoalPostVector = AgentInput.RED_GOALPOST_RIGHT.minus(expectedBallLocation.asVector2());
            ballToRightGoalPostVector = ballToRightGoalPostVector.getNormalized();
            ballToRightGoalPostVector = ballToRightGoalPostVector.scale(-82);
            ballToRightGoalPostVector = ballToRightGoalPostVector.plus(expectedBallLocation.asVector2());

            // Creates Vector needed to adjust shooting depended on left and right goal post
            ballToLeftGoalPostVector = AgentInput.RED_GOALPOST_LEFT.minus(expectedBallLocation.asVector2());
            ballToLeftGoalPostVector = ballToLeftGoalPostVector.getNormalized();
            ballToLeftGoalPostVector = ballToLeftGoalPostVector.scale(-82);
            ballToLeftGoalPostVector = ballToLeftGoalPostVector.plus(expectedBallLocation.asVector2());
        }

        // Get the needed positions and rotations
        Vector3 myPos = input.myLocation;
        Vector3 myRotation = input.myRotation;

        double ang = 0;

        if(input.myTeam == 1) {
            // Statements to determine where the agent should hit the ball
            if (AgentInput.BLUE_GOALPOST_RIGHT.minus(myPos.asVector2()).getMagnitude() > AgentInput.BLUE_GOALPOST_LEFT.minus(myPos.asVector2()).getMagnitude()) {
                ang = RLMath.carsAngleToPoint(myPos.asVector2(), myRotation.yaw, ballToRightGoalPostVector);
            } else {
                ang = RLMath.carsAngleToPoint(myPos.asVector2(), myRotation.yaw, ballToLeftGoalPostVector);
            }
        }

        else if(input.myTeam == 0) {
            // Statements to determine where the agent should hit the ball
            if (AgentInput.RED_GOALPOST_RIGHT.minus(myPos.asVector2()).getMagnitude() > AgentInput.RED_GOALPOST_LEFT.minus(myPos.asVector2()).getMagnitude()) {
                ang = RLMath.carsAngleToPoint(myPos.asVector2(), myRotation.yaw, ballToRightGoalPostVector);
            } else {
                ang = RLMath.carsAngleToPoint(myPos.asVector2(), myRotation.yaw, ballToLeftGoalPostVector);
            }
        }

        // Smooth the angle to a steering amount - this avoids wobbling
        double steering = RLMath.steeringSmooth(ang);

        boolean boost = false;

        if(expectedBallLocation.asVector2().minus(myPos.asVector2()).getMagnitude() < 500 && 0.5 > input.angleToBall && input.angleToBall > -0.5) {
            boost = true;
        }

        return new NodeStatus(Status.RUNNING, new AgentOutput().withAcceleration(1).withSteer(steering).withBoost(boost), this);
    }
}
