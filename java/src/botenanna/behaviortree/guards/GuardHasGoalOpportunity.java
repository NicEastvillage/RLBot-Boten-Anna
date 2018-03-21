package botenanna.behaviortree.guards;

import botenanna.AgentInput;
import botenanna.behaviortree.Leaf;
import botenanna.behaviortree.MissingNodeException;
import botenanna.behaviortree.NodeStatus;
import botenanna.math.RLMath;
import botenanna.math.Vector2;

public class GuardHasGoalOpportunity extends Leaf {
    public GuardHasGoalOpportunity(String[] arguments) throws IllegalArgumentException {
        super(arguments);
    }

    @Override
    public void reset() {
        //Irrelevant
    }

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
