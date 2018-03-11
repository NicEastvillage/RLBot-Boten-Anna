package botenanna.behaviortree.guards;

import botenanna.AgentInput;
import botenanna.behaviortree.Leaf;
import botenanna.behaviortree.MissingNodeException;
import botenanna.behaviortree.NodeStatus;

public class GuardIsMidAir extends Leaf {

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
        return input.myIsMidAir ? NodeStatus.DEFAULT_SUCCESS : NodeStatus.DEFAULT_FAILURE;
    }
}
