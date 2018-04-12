package botenanna.behaviortree.guards;


import botenanna.game.Situation;
import botenanna.behaviortree.Leaf;
import botenanna.behaviortree.MissingNodeException;
import botenanna.behaviortree.NodeStatus;
import botenanna.math.Vector3;
import botenanna.math.zone.Box;

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
    public NodeStatus run(Situation situation) throws MissingNodeException {

        Box isPointInBox = new Box(situation.ball.getPosition(), new Vector3(-4080, -5080, 4060),new Vector3(4080,5080,0));

        if(isPointInBox.ballBox()){

            return NodeStatus.DEFAULT_SUCCESS;
            }
        return NodeStatus.DEFAULT_FAILURE;
        }
    }
