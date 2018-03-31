package botenanna;

import botenanna.behaviortree.*;

public class Bot {

    public enum Team {
        BLUE, ORANGE
    }

    private final Team team;
    private final int playerIndex;
    private BehaviorTree behaviorTree;
    private AgentInput lastInputReceived;

    /** An Rocket League agent. */
    public Bot(int playerIndex, int teamIndex, BehaviorTree tree) {
        this.playerIndex = playerIndex;
        team = (teamIndex == 0 ? Team.BLUE : Team.ORANGE);
        behaviorTree = tree;
    }

    /** Let the bot process the information from the input packet
     * @param packet the game tick packet from the game
     * @return an AgentOutput of what the agent want to do
     */
    public AgentOutput process(AgentInput packet) {
        return behaviorTree.evaluate(packet);
    }

    /** Getter for the behavior tree
     * @return the behaviorTree    */
    public BehaviorTree getBehaviorTree() {
        return behaviorTree;
    }

    public AgentInput getLastInputReceived() {
        return lastInputReceived;
    }

    public void setLastInputReceived(AgentInput lastInputReceived) {
        this.lastInputReceived = lastInputReceived;
    }
}