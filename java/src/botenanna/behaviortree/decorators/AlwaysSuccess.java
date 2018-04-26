package botenanna.behaviortree.decorators;

import botenanna.game.Situation;
import botenanna.behaviortree.MissingNodeException;
import botenanna.behaviortree.NodeStatus;
import botenanna.behaviortree.Status;

/** The AlwaysSuccess node is a decorator. No matter if its child returns SUCCESS or FAILURE, the AlwaysSuccess will
 * always return SUCCESS. If its child returns RUNNING the running NodeStatus is returned. */
public class AlwaysSuccess extends Decorator {

    @Override
    public void reset() {
        // Irrelevant
    }

    @Override
    public NodeStatus run(Situation input) throws MissingNodeException {
        NodeStatus status = child.run(input);
        if (status.status == Status.RUNNING) return status;
        else return NodeStatus.DEFAULT_SUCCESS;
    }
}
