package botenanna.behaviortree.decorators;

import botenanna.Situation;
import botenanna.behaviortree.MissingNodeException;
import botenanna.behaviortree.NodeStatus;
import botenanna.behaviortree.Status;

/** <p>The Inverter is a Decorator node that inverts the return value of its child. So SUCCESS' becomes FAILURES, and FAILURES
 * become SUCCESS'. If the child returns RUNNING, this returns RUNNING as well.</p>
 * <p>Works like a negation in logic.</p> */
public class Inverter extends Decorator {

    @Override
    public void reset() {
        // Irrelevant
    }

    @Override
    public NodeStatus run(Situation input) throws MissingNodeException {
        if (child == null) throw new MissingNodeException(this);

        NodeStatus result = child.run(input);

        // Failure becomes success, and success becomes failure
        if (result.status == Status.RUNNING) return result;
        else if (result.status == Status.SUCCESS) return NodeStatus.DEFAULT_FAILURE;
        else return NodeStatus.DEFAULT_SUCCESS;
    }
}
