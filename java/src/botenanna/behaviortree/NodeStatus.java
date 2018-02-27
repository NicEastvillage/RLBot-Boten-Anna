package botenanna.behaviortree;

import botenanna.AgentOutput;

import java.util.ArrayList;

public class NodeStatus {

    /** A NodeStatus containing a success and nothing else. */
    public static final NodeStatus DEFAULT_FAILURE = new NodeStatus(Status.FAILURE, null, null);
    /** A NodeStatus containing a failure and nothing else. */
    public static final NodeStatus DEFAULT_SUCCESS = new NodeStatus(Status.SUCCESS, null, null);

    /** The return status of the node. If this is RUNNING, then {@code output} and {@code creator} is defined too.*/
    public final Status status;
    /** The AgentOutput produced by a Task. Will be null when {@code status} is not RUNNING.*/
    public final AgentOutput output;
    /** The creator of this NodeStatus. Can be null when {@code status} is not RUNNING.*/
    public final Node creator;

    private final ArrayList<Node> dependencies = new ArrayList<>();

    /** <p>A NodeStatus describes the result of a behaviour tree node.</p>
     * <p>Guard nodes must only create SUCCESS' or FAILURES. See NodeStatus.DEFAULT_SUCCESS and NodeStatus.DEFAULT_FAILURE
     * for commonly used NodeStatus'.</p>
     * <p>Task nodes usually creates RUNNING status', and when they do, {@code output} and {@code creator} must be defined.</p>
     * @param status the Status of the node. If this is RUNNING, {@code output} and {@code creator} must be defined, since the
     *               NodeStatus will then be return all the way to the behaviour tree's top.
     *               Otherwise they can be null.
     * @param output the resulting AgentOutput. @{@code status} must be RUNNING, if this is not null.
     * @param creator the node, that created this NodeStatus. The creator will be reset, if the NodeStatus created somewhere
     *                else in a subsequent tick.*/
    public NodeStatus(Status status, AgentOutput output, Node creator) {
        this.status = status;
        this.output = output;
        this.creator = creator;
    }

    /** */
    public void addDependency(Node dependency) {
        dependencies.add(dependency);
    }

    public ArrayList<Node> getDependencies() {
        return (ArrayList<Node>) dependencies.clone();
    }
}
