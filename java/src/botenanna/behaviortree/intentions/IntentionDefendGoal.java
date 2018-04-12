package botenanna.behaviortree.intentions;

import botenanna.game.Situation;
import botenanna.fitness.*;
import botenanna.math.Vector3;

import java.util.List;

/** The IntentionDefendGoal is the intention to go to the agents own goal and look towards the middle of the field.
 * Its signature is {@code "IntentionDefendGoal"}*/
public class IntentionDefendGoal extends Intention {

    /** The IntentionDefendGoal is the intention to go to the agents own goal and look towards the middle of the field.
     * Its signature is {@code "IntentionDefendGoal"}*/
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
