package botenanna.behaviortree.guards;

import botenanna.ArgumentTranslator;
import botenanna.Ball;
import botenanna.behaviortree.Leaf;
import botenanna.behaviortree.MissingNodeException;
import botenanna.behaviortree.NodeStatus;
import botenanna.behaviortree.Status;
import botenanna.game.Situation;
import botenanna.math.Vector2;
import botenanna.math.Vector3;
import botenanna.math.zone.Box;
import botenanna.physics.Path;

import java.util.function.Function;

public class GuardIsPointInsideBox extends Leaf {

    private Function<Situation, Object> givenPointFunc;
    private Function<Situation, Object> areaFunc;

    public GuardIsPointInsideBox(String[] arguments) throws IllegalArgumentException {
        super(arguments);

        if (arguments.length != 2){
            throw new IllegalArgumentException();
        }

        givenPointFunc = ArgumentTranslator.get(arguments[0]);
        areaFunc = ArgumentTranslator.get(arguments[1]);

    }

    @Override
    public void reset() {
        // Irrelevant
    }

    @Override
    public NodeStatus run(Situation input) throws MissingNodeException {

        // Determine point and area
        Vector3 givenPoint = (Vector3) givenPointFunc.apply(input);
        Box boxArea = (Box) areaFunc.apply(input);

        if (boxArea.isPointInBoxArea(givenPoint)) {
            return NodeStatus.DEFAULT_SUCCESS;
        } else {
            return NodeStatus.DEFAULT_FAILURE;
        }
    }
}
