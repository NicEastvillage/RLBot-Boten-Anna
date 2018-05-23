package botenanna.behaviortree.intentions;

import botenanna.intentions.IntentionFunction;
import botenanna.intentions.IntentionFunctionShootInDirection;
import botenanna.game.Arena;
import botenanna.game.Situation;
import botenanna.math.Vector3;

public class IntentionShootTowardsGoal extends Intention {


    /** The intention IntentionShootTowardsGoal will get the destination of the enemy goal
     * and then shoot the ball towards that destination. */
    public IntentionShootTowardsGoal(String[] arguments) throws IllegalArgumentException {
        super(arguments);
    }

    @Override
    protected IntentionFunction getIntetionFunction(Situation input) {
        final Vector3 targetPoint = Arena.getGoalPos(input.enemyPlayerIndex);
        return new IntentionFunctionShootInDirection(s -> targetPoint, 20, 20);
    }

    @Override
    protected boolean shouldInterrupt(Situation input) {
        return false;
    }
}
