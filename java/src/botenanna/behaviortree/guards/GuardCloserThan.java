package botenanna.behaviortree.guards;

import botenanna.game.Situation;
import botenanna.ArgumentTranslator;
import botenanna.behaviortree.Leaf;
import botenanna.behaviortree.MissingNodeException;
import botenanna.behaviortree.NodeStatus;
import botenanna.math.Vector3;

import java.util.function.Function;


public class GuardCloserThan extends Leaf {
private Function<Situation, Object> to;
private Function<Situation, Object> fromA;
private Function<Situation, Object> fromB;

    /** The GuardCloserThan compares 3 Vector3 and returns whether the distance between those are less than
     * a given distance. Can be inverted to check if distance is greater than instead.
     *
     * Its signature is {@code GuardCloserThan <to:Vector3> <fromA:Vector3> <fromB:Vector3>}*/

    public GuardCloserThan(String[] arguments) throws IllegalArgumentException {
        super(arguments);

        if (arguments.length != 3) {
            throw new IllegalArgumentException();
        }

        to = ArgumentTranslator.get(arguments[0]);
        fromA = ArgumentTranslator.get(arguments[1]);
        fromB = ArgumentTranslator.get(arguments[2]);
    }
    @Override
    public void reset() {

    }

    @Override
    public NodeStatus run(Situation input) throws MissingNodeException {
        Vector3 A = (Vector3) fromA.apply(input);
        Vector3 B = (Vector3) fromB.apply(input);
        Vector3 C = (Vector3) to.apply(input);
        if (A.getDistanceTo(C)<B.getDistanceTo(C)) {
            return NodeStatus.DEFAULT_SUCCESS;
        }
        return NodeStatus.DEFAULT_FAILURE;
    }
}
