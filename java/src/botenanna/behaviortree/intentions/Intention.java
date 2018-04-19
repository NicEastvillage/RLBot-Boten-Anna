package botenanna.behaviortree.intentions;

import botenanna.behaviortree.*;
import botenanna.fitness.FitnessInterface;
import botenanna.game.ActionSet;
import botenanna.game.Situation;
import botenanna.game.simulation.AStar;
import botenanna.physics.TimeLine;
import botenanna.physics.TimeTracker;

/** Intentions are nodes with no children. They use a fitness-function, the A*-algorithm, and simulation of the game to
 * find a sequence of ActionSets that will fulfil the intention. They return FAILURE when the method
 * {@link #shouldInterrupt(Situation)} returns true, and they return SUCCESS when the sequence is over. When there are
 * steps left in the sequence, they will return RUNNING with the evaluated ActionSet. */
public abstract class Intention extends Leaf {

    public static final double STEPSIZE = 0.05;

    private boolean isRunning = false;
    private FitnessInterface fitness;
    private TimeLine<ActionSet> sequence;
    private TimeTracker timeTracker = new TimeTracker();

    public Intention(String[] arguments) throws IllegalArgumentException {
        super(arguments);

        if (arguments.length != 0) throw new IllegalArgumentException();
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
