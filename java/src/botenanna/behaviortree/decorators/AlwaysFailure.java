package botenanna.behaviortree.decorators;

import botenanna.AgentInput;
import botenanna.behaviortree.MissingNodeException;
import botenanna.behaviortree.NodeStatus;
import botenanna.behaviortree.Status;

/** The AlwaysFailure node is a decorator. No matter if its child returns SUCCESS or FAILURE, the AlwaysFailure will
 * always return FAILURE. If its child returns RUNNING the running NodeStatus is returned. */
public class AlwaysFailure extends Decorator {

    @Override
    public void reset() {
        // Irrelevant
    }

    @Override
    public NodeStatus run(AgentInput input) throws MissingNodeException {
        NodeStatus status = child.run(input);
        if (status.status == Status.RUNNING) return status;
        else return NodeStatus.DEFAULT_FAILURE;
    }
}
