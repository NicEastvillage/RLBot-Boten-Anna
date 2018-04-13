package botenanna.behaviortree.guards;

import botenanna.AgentInput;
import botenanna.AgentOutput;
import botenanna.ArgumentTranslator;
import botenanna.Ball;
import botenanna.behaviortree.Leaf;
import botenanna.behaviortree.MissingNodeException;
import botenanna.behaviortree.NodeStatus;
import botenanna.behaviortree.Status;
import botenanna.math.Vector2;
import botenanna.math.Vector3;
import botenanna.math.zone.Box;
import botenanna.physics.Path;

import java.util.function.Function;

public class GuardWillBallHitGoal extends Leaf {

    private Function<AgentInput, Object> areaFunc;

    public GuardWillBallHitGoal(String[] arguments) throws IllegalArgumentException {
        super(arguments);

        if (arguments.length != 1){
            throw new IllegalArgumentException();
        }

        areaFunc = ArgumentTranslator.get(arguments[0]);

    }

    @Override
    public void reset() {
        // Irrelevant
    }

    @Override
    public NodeStatus run(AgentInput input) throws MissingNodeException {

        // Determine time it will take for ball to hit next Y-positive wall
        double time = input.ball.predictArrivalAtWallYPositive(Ball.RADIUS);

        // Find path of ball
        Path path = input.ball.getPath(time, 10);
        Vector3 finalDestination = path.getLastItem();

        // Determine area
        Box boxArea = (Box) areaFunc.apply(input);

        if (boxArea.isPointInBoxArea(finalDestination)) {
            System.out.println("Ball will hit goal box");
            return NodeStatus.DEFAULT_SUCCESS;
        } else {
            return NodeStatus.DEFAULT_FAILURE;
        }
    }
}
