package botenanna.behaviortree.tasks;

import botenanna.AgentInput;
import botenanna.AgentOutput;
import botenanna.ArgumentTranslator;
import botenanna.behaviortree.*;
import botenanna.math.RLMath;
import botenanna.math.Vector2;
import botenanna.math.Vector3;
import sun.management.Agent;

import java.util.function.Function;

public class TaskBallTowardsGoal extends Leaf {

    /** The TaskGoTowardsPoint is the simple version of going to a specific point.
     * In the current version the agent won’t slide and it will overshoot the point.
     *
     * It's signature is {@code TaskGoTowardsPoint <point:Vector3>} */
    public TaskBallTowardsGoal(String[] arguments) throws IllegalArgumentException {
        super(arguments);
    }

    @Override
    public void reset() {
        // Irrelevant
    }

    @Override
    public NodeStatus run(AgentInput input) throws MissingNodeException {

        Vector3 expectedBall;
        double predictSeconds = 0;
        double predict = 0.02;
        double counter = 0.02;
        double velocity;
        boolean isBallStill = false;

        if(10 > input.ballVelocity.getMagnitude()){
            isBallStill = true;
        }

        while(predictSeconds < 0.1 && counter <= 5 && !isBallStill){
            expectedBall = input.ballLocation.plus(input.ballVelocity.scale(predict));

            if (input.myVelocity.getMagnitude() < 800){
                velocity = 800;
            }
            else velocity = input.myVelocity.getMagnitude();

            if (-25 < expectedBall.minus(input.myLocation.plus(input.myFrontVector.scale(70))).getMagnitude() - velocity*predict && expectedBall.minus(input.myLocation).getMagnitude() - velocity*predict < 25) {
                predictSeconds = predict;
            }

            predict += 0.02;
            counter += 0.02;
        }

        if(counter > 5){
            predictSeconds = 0;
        }

        if (isBallStill){
            predictSeconds = 0;
        }

        Vector3 expectedBallLocation = input.ballLocation.plus(input.ballVelocity.scale(predictSeconds));

        Vector2 ballToRightGoalPostVector = new Vector2(0,0);
        Vector2 ballToLeftGoalPostVector = new Vector2(0,0);
        Vector2 rightGoalPost = new Vector2(0,0);
        Vector2 leftGoalPost = new Vector2(0,0);
        Vector2 middleOfGoal;

        if (input.myTeam == 1) {
            ballToRightGoalPostVector = AgentInput.BLUE_GOALPOST_RIGHT.minus(expectedBallLocation.asVector2());
            ballToLeftGoalPostVector = AgentInput.BLUE_GOALPOST_LEFT.minus(expectedBallLocation.asVector2());
            middleOfGoal = new Vector2(0,-5200);
            rightGoalPost = AgentInput.BLUE_GOALPOST_RIGHT;
            leftGoalPost = AgentInput.BLUE_GOALPOST_LEFT;
        }
        else {
            ballToRightGoalPostVector = AgentInput.RED_GOALPOST_RIGHT.minus(expectedBallLocation.asVector2());
            ballToLeftGoalPostVector = AgentInput.RED_GOALPOST_LEFT.minus(expectedBallLocation.asVector2());
            rightGoalPost = AgentInput.RED_GOALPOST_RIGHT;
            leftGoalPost = AgentInput.RED_GOALPOST_LEFT;
            middleOfGoal = new Vector2(0,5200);
        }

            middleOfGoal = middleOfGoal.minus(expectedBallLocation.asVector2());

            // Creates Vector needed to adjust shooting depended on left and right goal post
            ballToRightGoalPostVector = ballToRightGoalPostVector.getNormalized();
            ballToRightGoalPostVector = ballToRightGoalPostVector.scale(-80);
            ballToRightGoalPostVector = ballToRightGoalPostVector.plus(expectedBallLocation.asVector2());

            // Creates Vector needed to adjust shooting depended on left and right goal post
            ballToLeftGoalPostVector = ballToLeftGoalPostVector.getNormalized();
            ballToLeftGoalPostVector = ballToLeftGoalPostVector.scale(-80);
            ballToLeftGoalPostVector = ballToLeftGoalPostVector.plus(expectedBallLocation.asVector2());

            middleOfGoal = middleOfGoal.getNormalized();
            middleOfGoal = middleOfGoal.scale(-80);
            middleOfGoal = middleOfGoal.plus(expectedBallLocation.asVector2());


        // Get the needed positions and rotations
        Vector3 myPos = input.myLocation.plus(input.myFrontVector.scale(70));
        Vector3 myRotation = input.myRotation;

        double ang = 0;

        // Statements to determine where the agent should hit the ball
        //if (rightGoalPost.minus(myPos.asVector2()).getMagnitude() > leftGoalPost.minus(myPos.asVector2()).getMagnitude()) {
        //    ang = RLMath.carsAngleToPoint(myPos.asVector2(), myRotation.yaw, ballToRightGoalPostVector);
        //}
        //else {
        //    ang = RLMath.carsAngleToPoint(myPos.asVector2(), myRotation.yaw, ballToLeftGoalPostVector);
        //}

        ang = RLMath.carsAngleToPoint(myPos.asVector2(), myRotation.yaw, middleOfGoal);

        // Smooth the angle to a steering amount - this avoids wobbling
        double steering = RLMath.steeringSmooth(ang);


        //When the agent should boost
        boolean boost = false;

        if(800 > expectedBallLocation.asVector2().minus(myPos.asVector2()).getMagnitude() && 1.5 > input.angleToBall && input.angleToBall > -1.5) {
            boost = true;
        }

        return new NodeStatus(Status.RUNNING, new AgentOutput().withAcceleration(1).withSteer(steering).withBoost(boost), this);
    }
}
