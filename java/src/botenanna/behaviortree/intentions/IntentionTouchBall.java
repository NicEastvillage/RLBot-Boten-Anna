package botenanna.behaviortree.intentions;

import botenanna.intentions.IntentionFunctionDriveOverPointWithAngle;
import botenanna.game.Arena;
import botenanna.intentions.IntentionFunction;
import botenanna.game.Situation;
import botenanna.math.Vector3;

/** The IntentionTouchBall is the intention to go to the ball's position. The agent will try to arrive with an
 * angle towards the enemy goal box, but nothing is guaranteed.
 * Its signature is {@code "IntentionTouchBall"}*/
public class IntentionTouchBall extends Intention {

    /** The IntentionTouchBall is the intention to go to the ball's position. The agent will try to arrive with an
     * angle towards the enemy goal box, but nothing is guaranteed.
     * Its signature is {@code "IntentionTouchBall"}*/
    public IntentionTouchBall(String[] arguments) throws IllegalArgumentException {
        super(arguments);
    }

    @Override
    protected IntentionFunction getIntetionFunction(Situation input) {
        final Vector3 enemyGoal = Arena.getGoalPos(input.enemyPlayerIndex);
        return new IntentionFunctionDriveOverPointWithAngle(s -> s.getBall().getPosition(), s -> enemyGoal, 0.7, 20, false);
    }

    @Override
    protected boolean shouldInterrupt(Situation input) {
        return false;
    }
}
