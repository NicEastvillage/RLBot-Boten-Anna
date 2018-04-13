package botenanna.behaviortree.intentions;

import botenanna.fitness.FitnessDriveOverPointWithAngle;
import botenanna.fitness.FitnessInterface;
import botenanna.game.Situation;
import botenanna.physics.Path;

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
    protected FitnessInterface getFitnessFunction(Situation input) {
        return new FitnessDriveOverPointWithAngle(input.ball.getPath(50, STEPSIZE), new Path(Situation.getGoalBox(input.myPlayerIndex)), 0.7, 20, false);
    }

    @Override
    protected boolean shouldInterrupt(Situation input) {
        return false;
    }
}
