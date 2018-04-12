package botenanna.behaviortree.guards;

import botenanna.game.Situation;
import botenanna.behaviortree.Leaf;
import botenanna.behaviortree.MissingNodeException;
import botenanna.behaviortree.NodeStatus;


public class GuardBallMidAir extends Leaf{

/** The GuardBallMidAir compares if the ball z is in the air by taking the z value and checks if it is over a set value and returns true if is is over*/


    public GuardBallMidAir(String[] arguments) throws IllegalArgumentException {
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

        if(input.ball.getPosition().z > 200){
            return NodeStatus.DEFAULT_SUCCESS;
        } else {
            return NodeStatus.DEFAULT_FAILURE;
        }
    }
}