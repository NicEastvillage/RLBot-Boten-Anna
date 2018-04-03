package botenanna.behaviortree.guards;

import botenanna.AgentInput;
import botenanna.behaviortree.Leaf;
import botenanna.behaviortree.MissingNodeException;
import botenanna.behaviortree.NodeStatus;

public class GuardIsMidAir extends Leaf {

    /** <p>The GuardIsMidAir returns SUCCESS when the agent is mid air and FAILURE when it is on the ground or on a wall.</p>
     * <p>It's signature is: {@code GuardIsMidAir}</p>*/
    public GuardIsMidAir(String[] arguments) throws IllegalArgumentException {
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
        // Simply check the isMidAir variable from input
        return input.myCar.isMidAir ? NodeStatus.DEFAULT_SUCCESS : NodeStatus.DEFAULT_FAILURE;
    }
}
