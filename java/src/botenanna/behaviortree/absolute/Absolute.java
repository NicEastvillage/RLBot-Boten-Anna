package botenanna.behaviortree.absolute;

import botenanna.behaviortree.Node;
import botenanna.behaviortree.builder.BehaviourTreeChildException;

/** An Absolute Node is a node with a fixed amount of child nodes. */
public abstract class Absolute implements Node {

    private int currentChildCount;
    private Node[] children;

    /** An Absolute Node is a node with a fixed amount of child nodes. */
    public Absolute(int childCount) {
        currentChildCount = 0;
        children = new Node[childCount];
    }

    /** Set the child of the Absolute Node. The children are set in order. The first call will set the first child, the
     * second call will set the second child, and so on. Children cannot be changed once set. */
    @Override
    public void addChild(Node child) throws BehaviourTreeChildException {
        if (child == null || currentChildCount >= children.length) throw new BehaviourTreeChildException();

        // Add child
        children[currentChildCount] = child;
        currentChildCount++;
    }

    /** Accessor to this Absolute Node's child Nodes. This will throw an exception if the child node is null,
     * or if {@code index} is out of bounds.
     * @param index the index of the returned child.
     * @return the child of this Absolute Node with index of {@code index}. */
    protected Node child(int index) {
        if (index < 0 || children.length <= index) throw new NullPointerException("Index of " + index + " is out of bounds. Absolute Node has " + children.length + " children.");
        if (children[index] == null) throw new NullPointerException("Child with index of " + index + " is null.");

        return children[index];
    }
}
