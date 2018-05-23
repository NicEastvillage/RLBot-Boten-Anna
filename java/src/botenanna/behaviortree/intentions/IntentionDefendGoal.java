package botenanna.behaviortree.intentions;

import botenanna.game.Arena;
import botenanna.game.Situation;
import botenanna.intentions.*;
import botenanna.math.Vector3;

/** The IntentionDefendGoal is the intention to go to the agents own goal and look towards the middle of the field.
 * Its signature is {@code "IntentionDefendGoal"}*/
public class IntentionDefendGoal extends Intention {

    /** The IntentionDefendGoal is the intention to go to the agents own goal and look towards the middle of the field.
     * Its signature is {@code "IntentionDefendGoal"}*/
    public IntentionDefendGoal(String[] arguments) throws IllegalArgumentException {
        super(arguments);
    }

    @Override
    protected IntentionFunction getIntetionFunction(Situation input) {
        final Vector3 goalPos = Arena.getGoalPos(input.myPlayerIndex);
        return new IntentionFunctionDriveOverPointWithAngle(s -> goalPos, s -> new Vector3(), 0.30, 60, true);
    }

    @Override
    protected boolean shouldInterrupt(Situation input) {
        return false;
    }
}
