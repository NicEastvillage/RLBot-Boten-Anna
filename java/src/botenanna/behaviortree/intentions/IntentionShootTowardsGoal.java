package botenanna.behaviortree.intentions;

import botenanna.fitness.FitnessFunction;
import botenanna.fitness.FitnessShootInDirection;
import botenanna.game.Situation;
import botenanna.physics.Path;

public class IntentionShootTowardsGoal extends Intention {


    /** The intention IntentionShootTowardsGoal will get the destination of the enemy goal
     * and then shoot the ball towards that destination. */
    public IntentionShootTowardsGoal(String[] arguments) throws IllegalArgumentException {
        super(arguments);
    }

    @Override
    protected FitnessFunction getFitnessFunction(Situation input) {
        return new FitnessShootInDirection(new Path(Situation.getGoalBox(input.enemyPlayerIndex)), 20, 20);
    }

    @Override
    protected boolean shouldInterrupt(Situation input) {
        return false;
    }
}
