package botenanna.behaviortree.guards;

import botenanna.AgentInput;
import botenanna.behaviortree.Leaf;
import botenanna.behaviortree.MissingNodeException;
import botenanna.behaviortree.NodeStatus;
import botenanna.math.RLMath;
import botenanna.math.Vector2;

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
        if(input.myTeam == 1) {
            double RightGoalPost = RLMath.carsAngleToPoint(new Vector2(input.myLocation), input.myRotation.yaw, AgentInput.BLUE_GOALPOST_RIGHT);
            double LeftGoalPost = RLMath.carsAngleToPoint(new Vector2(input.myLocation), input.myRotation.yaw, AgentInput.BLUE_GOALPOST_LEFT);
            if (input.myLocation.x <= 900 && input.myLocation.x >= -900 && input.ballLocation.x <= 900 && input.ballLocation.x >= -900 && input.myLocation.y >= input.ballLocation.y)
                return NodeStatus.DEFAULT_SUCCESS;
            if (input.angleToBall < 0.5 && input.angleToBall > -0.5 && RightGoalPost < 0.5 && LeftGoalPost > -0.5)
                return NodeStatus.DEFAULT_SUCCESS;
        }

        if(input.myTeam == 0) {
            double RightGoalPost = RLMath.carsAngleToPoint(new Vector2(input.myLocation), input.myRotation.yaw, AgentInput.RED_GOALPOST_RIGHT);
            double LeftGoalPost = RLMath.carsAngleToPoint(new Vector2(input.myLocation), input.myRotation.yaw, AgentInput.RED_GOALPOST_LEFT);
            if (input.myLocation.x <= 900 && input.myLocation.x >= -900 && input.ballLocation.x <= 900 && input.ballLocation.x >= -900 && input.myLocation.y <= input.ballLocation.y)
                return NodeStatus.DEFAULT_SUCCESS;
            if (input.angleToBall < 0.5 && input.angleToBall > -0.5 && RightGoalPost < 0.5 && LeftGoalPost > -0.5)
                return NodeStatus.DEFAULT_SUCCESS;
        }

        return NodeStatus.DEFAULT_FAILURE;
    }
}
