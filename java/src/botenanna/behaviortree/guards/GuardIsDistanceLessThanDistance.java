package botenanna.behaviortree.guards;

import botenanna.behaviortree.ArgumentTranslator;
import botenanna.behaviortree.Leaf;
import botenanna.behaviortree.MissingNodeException;
import botenanna.behaviortree.NodeStatus;
import botenanna.game.Situation;
import botenanna.math.Vector3;

import java.util.function.Function;

public class GuardIsDistanceLessThanDistance extends Leaf {

    private Function<Situation, Object> fromVecA;
    private Function<Situation, Object> toVecB;

    private Function<Situation, Object> fromVecC;
    private Function<Situation, Object> toVecD;

    /** The GuardIsDistanceLessThanDistance compares the distance between two vectors with another two vectors
     * and returns true if the first two vectors distance is less than the last two.
     *
     * Its signature is {@code GuardIsDistanceLessThanDistance <fromVecA:Vector3> <toVecB:Vector3> <fromVecC:Vector3> <toVecD:Vector3>} */
    public GuardIsDistanceLessThanDistance (String[] arguments) throws IllegalArgumentException {
        super(arguments);

        if (arguments.length != 4) throw new IllegalArgumentException();

        fromVecA = ArgumentTranslator.get(arguments[0]);
        toVecB = ArgumentTranslator.get(arguments[1]);

        fromVecC = ArgumentTranslator.get(arguments[2]);
        toVecD = ArgumentTranslator.get(arguments[3]);
    }

    @Override
    public void reset() {
        // Irrelevant
    }

    @Override
    public NodeStatus run(Situation input) throws MissingNodeException {

        // Get points
        Vector3 fromA = (Vector3) fromVecA.apply(input);
        Vector3 toB = (Vector3) toVecB.apply(input);

        Vector3 fromC = (Vector3) fromVecC.apply(input);
        Vector3 toD = (Vector3) toVecD.apply(input);

        // Compare distance
        double distFromAtoB = fromA.minus(toB).getMagnitudeSqr();
        double distFromCtoD = fromC.minus(toD).getMagnitudeSqr();

        // Returns success if distance from A to B is less than C to D.
        if (distFromAtoB <= distFromCtoD) return NodeStatus.DEFAULT_SUCCESS;
        return NodeStatus.DEFAULT_FAILURE;
    }
}
