package botenanna.game.simulation;

import botenanna.intentions.IntentionFunction;
import botenanna.game.ActionSet;
import botenanna.game.Car;
import botenanna.game.Situation;
import botenanna.physics.SteppedTimeLine;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

public class AStar {

    private static final int FORCED_STOP_ITERATIONS = 4;

    /** The TimeNode wraps the Situation which enables us to build a tree of Situations and remember details about
     * the evaluation of each node. */
    private static class TimeNode {
        public final Situation situation;
        public final ActionSet actionTaken;
        public final TimeNode cameFrom;
        public final double timeSpent;
        public final double intentionValue;

        public TimeNode(Situation situation, ActionSet actionTaken, TimeNode cameFrom, double timeSpent, IntentionFunction intentionFunction) {
            this.cameFrom = cameFrom;
            this.situation = situation;
            this.actionTaken = actionTaken;
            this.timeSpent = timeSpent;
            intentionValue = intentionFunction.compute(situation, timeSpent);
        }
    }

    /** Find a sequence of actions that steers the agent towards a desired intention defined by an intention function.
     * The method uses a modified version of A*. */
    public static SteppedTimeLine<ActionSet> findSequence(Situation startSituation, IntentionFunction intention, double stepsize) {

        TimeNode startNode = new TimeNode(startSituation, new ActionSet(), null, 0, intention);

        TreeSet<TimeNode> openSet = new TreeSet<>((n1, n2) -> {
            if (n1 == n2) return 0; // Must be consistent with equals
            double fit = n1.intentionValue - n2.intentionValue;
            // Even if the situations have the same value, they are not the same
            if (fit < 0) return -1;
            else return 1;
        });

        openSet.add(startNode);

        while (!openSet.isEmpty()) {
            TimeNode current = openSet.first();

            // Is this situation a fulfilling destination?
            if (current.actionTaken != null) {
                if (current.timeSpent >= stepsize * FORCED_STOP_ITERATIONS || intention.isDeviationFulfilled(current.situation, current.timeSpent)) {
                    List<ActionSet> sequence = reconstructSequence(current);
                    return toTimeLine(sequence, stepsize);
                }
            }

            openSet.remove(current);

            // Try all sensible actions and simulate what situations they create
            List<ActionSet> followingActions = getFollowingActionSets(current.situation, current.actionTaken);
            for (ActionSet action : followingActions) {
                Situation newSituation = Simulation.simulate(current.situation, stepsize, action);
                TimeNode node = new TimeNode(newSituation, action, current, current.timeSpent + stepsize, intention);
                openSet.add(node);
            }
        }

        return null;
    }

    /** Helper method for the {@link #findSequence(Situation, IntentionFunction, double)}.
     * Takes a list of ActionSets and places them in a TimeLine with stepsize interval between each. */
    private static SteppedTimeLine<ActionSet> toTimeLine(List<ActionSet> sequence, double stepsize) {
        SteppedTimeLine<ActionSet> timeLine = new SteppedTimeLine<>();
        double time = 0;
        for (ActionSet action : sequence) {
            timeLine.addTimeStep(time, action);
            time += stepsize;
        }
        return timeLine;
    }

    /** Helper method for the {@link #findSequence(Situation, IntentionFunction, double)} to backtrack the actions taken
     * and create the sequence. */
    private static List<ActionSet> reconstructSequence(TimeNode destination) {
        TimeNode current = destination;
        List<ActionSet> sequence = new ArrayList<>();
        while (current.cameFrom != null) {
            sequence.add(current.actionTaken);
            current = current.cameFrom;
        }
        return sequence;
    }

    /** This method will generate all valid ActionSet that sensibly follow a given ActionSet in a Situation.
     * @param situation the Situation.
     * @param current the ActionSet prior to the ones generated. */
    public static List<ActionSet> getFollowingActionSets(Situation situation, ActionSet current) {

        List<ActionSet> following = new LinkedList<>();

        Car myCar = situation.getMyCar();

        double[] newThrottles = getFollowingDirections(current == null ? 0 : current.getThrottle());
        double[] newSteerings = getFollowingDirections(current == null ? 0 : current.getSteer());
        // pitch and roll is 0, if car is grounded
        double[] newPitches = myCar.isMidAir() ? getFollowingDirections(current == null ? 0 : current.getPitch()) : new double[]{0};
        double[] newRolls = myCar.isMidAir() ? getFollowingDirections(current == null ? 0 : current.getRoll()) : new double[]{0};
        // jump is false, if jumping has no effect // FIXME With current implementation, second jump will always be one step long
        boolean[] newJumps = !myCar.hasDoubleJumped() ? new boolean[]{true, false} : new boolean[]{false};
        // boost is false, if car has no boost
        boolean[] newBoosts = myCar.getBoost() > 0 ? new boolean[]{true, false} : new boolean[]{false};
        boolean[] newSlides = new boolean[]{true, false};

        for (double throttle : newThrottles) {
            for (double steer : newSteerings) {
                for (double pitch : newPitches) {
                    for (double roll : newRolls) {
                        for (boolean jump : newJumps) {
                            for (boolean boost : newBoosts) {
                                // boost is false, if throttle is 0 or -1
                                if (boost && throttle != 1) continue;

                                for (boolean slide : newSlides) {
                                    // slide is false, when boost is true, or when steer == 0
                                    if (slide && (boost || steer == 0)) continue;

                                    following.add(new ActionSet()
                                            .withThrottle(throttle)
                                            .withSteer(steer)
                                            .withPitch(pitch)
                                            .withRoll(roll)
                                            .withJump(jump)
                                            .withBoost(boost)
                                            .withSlide(slide));
                                }
                            }
                        }
                    }
                }
            }
        }

        return following;
    }

    /** A helper method that returns an array of directions that are close to a given direction. */
    private static double[] getFollowingDirections(double value) {
        if (value == 1) return new double[]{1, 0};
        if (value == -1) return new double[]{0, -1};
        if (value == 0) return new double[]{1, 0, -1};
        return new double[0];
    }
}
