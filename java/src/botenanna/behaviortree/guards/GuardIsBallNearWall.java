package botenanna.behaviortree.guards;


import java.awt.*;
import botenanna.AgentInput;
import botenanna.behaviortree.Leaf;
import botenanna.behaviortree.MissingNodeException;
import botenanna.behaviortree.NodeStatus;

/** checks if the ball is close to a wall by taking the x and y value and comparing it to the coordinates of the ball and returns true if the ball is within it field*/

public class GuardIsBallNearWall extends Leaf {

    public GuardIsBallNearWall(String[] arguments) throws IllegalArgumentException {
        super(arguments);

        // Takes no arguments
        if (arguments.length != 0) throw new IllegalArgumentException();
    }

    @Override
    public void reset() {
        // Irrelevant
    }

    @Override
    public NodeStatus run(AgentInput input) throws MissingNodeException {


        if(input.BallIsWithinField(input.ball.getPosition().asVector2())){

            return NodeStatus.DEFAULT_SUCCESS;
            }
        return NodeStatus.DEFAULT_FAILURE;
        }
    }
