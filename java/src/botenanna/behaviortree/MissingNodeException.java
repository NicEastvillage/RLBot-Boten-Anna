package botenanna.behaviortree;

/** Thrown when a node is missing a child node. */
public class MissingNodeException extends RuntimeException {

    private Node parent;

    /** Thrown when a node is missing a child node. */
    public MissingNodeException(Node parent) {
        this.parent = parent;
    }

    /** @return the node which is missing a child node. */
    public Node getParent() {
        return parent;
    }
}
