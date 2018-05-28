package botenanna.behaviortree.guards;

import botenanna.game.Arena;
import botenanna.game.Situation;
import botenanna.behaviortree.Leaf;
import botenanna.behaviortree.MissingNodeException;
import botenanna.behaviortree.NodeStatus;

public class GuardIntercept extends Leaf {

    /** The GuardIntercept simply compares the balls y velocity with the goal direction and returns succes if the ball is headed in the direction of the goal
     *
     *  Its signature is {@code GuardIntercept}*/
    public GuardIntercept(String[] arguments) throws IllegalArgumentException {
        super(arguments);

        // Takes no arguments
        if (arguments.length != 0) throw new IllegalArgumentException();
    }

    @Override
    public void reset() {
        // Irrelevant
    }

    @Override
    public NodeStatus run(Situation input) throws MissingNodeException {

        if (input.getBall().getVelocity().y * Arena.getTeamGoalYDirection(input.myPlayerIndex) > Arena.getTeamGoalYDirection(input.myPlayerIndex)) {
            return NodeStatus.DEFAULT_SUCCESS;
        }
        return NodeStatus.DEFAULT_FAILURE;
    }
}
