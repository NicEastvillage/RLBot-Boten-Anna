package botenanna.behaviortree.composites;

import botenanna.behaviortree.MissingNodeException;
import botenanna.behaviortree.Node;
import botenanna.behaviortree.NodeStatus;
import botenanna.behaviortree.Status;
import rlbot.api.GameData;

/** <p>The Sequencer is a Composite node. When run it goes through each of its child nodes, one at a time, until one fails.</p>
 * <p>If a child returns SUCCESS it goes onto the next child. If a child returns FAILURE the Sequencer immediately stops
 * and returns FAILURE itself. If any RUNNING node is found, RUNNING is returned.</p>
 * <p>The Sequencer can be used as a condition AND with short-circuit evaluation.</p>*/
public class Sequencer extends Composite {

    @Override
    public void reset() {
        // Irrelevant
    }

    @Override
    public NodeStatus run(GameData.GameTickPacket packet) throws MissingNodeException {
        if (children.size() == 0) throw new MissingNodeException(this);

        // Go through each child node until one returns RUNNING or FAILURE
        for (Node child : children) {
            NodeStatus result = child.run(packet);
            if (result.status == Status.RUNNING) return result;
            if (result.status == Status.FAILURE) return NodeStatus.DEFAULT_FAILURE;
        }

        // If no RUNNING or FAILURE was found, return SUCCESS.
        return NodeStatus.DEFAULT_SUCCESS;
    }
}
