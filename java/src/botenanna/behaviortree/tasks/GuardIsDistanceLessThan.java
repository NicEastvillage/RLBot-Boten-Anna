package botenanna.behaviortree.tasks;

import botenanna.AgentInput;
import botenanna.behaviortree.Leaf;
import botenanna.behaviortree.MissingNodeException;
import botenanna.behaviortree.NodeStatus;
import botenanna.math.Vector3;
import rlbot.api.GameData;

/** The GuardIsDistanceLessThan compares to Vector3 ands returns whether the distance between those are less than
 * a given distance.
 * Its signature is {@code GuardIsDistanceLessThan <Vector3> <Vector3> <double>}*/
public class GuardIsDistanceLessThan extends Leaf {

    private double distance = 1000;

    public GuardIsDistanceLessThan(String[] arguments) throws IllegalArgumentException {
        super(arguments); //TODO Actually get some arguments
    }

    @Override
    public void reset() {
        // Irrelevant
    }

    @Override
    public NodeStatus run(AgentInput input) throws MissingNodeException {
        // TODO For now, always return whether distance between player and ball is less than 1000

        // Get points
        Vector3 carPos = input.myLocation;
        Vector3 ballPos = input.ballLocation;

        // Compare distance
        double dist = carPos.minus(ballPos).getMagnitudeSqr();
        if (dist <= distance * distance) return NodeStatus.DEFAULT_SUCCESS;
        return NodeStatus.DEFAULT_FAILURE;
    }
}
