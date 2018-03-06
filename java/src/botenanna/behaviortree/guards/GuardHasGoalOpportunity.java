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
        double RightGoalPost = RLMath.carsAngleToPoint(new Vector2(input.myLocation), input.myRotation.yaw, new Vector2(770,5200));
        double LeftGoalPost = RLMath.carsAngleToPoint(new Vector2(input.myLocation), input.myRotation.yaw, new Vector2(-770,5200));
        if(input.myLocation.x <= 771 && input.myLocation.x >= -771 && input.ballLocation.x <= 771 && input.ballLocation.x >= -771)
            return NodeStatus.DEFAULT_SUCCESS;
        if(input.angleToBall < 0.5 && input.angleToBall > -0.5 && RightGoalPost < 0.5 && LeftGoalPost > -0.5)
            return NodeStatus.DEFAULT_SUCCESS;

        return NodeStatus.DEFAULT_FAILURE;
        //input.ballLocation;
        //input.angleToBall;
        //input.enemyTeam;
        //input.myRotation;
        //input.getGoalDirection(input.enemyPlayerIndex); //Blue is Minus, Red is Plus
    }
}
