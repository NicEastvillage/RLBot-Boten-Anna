package botenanna.behaviortree.guards;

import botenanna.AgentInput;
import botenanna.ArgumentTranslator;
import botenanna.behaviortree.Leaf;
import botenanna.behaviortree.MissingNodeException;
import botenanna.behaviortree.NodeStatus;
import botenanna.math.Vector3;

import java.util.function.Function;

public class GuardIsPointBehind extends Leaf {

    private Function<AgentInput, Object> point;

    public GuardIsPointBehind(String[] arguments) throws IllegalArgumentException {
        super(arguments);

        if (arguments.length != 1){
            throw new IllegalArgumentException();
        }

        point = ArgumentTranslator.get(arguments[0]);

    }

    @Override
    public void reset() {
        // Irrelevant
    }

    @Override
    public NodeStatus run(AgentInput input) throws MissingNodeException {
        // Convert given input to a Vector
        Vector3 givenPoint = (Vector3) point.apply(input);

        return (input.whichSideOfPlane(givenPoint) < (Math.PI/2)) ? NodeStatus.DEFAULT_SUCCESS : NodeStatus.DEFAULT_FAILURE;
    }
}
