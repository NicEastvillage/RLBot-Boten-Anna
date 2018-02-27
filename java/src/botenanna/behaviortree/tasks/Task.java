package botenanna.behaviortree.tasks;

import botenanna.behaviortree.BehaviourTreeBuildingException;
import botenanna.behaviortree.Node;

public abstract class Task implements Node {

    private String[] arguments;

    public Task(String[] arguments) {
        this.arguments = arguments;
    }

    /** Should not be called, since Tasks have no children! */
    @Override
    public final void addChild(Node child) throws BehaviourTreeBuildingException {
        throw new BehaviourTreeBuildingException();
    }

    /** @return the arguments this node received, when it was created. */
    public String[] getArguments() {
        return arguments;
    }
}
