package botenanna.behaviortree.guards;

import botenanna.AgentInput;
import botenanna.behaviortree.Leaf;
import botenanna.behaviortree.MissingNodeException;
import botenanna.behaviortree.NodeStatus;

public class GuardHasBoost extends Leaf {

    private int amount = 50;

    /** The GuardHasBoost returns SUCCESS when the agent has more than a given amount of boost.
     * If nothing is specified, that amount will be 50%.
     *
     * The signature is: {@code GuardHasBoost [amount:INT]} */
    public GuardHasBoost(String[] arguments) throws IllegalArgumentException {
        super(arguments);

        if (arguments.length > 1) {
            throw new IllegalArgumentException();
        }

        // Parse optional amount
        if (arguments.length == 1) {
            amount = Integer.parseInt(arguments[0]);
        }
    }

    @Override
    public void reset() {
        // Irrelevant
    }

    @Override
    public NodeStatus run(AgentInput input) throws MissingNodeException {

        if (amount <= input.myCar.boost) {
            // Return success
            return NodeStatus.DEFAULT_SUCCESS;
        }

        return NodeStatus.DEFAULT_FAILURE;
    }
}
