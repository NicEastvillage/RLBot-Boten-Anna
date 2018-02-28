package botenanna.behaviortree.composites;

import botenanna.behaviortree.MissingNodeException;
import botenanna.behaviortree.NodeStatus;
import botenanna.behaviortree.Status;
import botenanna.behaviortree.decorators.Decorator;
import rlbot.api.GameData;

/** <p>The Invert is a Decorator node that inverts the return value of its child. So SUCCESS' becomes FAILURES, and FAILURES
 * become SUCCESS'. If the child returns RUNNING, this returns RUNNING as well.</p>
 * <p>Works like a negation in logic.</p> */
public class Invert extends Decorator {

    @Override
    public void reset() {
        // Irrelevant
    }

    @Override
    public NodeStatus run(GameData.GameTickPacket packet) throws MissingNodeException {
        if (child == null) throw new MissingNodeException(this);

        NodeStatus result = child.run(packet);

        // Failure becomes success, and success becomes failure
        if (result.status == Status.RUNNING) return result;
        else if (result.status == Status.SUCCESS) return NodeStatus.DEFAULT_FAILURE;
        else return NodeStatus.DEFAULT_SUCCESS;
    }
}
