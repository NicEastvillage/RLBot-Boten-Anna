package botenanna;

import botenanna.behaviortree.*;
import botenanna.behaviortree.composites.Selector;
import botenanna.behaviortree.composites.Sequencer;
import botenanna.behaviortree.decorators.Invert;
import botenanna.behaviortree.tasks.*;
import botenanna.behaviortree.guards.*;
import botenanna.math.RLMath;
import botenanna.math.Vector2;
import botenanna.math.Vector3;
import botenanna.overlayWindow.StatusWindow;
import botenanna.physics.Rigidbody;
import botenanna.physics.Boostpads;
import rlbot.api.GameData;

public class Bot {

    public enum Team {
        BLUE, ORANGE
    }

    private final Team team;
    private final int playerIndex;
    private BehaviorTree behaviorTree;

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
}