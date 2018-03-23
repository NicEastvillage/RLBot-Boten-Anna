package botenanna.behaviortree.guards;

import botenanna.AgentInput;
import botenanna.behaviortree.Leaf;
import botenanna.behaviortree.MissingNodeException;
import botenanna.behaviortree.NodeStatus;
import botenanna.math.RLMath;
import botenanna.math.Vector2;

public class GuardIntercept extends Leaf {
    /** The GuardIntercept simply compares the balls y velocity with the goal direction and returns succes if the ball is headed in the direction of the goal*
     * Its signature is {@code GuardIntercept}*/

    public GuardIntercept(String[] arguments) throws IllegalArgumentException {
        super(arguments);
        if (arguments.length != 0) throw new IllegalArgumentException();
    }
    @Override
    public void reset() {

    }

    @Override
    public NodeStatus run(AgentInput input) throws MissingNodeException {

        if (input.ballVelocity.y*input.getGoalDirection(input.myPlayerIndex)>input.getGoalDirection(input.myPlayerIndex)) {
            return NodeStatus.DEFAULT_SUCCESS;
        }
        return NodeStatus.DEFAULT_FAILURE;
    }
}
