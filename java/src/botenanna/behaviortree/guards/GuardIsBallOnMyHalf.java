package botenanna.behaviortree.guards;

import botenanna.game.Arena;
import botenanna.game.Situation;
import botenanna.behaviortree.*;

public class GuardIsBallOnMyHalf extends Leaf {

    /** The GuardIsBallOnMyHalf returns SUCCESS when the ball is on the agent's own half of the field.
     * Can be invert to check if the ball is on the enemy half.
     *
     * Its signature is {@code GuardIsBallOnMyHalf} */
    public GuardIsBallOnMyHalf(String[] arguments) throws IllegalArgumentException {
        super(arguments);

        // Takes no arguments
        if (arguments.length != 0) throw new IllegalArgumentException();
    }

    @Override
    public void reset() {
        // Irrelevant
    }

    @Override
    public NodeStatus run(Situation input) throws MissingNodeException {

        double ballY = input.getBall().getPosition().y;

        if (ballY * Arena.getTeamGoalYDirection(input.myPlayerIndex) >= 0) {
            return NodeStatus.DEFAULT_SUCCESS;
        } else {
            return NodeStatus.DEFAULT_FAILURE;
        }
    }
}
