package botenanna.behaviortree.composites;

import botenanna.behaviortree.Node;
import botenanna.behaviortree.builder.BehaviourTreeChildException;

import java.util.ArrayList;

/** Composites are nodes that have one or more child nodes.*/
public abstract class Composite implements Node {

    /** A list of all children this composite node has. */
    protected final ArrayList<Node> children = new ArrayList<>();

    /** Add a child to this Composite node */
    @Override
    public final void addChild(Node child) throws BehaviourTreeChildException {
        children.add(child);
    }
}
