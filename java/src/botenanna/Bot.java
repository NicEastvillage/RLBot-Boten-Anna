package botenanna;

import botenanna.behaviortree.*;
import botenanna.game.Actions;
import botenanna.game.Situation;

public class Bot {

    public enum Team {
        BLUE, ORANGE
    }

    private final Team team;
    private final int playerIndex;
    private BehaviorTree behaviorTree;
    private Situation lastInputReceived;

    /** A Rocket League agent. */
    public Bot(int playerIndex, int teamIndex, BehaviorTree tree) {
        this.playerIndex = playerIndex;
        team = (teamIndex == 0 ? Team.BLUE : Team.ORANGE);
        behaviorTree = tree;
    }

    /** Let the bot process the information from the input packet
     * @param packet the game tick packet from the game
     * @return an Actions of what the agent want to do
     */
    public Actions process(Situation packet) {
        return behaviorTree.evaluate(packet);
    }

    public int getPlayerIndex() {
        return playerIndex;
    }

    public Team getTeam() {
        return team;
    }

    /** Getter for the behavior tree
     * @return the behaviorTree    */
    public BehaviorTree getBehaviorTree() {
        return behaviorTree;
    }

    public Situation getLastInputReceived() {
        return lastInputReceived;
    }

    public void setLastInputReceived(Situation lastInputReceived) {
        this.lastInputReceived = lastInputReceived;
    }
}