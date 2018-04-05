package botenanna.behaviortree.guards;

import botenanna.Situation;
import botenanna.ArgumentTranslator;
import botenanna.behaviortree.Leaf;
import botenanna.behaviortree.MissingNodeException;
import botenanna.behaviortree.NodeStatus;
import botenanna.math.Vector3;

import java.util.function.Function;

public class GuardIsDistanceLessThan extends Leaf {

    private Function<Situation, Object> toFunc;
    private Function<Situation, Object> fromFunc;
    private double distance;

    /** The GuardIsDistanceLessThan compares to Vector3 ands returns whether the distance between those are less than
     * a given distance. Can be inverted to check if distance is greater than instead.
     *
     * Its signature is {@code GuardIsDistanceLessThan <from:Vector3> <to:Vector3> <dist:DOUBLE>}*/
    public GuardIsDistanceLessThan(String[] arguments) throws IllegalArgumentException {
        super(arguments);

        if (arguments.length != 3) {
            throw new IllegalArgumentException();
        }

        fromFunc = ArgumentTranslator.get(arguments[0]);
        toFunc = ArgumentTranslator.get(arguments[1]);
        distance = Double.parseDouble(arguments[2]);
    }

    @Override
    public void reset() {
        // Irrelevant
    }

    @Override
    public NodeStatus run(Situation input) throws MissingNodeException {

        // Get points
        Vector3 from = (Vector3) fromFunc.apply(input);
        Vector3 to = (Vector3) toFunc.apply(input);

        // Compare distance
        double dist = from.minus(to).getMagnitudeSqr();
        if (dist <= distance * distance) return NodeStatus.DEFAULT_SUCCESS;
        return NodeStatus.DEFAULT_FAILURE;
    }
}
