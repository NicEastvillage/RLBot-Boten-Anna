package botenanna.behaviortree;

import botenanna.AgentOutput;
import rlbot.api.GameData;

public class BehaviorTree implements Node {

    private Node topNode;
    private NodeStatus lastNodeStatus;

    @Override
    public void reset() {
        lastNodeStatus = null;
    }

    @Override
    public NodeStatus run(GameData.GameTickPacket packet) throws MissingNodeException {

        if (topNode == null) throw new MissingNodeException(this);

        NodeStatus newNodeStatus = topNode.run(packet);

        // If newNodeStatus's creator is not the same as the lastNodeStatus's creator, then
        // lastNodeStatus's creator and all dependencies will be reset.
        if (lastNodeStatus != null && (newNodeStatus == null || newNodeStatus.creator != lastNodeStatus.creator)) {
            lastNodeStatus.creator.reset();
            for (Node dependencies : lastNodeStatus.getDependencies()) {
                dependencies.reset();
            }
        }

        // Set lastNodeStatus to newNodeStatus
        if (newNodeStatus == null) {
            // If newNodeStatus is null, something went wrong, so we just create one now.
            lastNodeStatus = new NodeStatus(Status.RUNNING, new AgentOutput(), this);
        } else {
            lastNodeStatus = newNodeStatus;
        }

        return lastNodeStatus;
    }

    /** Evaluate the behaviour tree.
     * @return the AgentOutput. */
    public AgentOutput evaluate(GameData.GameTickPacket packet) {
        NodeStatus nodeStatus = run(packet);
        return nodeStatus.output;
    }

    /** Set the top node of the tree to {@code child}.
     * @param child top node of the tree. */
    @Override
    public void addChild(Node child) throws BehaviourTreeBuildingException {
        if (topNode != null) {
            throw new BehaviourTreeBuildingException();
        }

        topNode = child;
    }
}
