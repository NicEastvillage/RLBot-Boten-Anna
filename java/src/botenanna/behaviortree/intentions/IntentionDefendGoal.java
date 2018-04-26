package botenanna.behaviortree.intentions;

import botenanna.game.Arena;
import botenanna.game.Situation;
import botenanna.fitness.*;
import botenanna.math.Vector3;
import botenanna.physics.Path;

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
        return new FitnessDriveOverPointWithAngle(new Path(Arena.getGoalPos(input.myPlayerIndex)), new Path(new Vector3()), 0.30, 60, true);
    }

    @Override
    protected boolean shouldInterrupt(Situation input) {
        return false;
    }
}
