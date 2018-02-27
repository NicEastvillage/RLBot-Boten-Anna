package botenanna.behaviortree;

import rlbot.api.GameData;

public interface Node {
    /** Reset any data held by the node. */
    void reset();
    /** Run the node and its children.
     * @return a NodeStatus containing the result of the node. */
    NodeStatus run(GameData.GameTickPacket packet) throws MissingNodeException;
    void addChild(Node child) throws BehaviourTreeBuildingException ;
}
