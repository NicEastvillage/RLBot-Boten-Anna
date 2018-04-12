package botenanna.behaviortree.intentions;


import botenanna.behaviortree.Leaf;
import botenanna.behaviortree.MissingNodeException;
import botenanna.behaviortree.NodeStatus;
import botenanna.behaviortree.Status;
import botenanna.game.ActionSet;
import botenanna.game.Situation;
import botenanna.fitness.*;
import botenanna.game.simulation.AStar;
import botenanna.math.Vector3;
import botenanna.physics.TimeLine;
import botenanna.physics.TimeTracker;
import rlbot.api.GameData;

import java.util.List;

public class IntentionDefendGoal extends Intention {

    public IntentionDefendGoal(String[] arguments) throws IllegalArgumentException {
        super(arguments);
    }

    @Override
    protected FitnessInterface getFitnessFunction(Situation input) {
        return new FitnessDriveOverPointWithAngle(Situation.getGoalBox(input.myPlayerIndex), new Vector3(), 0.30, 60);
    }

    @Override
    protected boolean shouldInterrupt(Situation input) {
        return false;
    }
}
