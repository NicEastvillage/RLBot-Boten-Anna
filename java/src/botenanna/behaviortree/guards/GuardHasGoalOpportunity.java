package botenanna.behaviortree.guards;

import botenanna.AgentInput;
import botenanna.behaviortree.Leaf;
import botenanna.behaviortree.MissingNodeException;
import botenanna.behaviortree.NodeStatus;
import botenanna.math.RLMath;
import botenanna.math.Vector2;
import botenanna.math.Vector3;

public class GuardHasGoalOpportunity extends Leaf {
    /**<p>The guard will try to determine whether the agent has a goal opportunity or not.
     * If the ball and the agent is approximately in the middle of the y axis there is a goal opportunity.
     * There is also a goal opportunity if the agents angle is towards the ball and the goal at the same time.</p>
     *
     * <p>The agent also have to be at the correct side relatively to the ball, for a goal opportunity to appear.</p>
     *
     * <p>It's signature is {@code GuardHasGoalOpportunity}</p>*/

    public GuardHasGoalOpportunity(String[] arguments) throws IllegalArgumentException {
        super(arguments);

        // Takes no arguments
        if (arguments.length != 0) throw new IllegalArgumentException();
    }

    @Override
    public void reset() {
        //Irrelevant
    }

    // Calculating goal opportunities whether the agent is Team Orange or Team Blue.
    @Override
    public NodeStatus run(AgentInput input) throws MissingNodeException {

        double predict = 0;
        double predictSeconds = 0;
        Vector3 expectedBall;
        int variation = 500;
        Vector2 goalOpportunityLocation;

        if (input.myTeam == 1) {
            goalOpportunityLocation = new Vector2(0,-5200);
            goalOpportunityLocation = goalOpportunityLocation.minus(input.ballLocation.asVector2());
            goalOpportunityLocation = goalOpportunityLocation.getNormalized();
            goalOpportunityLocation = goalOpportunityLocation.scale(-80);
            //goalOpportunityLocation = goalOpportunityLocation.plus(expectedBallLocation.asVector2());
            //if (input.myLocation.x <= 900 && input.myLocation.x >= -900 && input.ballLocation.x <= 900 && input.ballLocation.x >= -900 && input.myLocation.y >= input.ballLocation.y)
            //    return NodeStatus.DEFAULT_SUCCESS;
            //if (input.angleToBall < 0.5 && input.angleToBall > -0.5 && RightGoalPost < 0.5 && LeftGoalPost > -0.5) {
            //    if (input.ballVelocity.getMagnitude() < 500 && input.myDistanceToBall < 2000) {
            //        return NodeStatus.DEFAULT_SUCCESS;
            //    }
            //
            double velocity;
            while (predict <= 7 && predictSeconds < 0.1) {
                expectedBall = input.ballLocation.plus(input.ballVelocity.scale(predict));
                if(input.myVelocity.getMagnitude() < 800){
                    velocity = 800;
                }
                else {
                    velocity = input.myVelocity.getMagnitude();
                }
                if (-variation < expectedBall.minus(input.myLocation.plus(input.myFrontVector.scale(70))).getMagnitude() - velocity * predict && expectedBall.minus(input.myLocation.plus(input.myFrontVector.scale(70))).getMagnitude() - velocity * predict < variation) {

                    expectedBall = input.ballLocation.plus(input.ballVelocity.scale(predict));
                    double angleToExpectedBall = RLMath.carsAngleToPoint(input.myLocation.asVector2(), input.myRotation.yaw, expectedBall.asVector2());

                    if (predictSeconds >= 0.1 && angleToExpectedBall < 1 && angleToExpectedBall > -1) {
                        return NodeStatus.DEFAULT_SUCCESS;
                    }

                    if (input.myLocation.x <= 900 && input.myLocation.x >= -900 && expectedBall.x <= 900 && expectedBall.x >= -900 && input.myLocation.y >= input.ballLocation.y) {
                        return NodeStatus.DEFAULT_SUCCESS;
                    }
                    predictSeconds = predict;
                }
                predict += 0.1;
            }
        }

        if (input.myTeam == 0) {
            //if (input.myLocation.x <= 900 && input.myLocation.x >= -900 && input.ballLocation.x <= 900 && input.ballLocation.x >= -900 && input.myLocation.y <= input.ballLocation.y)
            //    return NodeStatus.DEFAULT_SUCCESS;
            //if (input.angleToBall < 0.5 && input.angleToBall > -0.5 && RightGoalPost < 0.5 && LeftGoalPost > -0.5)
            //    return NodeStatus.DEFAULT_SUCCESS;

            double velocity;
            while (predict <= 7 && predictSeconds < 0.1) {
                expectedBall = input.ballLocation.plus(input.ballVelocity.scale(predict));
                if(input.myVelocity.getMagnitude() < 800){
                    velocity = 800;
                }
                else velocity = input.myVelocity.getMagnitude();
                if (-variation < expectedBall.minus(input.myLocation.plus(input.myFrontVector.scale(70))).getMagnitude() - velocity * predict && expectedBall.minus(input.myLocation.plus(input.myFrontVector.scale(70))).getMagnitude() - velocity * predict < variation) {

                    expectedBall = input.ballLocation.plus(input.ballVelocity.scale(predict));
                    double angleToExpectedBall = RLMath.carsAngleToPoint(input.myLocation.asVector2(), input.myRotation.yaw, expectedBall.asVector2());

                    if (predictSeconds >= 0.1 && angleToExpectedBall < 1 && angleToExpectedBall > -1) {
                        return NodeStatus.DEFAULT_SUCCESS;
                    }

                    if (predictSeconds >= 0.1 && input.myLocation.x <= 900 && input.myLocation.x >= -900 && expectedBall.x <= 900 && expectedBall.x >= -900 && input.myLocation.y <= input.ballLocation.y) {
                        return NodeStatus.DEFAULT_SUCCESS;
                    }
                    predictSeconds = predict;
                }
                predict += 0.1;
            }
        }
        return NodeStatus.DEFAULT_FAILURE;
    }
    }
