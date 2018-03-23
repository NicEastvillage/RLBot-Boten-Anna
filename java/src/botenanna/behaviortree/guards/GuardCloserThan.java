package botenanna.behaviortree.guards;

import botenanna.AgentInput;
import botenanna.ArgumentTranslator;
import botenanna.behaviortree.Leaf;
import botenanna.behaviortree.MissingNodeException;
import botenanna.behaviortree.NodeStatus;
import botenanna.math.Vector3;

import java.util.function.Function;


public class GuardCloserThan extends Leaf {
private Function<AgentInput, Object> fromA;
private Function<AgentInput, Object> pointB;
private Function<AgentInput, Object> fromC;


    /** The GuardCloserThan compares to Vector3 ands returns whether the distance between those are less than
     * a given distance. Can be inverted to check if distance is greater than instead.
     *
     * Its signature is {@code GuardCloserThan <fromA:Vector3> <to:Vector3> <fromC>}*/

    public GuardCloserThan(String[] arguments) throws IllegalArgumentException {
        super(arguments);

        if (arguments.length != 3) {
            throw new IllegalArgumentException();
        }

        fromA = ArgumentTranslator.get(arguments[0]);
        pointB = ArgumentTranslator.get(arguments[2]);
        fromC = ArgumentTranslator.get(arguments[1]);
    }
    @Override
    public void reset() {

    }

    @Override
    public NodeStatus run(AgentInput input) throws MissingNodeException {
        Vector3 A = (Vector3) fromA.apply(input);
        Vector3 B = (Vector3) pointB.apply(input);
        Vector3 C = (Vector3) fromC.apply(input);
        if (A.getDistanceTo(B)<C.getDistanceTo(B)) {
            return NodeStatus.DEFAULT_SUCCESS;
        }
        return NodeStatus.DEFAULT_FAILURE;
    }
}
