package botenanna.behaviortree.decorators;

import botenanna.behaviortree.Node;
import botenanna.behaviortree.builder.BehaviourTreeChildException;

/** Decorators are nodes that only have one child node. */
public abstract class Decorator implements Node {

    /** The child node of this Decorator. */
    protected Node child;

    /** Set the child node of this Decorator. */
    @Override
    public final void addChild(Node child) throws BehaviourTreeChildException {
        if (this.child != null || child == null) throw new BehaviourTreeChildException();
        this.child = child;
    }
}
