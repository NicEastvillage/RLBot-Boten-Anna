package botenanna.behaviortree.composites;

import botenanna.game.Situation;
import botenanna.behaviortree.MissingNodeException;
import botenanna.behaviortree.Node;
import botenanna.behaviortree.NodeStatus;
import botenanna.behaviortree.Status;

/** <p>The Selector is a Composite node. When run it goes through each of its child nodes, one at a time, until one succeeds.</p>
 * <p>If a child returns FAILURE it goes onto the next child. If a child returns SUCCESS the Selector immediately stops
 * and returns SUCCESS itself. If any RUNNING node is found, RUNNING is returned.</p>
 * <p>The Selector can be used as a conditional OR with short-circuit evaluation.</p>*/
public class Selector extends Composite {

    @Override
    public void reset() {
        // Irrelevant
    }

    @Override
    public NodeStatus run(Situation input) throws MissingNodeException {
        if (children.size() == 0) throw new MissingNodeException(this);

        // Go through each child node until one returns RUNNING or SUCCESS
        for (Node child : children) {
            NodeStatus result = child.run(input);
            if (result.status == Status.RUNNING) return result;
            if (result.status == Status.SUCCESS) return NodeStatus.DEFAULT_SUCCESS;
        }

        // If no RUNNING or SUCCESS was found, return FAILURE.
        return NodeStatus.DEFAULT_FAILURE;
    }
}
