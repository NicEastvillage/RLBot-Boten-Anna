package botenanna.behaviortree;

import botenanna.Situation;
import botenanna.AgentOutput;
import botenanna.behaviortree.builder.BehaviourTreeBuildingException;

public class BehaviorTree implements Node {

    private Node topNode;
    private NodeStatus lastNodeStatus;

    @Override
    public void reset() {
        lastNodeStatus = null;
    }

    @Override
    public NodeStatus run(Situation input) throws MissingNodeException {

        if (topNode == null) throw new MissingNodeException(this);

        NodeStatus newNodeStatus;
        // Check if last status has high priority
        if (lastNodeStatus != null && lastNodeStatus.isHighPriority) {
            newNodeStatus = lastNodeStatus.creator.run(input);
        } else {
            newNodeStatus = topNode.run(input);
        }

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
    public AgentOutput evaluate(Situation input) {
        NodeStatus nodeStatus = run(input);
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

    /** Gets the string of the lastNodeStatus creator
     *  @return the string of the creator     */
    public String getLastNodeName() {
        if (lastNodeStatus==null){
                return "None";}
        return String.valueOf(lastNodeStatus.creator);

    }
}
