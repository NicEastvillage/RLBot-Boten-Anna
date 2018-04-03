package botenanna.behaviortree.guards;


import java.awt.*;
import botenanna.AgentInput;
import botenanna.behaviortree.Leaf;
import botenanna.behaviortree.MissingNodeException;
import botenanna.behaviortree.NodeStatus;

//checks if the ball is close to a wall

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
