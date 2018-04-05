package botenanna.behaviortree.tasks;

import botenanna.game.Situation;
import botenanna.game.Actions;
import botenanna.behaviortree.Leaf;
import botenanna.behaviortree.MissingNodeException;
import botenanna.behaviortree.NodeStatus;
import botenanna.behaviortree.Status;

public class TaskGoForwards extends Leaf {

    /** <p>Make the agent go forwards in a straight line. Mainly used for testing.</p>
     * <p>Signature: {@code TaskGoForwards}</p>*/
    public TaskGoForwards(String[] arguments) throws IllegalArgumentException {
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
        return new NodeStatus(Status.RUNNING, new Actions().withAcceleration(1), this);
    }
}
