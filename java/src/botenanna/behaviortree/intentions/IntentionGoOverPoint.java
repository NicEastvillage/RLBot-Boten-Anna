package botenanna.behaviortree.intentions;

import botenanna.behaviortree.ArgumentTranslator;
import botenanna.game.Arena;
import botenanna.game.Situation;
import botenanna.intentions.IntentionFunction;
import botenanna.intentions.IntentionFunctionDriveOverPointWithAngle;
import botenanna.math.Vector3;

import java.util.function.Function;

/** The IntentionGoOverPoint is the intention to go to any point given as an argument. The agent will try to arrive with an
 * angle towards the enemy goal box, but nothing is guaranteed. Because of this intentions vague definition, the
 * performance of this node will be unreliable.
 * Its signature is {@code "IntentionGoOverPoint <point:Vector3>"}*/
public class IntentionGoOverPoint extends Intention {

    private Function<Situation, Object> pointFunc;

    /** The IntentionGoOverPoint is the intention to go to any point given as an argument. The agent will try to arrive with an
     * angle towards the enemy goal box, but nothing is guaranteed. Because of this intentions vague definition, the
     * performance of this node will be unreliable.
     * Its signature is {@code "IntentionGoOverPoint <point:Vector3>"}*/
    public IntentionGoOverPoint(String[] arguments) throws IllegalArgumentException {
        super(arguments);

        pointFunc = ArgumentTranslator.get(arguments[0]);
    }

    @Override
    protected boolean isValidNumberOfArguments(int argumentCount) {
        return argumentCount == 1;
    }

    @Override
    protected IntentionFunction getIntentionFunction(Situation input) {
        final Vector3 enemyGoal = Arena.getGoalPos(input.enemyPlayerIndex);
        return new IntentionFunctionDriveOverPointWithAngle(s -> (Vector3) pointFunc.apply(s), s -> enemyGoal, 0.7, 30, false);
    }

    @Override
    protected boolean shouldInterrupt(Situation input) {
        return false;
    }
}
