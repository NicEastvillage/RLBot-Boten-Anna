package botenanna.behaviortree.guards;


import botenanna.behaviortree.Leaf;
import botenanna.behaviortree.MissingNodeException;
import botenanna.behaviortree.NodeStatus;
import botenanna.game.Situation;

public class GuardHasBallPossession extends Leaf {


    /** The guard GuardHasBallPossession returns SUCCESS if the agent has ball possession and FAILURE if it does not.
     *  The possession is determined by utility theory introduced in Situation.
     *
     *  It's signature is: {@code GuardHasBallPossession} */
    public GuardHasBallPossession(String[] arguments) throws IllegalArgumentException {
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

        return (input.hasPossession(input.myPlayerIndex)) ? NodeStatus.DEFAULT_SUCCESS : NodeStatus.DEFAULT_FAILURE;
    }
}
