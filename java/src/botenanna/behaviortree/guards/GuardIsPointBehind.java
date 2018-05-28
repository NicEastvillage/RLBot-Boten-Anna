package botenanna.behaviortree.guards;

import botenanna.behaviortree.ArgumentTranslator;
import botenanna.behaviortree.Leaf;
import botenanna.behaviortree.MissingNodeException;
import botenanna.behaviortree.NodeStatus;
import botenanna.game.Situation;
import botenanna.math.Vector3;

import java.util.function.Function;

public class GuardIsPointBehind extends Leaf {

    private Function<Situation, Object> point;

    /**
     * The guard GuardIsPointBehind takes a Vector3 point and checks if the given point is behind the current car.
     * If the point is behind it will return SUCCESS and if not, it will return FAILURE.
     *
     * Its signature is: {@code GuardIsPointBehind <givenPoint:Vector3>} */
    public GuardIsPointBehind(String[] arguments) throws IllegalArgumentException {
        super(arguments);

        if (arguments.length != 1) throw new IllegalArgumentException();

        point = ArgumentTranslator.get(arguments[0]);
    }

    @Override
    public void reset() {
        // Irrelevant
    }

    @Override
    public NodeStatus run(Situation input) throws MissingNodeException {
        // Convert given input to a Vector
        Vector3 givenPoint = (Vector3) point.apply(input);

        return input.isPointBehindCar(input.myPlayerIndex, givenPoint) ? NodeStatus.DEFAULT_SUCCESS : NodeStatus.DEFAULT_FAILURE;
    }
}
