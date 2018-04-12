package botenanna.behaviortree.intentions;

import botenanna.behaviortree.*;
import botenanna.fitness.FitnessInterface;
import botenanna.game.ActionSet;
import botenanna.game.Situation;
import botenanna.game.simulation.AStar;
import botenanna.physics.TimeLine;
import botenanna.physics.TimeTracker;

public abstract class Intention extends Leaf {

    public static final double STEPSIZE = 0.1;

    private boolean isRunning = false;
    private FitnessInterface fitness;
    private TimeTracker timeTracker;
    private TimeLine<ActionSet> sequence;

    public Intention(String[] arguments) throws IllegalArgumentException {
        super(arguments);

        if (arguments.length != 0) throw new IllegalArgumentException();

        timeTracker = new TimeTracker();
    }

    @Override
    public void reset() {
        isRunning = false;
        fitness = null;
        sequence = null;
    }

    @Override
    public NodeStatus run(Situation input) throws MissingNodeException {
        if (!isRunning) {
            fitness = getFitnessFunction(input);
            sequence = AStar.findSequence(input, fitness, STEPSIZE);
            timeTracker.startTimer();
            isRunning = true;
        }

        // Interrupted?
        if (shouldInterrupt(input)) {
            isRunning = false;
            return NodeStatus.DEFAULT_FAILURE;
        }

        // Has next step?
        if (sequence.getLastTime() < timeTracker.getElapsedSecondsTimer() + STEPSIZE) {
            reset();
            return NodeStatus.DEFAULT_SUCCESS;
        }

        ActionSet action = sequence.evaluate(timeTracker.getElapsedSecondsTimer());
        return new NodeStatus(Status.RUNNING, action, this);
    }

    protected abstract FitnessInterface getFitnessFunction(Situation input);
    protected abstract boolean shouldInterrupt(Situation input);
}
