package botenanna.behaviortree;

import botenanna.AgentInput;
import botenanna.behaviortree.builder.BehaviourTreeBuildingException;

public interface Node {
    /** Reset any data held by the node. */
    void reset();
    /** Run the node and its children.
     * @return a NodeStatus containing the result of the node. */
    NodeStatus run(AgentInput input) throws MissingNodeException;
    void addChild(Node child) throws BehaviourTreeBuildingException;
}
