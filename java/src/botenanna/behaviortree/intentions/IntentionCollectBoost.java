package botenanna.behaviortree.intentions;

import botenanna.intentions.IntentionFunctionDriveOverPointWithAngle;
import botenanna.intentions.IntentionFunction;
import botenanna.game.Boostpad;
import botenanna.game.Situation;
import botenanna.math.Vector3;

/** The IntentionCollectBoost will collect the best boost pad and continue towards the balls position.
 * Its signature is {@code "IntentionCollectBoost"}*/
public class IntentionCollectBoost extends Intention {

    /** The IntentionCollectBoost will collect the best boost pad and continue towards the balls position.
     * Its signature is {@code "IntentionCollectBoost"}*/
    public IntentionCollectBoost(String[] arguments) throws IllegalArgumentException {
        super(arguments);
    }

    @Override
    protected IntentionFunction getIntetionFunction(Situation input) {
        final Vector3 bestPadPos = input.getBestBoostPad().getPosition(); // Not dynamic, since that will confuse the algorithms
        return new IntentionFunctionDriveOverPointWithAngle(s -> bestPadPos, s -> s.getBall().getPosition(), 0.45, Boostpad.PAD_RADIUS, false);
    }

    @Override
    protected boolean shouldInterrupt(Situation input) {
        return false;
    }
}
