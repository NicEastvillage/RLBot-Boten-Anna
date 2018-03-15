package botenanna.behaviortree.guards;

import botenanna.AgentInput;
import botenanna.behaviortree.Leaf;
import botenanna.behaviortree.MissingNodeException;
import botenanna.behaviortree.NodeStatus;

public class GuardIsKickoff extends Leaf{

    /** <p> The GuardIsKickoff returns SUCCESS if there is a kickoff. </p>
     *  <p> It's signature is: {@code GuardIsKickoff}</p> */
    public GuardIsKickoff(String[] arguments) throws IllegalArgumentException {
        super(arguments);

        //Takes no arguments
        if(arguments.length != 0) throw new IllegalArgumentException();
    }

    @Override
    public void reset() {
        // Irrelevant
    }

    @Override
    public NodeStatus run(AgentInput input) throws MissingNodeException {

        if(input.ballLocation.x == 0 && input.ballLocation.y == 0){
            if(input.ballVelocity.x == 0 && input.ballVelocity.y == 0)
                return NodeStatus.DEFAULT_SUCCESS;
        }

        return NodeStatus.DEFAULT_FAILURE;
    }
}
