package botenanna.behaviortree.tasks;

import botenanna.AgentInput;
import botenanna.AgentOutput;
import botenanna.behaviortree.*;
import botenanna.math.RLMath;
import botenanna.math.Vector2;
import botenanna.math.Vector3;

public class TaskBallTowardsGoal extends Leaf {

    /**<p>Make the agent try to shoot the ball towards the enemy goal, if possible. The agent will simulate how much into the future it should predict.
     *This way the agent will be able to predict and hit the ball towards the opponents goal.</p>
     *
     * <p> The agent will always try to drive towards a vector point that should be able to shoot the ball towards goal,
     * because of this, the agent needs to be at the correct position relative to the ball, else the agent can shoot
     * the ball towards its own goal.</p>
     *
     * <p>It's signature is {@code TaskBallTowardsGoal}</p>*/

    public TaskBallTowardsGoal(String[] arguments) throws IllegalArgumentException {
        super(arguments);

        // Takes no arguments
        if (arguments.length != 0) throw new IllegalArgumentException();
    }

    @Override
    public void reset() {
        // Irrelevant
    }

    @Override
    public NodeStatus run(AgentInput input) throws MissingNodeException {

        //TODO: The agent is only trying to shoot the ball towards the middle of opponents goal, there are commented math for right and left side of the goal
        //TODO: Do so the agent shoots towards the "easisest" place in the goal.
        /*
        Vector3 expectedBall;
        double predictSeconds = 0;
        double predict = 0.02;
        double counter = 0.02;
        double velocity;
        boolean isBallStill = false;

        //If the ball is really slow or still, skip the loop and don't predict.
        if(10 > input.ballVelocity.getMagnitude()){
            isBallStill = true;
        }

        //The loop will find a spot where the distance of expected ball to car minus the carvelocity multiplied by predict is between -25 and 25.
        //That way the agent should always be able to choose the right amount of prediction seconds, although this will probably change a little bit every tick as
        //the carvelocity changes.
        while(predictSeconds < 0.1 && counter <= 5 && !isBallStill){
            expectedBall = input.ballLocation.plus(input.ballVelocity.scale(predict));

            // If the car is not really driving, it should overextend its prediction to the future.
            if (input.myVelocity.getMagnitude() < 800){
                velocity = 800;
            }
            else velocity = input.myVelocity.getMagnitude();

            if (-25 < expectedBall.minus(input.myPosition.plus(input.myFrontVector.scale(70))).getMagnitude() - velocity*predict && expectedBall.minus(input.myPosition.plus(input.myFrontVector.scale(70))).getMagnitude() - velocity*predict < 25) {
                predictSeconds = predict;
            }

            predict += 0.02;
            counter += 0.02;
        }

        // If it runs through loop without choosing one, then don't predict (Probably not needed)
        if(counter > 5) {
            predictSeconds = 0;
        }

        // if ball is still, don't predict
        if (isBallStill){
            predictSeconds = 0;
        }*/
        double predictSeconds = input.getCollisionTime();
        Vector3 expectedBallLocation = input.ball.getPosition().plus(input.ball.getVelocity().scale(predictSeconds));

        //Vector2 ballToRightGoalPostVector = new Vector2(0,0);
        //Vector2 ballToLeftGoalPostVector = new Vector2(0,0);
        //Vector2 rightGoalPost = new Vector2(0,0);
        //Vector2 leftGoalPost = new Vector2(0,0);
        Vector2 middleOfGoal;

        if (input.myCar.team == 1) {
            //ballToRightGoalPostVector = AgentInput.BLUE_GOALPOST_RIGHT.minus(expectedBallLocation.asVector2());
            //ballToLeftGoalPostVector = AgentInput.BLUE_GOALPOST_LEFT.minus(expectedBallLocation.asVector2());
            middleOfGoal = new Vector2(0,-5200);
            //rightGoalPost = AgentInput.BLUE_GOALPOST_RIGHT;
            //leftGoalPost = AgentInput.BLUE_GOALPOST_LEFT;
        }
        else {
            //ballToRightGoalPostVector = AgentInput.RED_GOALPOST_RIGHT.minus(expectedBallLocation.asVector2());
            //ballToLeftGoalPostVector = AgentInput.RED_GOALPOST_LEFT.minus(expectedBallLocation.asVector2());
            //rightGoalPost = AgentInput.RED_GOALPOST_RIGHT;
            //leftGoalPost = AgentInput.RED_GOALPOST_LEFT;
            middleOfGoal = new Vector2(0,5200);
        }

            middleOfGoal = middleOfGoal.minus(expectedBallLocation.asVector2());

            // Creates Vector needed to adjust shooting depended on left and right goal post
            //ballToRightGoalPostVector = ballToRightGoalPostVector.getNormalized();
            //ballToRightGoalPostVector = ballToRightGoalPostVector.scale(-80);
            //ballToRightGoalPostVector = ballToRightGoalPostVector.plus(expectedBallLocation.asVector2());

            // Creates Vector needed to adjust shooting depended on left and right goal post
            //ballToLeftGoalPostVector = ballToLeftGoalPostVector.getNormalized();
            //ballToLeftGoalPostVector = ballToLeftGoalPostVector.scale(-80);
            //ballToLeftGoalPostVector = ballToLeftGoalPostVector.plus(expectedBallLocation.asVector2());

            middleOfGoal = middleOfGoal.getNormalized();
            middleOfGoal = middleOfGoal.scale(-80);
            middleOfGoal = middleOfGoal.plus(expectedBallLocation.asVector2());


        // Get the needed positions and rotations
        Vector3 myPos = input.myCar.position.plus(input.myCar.frontVector.scale(70));
        Vector3 myRotation = input.myCar.position;

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

        if(800 > expectedBallLocation.asVector2().minus(myPos.asVector2()).getMagnitude() && 1.5 > input.myCar.angleToBall && input.myCar.angleToBall > -1.5) {
            boost = true;
        }

        return new NodeStatus(Status.RUNNING, new AgentOutput().withAcceleration(1).withSteer(steering).withBoost(boost), this);
    }
}
