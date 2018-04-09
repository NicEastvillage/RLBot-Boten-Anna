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

public class IntentionDefendGoal extends Leaf {

    private FitnessInterface fitness;
    private TimeTracker timeTracker;
    private TimeLine<ActionSet> sequence;

    public IntentionDefendGoal(String[] arguments) throws IllegalArgumentException {
        super(arguments);

        if (arguments.length != 0) throw new IllegalArgumentException();

        timeTracker = new TimeTracker();
    }



    @Override
    public void reset() {
        fitness = null;
    }

    @Override
    public NodeStatus run(Situation input) throws MissingNodeException {

        if (fitness == null) {
            fitness = new FitnessDriveOverPointWithAngle(Situation.getGoalBox(input.myPlayerIndex), new Vector3(), 0.30, 60);
            sequence = AStar.findSequence(input, fitness, 0.1);
            timeTracker.startTimer();
        }

        if (sequence.getLastTime() < timeTracker.getElapsedSecondsTimer() + 0.1) {
            reset();
            return NodeStatus.DEFAULT_SUCCESS;
        }

        ActionSet action = sequence.evaluate(timeTracker.getElapsedSecondsTimer());
        return new NodeStatus(Status.RUNNING, action, this);
    }

}
