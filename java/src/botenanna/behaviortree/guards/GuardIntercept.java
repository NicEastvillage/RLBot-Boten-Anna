package botenanna.behaviortree.guards;

import botenanna.AgentInput;
import botenanna.behaviortree.Leaf;
import botenanna.behaviortree.MissingNodeException;
import botenanna.behaviortree.NodeStatus;
import botenanna.math.RLMath;
import botenanna.math.Vector2;

public class GuardIntercept extends Leaf {


    public GuardIntercept(String[] arguments) throws IllegalArgumentException {
        super(arguments);
        if (arguments.length != 0) throw new IllegalArgumentException();
    }
    @Override
    public void reset() {

    }

    @Override
    public NodeStatus run(AgentInput input) throws MissingNodeException {

        if (input.myLocation.getDistanceTo(input.getGoalBox(input.myPlayerIndex))<input.ballLocation.getDistanceTo(input.getGoalBox(input.myPlayerIndex))) {
            return NodeStatus.DEFAULT_SUCCESS;
        }
        return NodeStatus.DEFAULT_FAILURE;
    }
}
