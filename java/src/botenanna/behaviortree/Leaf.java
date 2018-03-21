package botenanna.behaviortree;

import botenanna.behaviortree.builder.BehaviourTreeChildException;

public abstract class Leaf implements Node {

    private String[] arguments;

    public Leaf(String[] arguments) throws IllegalArgumentException {
        this.arguments = arguments;
    }

    /** Should not be called, since Leafs have no children! */
    @Override
    public final void addChild(Node child) throws BehaviourTreeChildException {
        throw new BehaviourTreeChildException();
    }

    /** @return the arguments this Leaf received, when it was created. */
    public String[] getArguments() {
        return arguments;
    }
}
