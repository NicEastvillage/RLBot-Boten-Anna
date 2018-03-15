package botenanna.behaviortree.tasks;

import botenanna.AgentInput;
import botenanna.behaviortree.Leaf;
import botenanna.behaviortree.MissingNodeException;
import botenanna.behaviortree.NodeStatus;

public class TaskBallTowardsGoal extends Leaf {
    public TaskBallTowardsGoal(String[] arguments) throws IllegalArgumentException {
        super(arguments);
    }

    @Override
    public void reset() {

    }

    @Override
    public NodeStatus run(AgentInput input) throws MissingNodeException {
        return null;
    }
}
