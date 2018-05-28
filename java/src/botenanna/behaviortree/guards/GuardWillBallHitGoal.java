package botenanna.behaviortree.guards;

import botenanna.game.Situation;
import botenanna.behaviortree.ArgumentTranslator;
import botenanna.Ball;
import botenanna.behaviortree.Leaf;
import botenanna.behaviortree.MissingNodeException;
import botenanna.behaviortree.NodeStatus;
import botenanna.math.Vector3;
import botenanna.math.zone.Box;
import botenanna.physics.BallPhysics;
import botenanna.physics.SimplePhysics;

import java.util.function.Function;

public class GuardWillBallHitGoal extends Leaf {

    private Function<Situation, Object> areaFunc;

    /**
     * The guard GuardWillBallHitGoal checks if the path of the ball ends in the goal box using a simulation
     * of where the ball is when it reaches the wall. The guard returns SUCCESS when the ball has a path that
     * ends in the given box area. (Goal box) and returns FAILURE if it will not.
     *
     * Its signature is: {@code GuardWillBallHitGoal <boxArea:Box>}*/

    public GuardWillBallHitGoal(String[] arguments) throws IllegalArgumentException {
        super(arguments);

        if (arguments.length != 1) {
            throw new IllegalArgumentException();
        }

        areaFunc = ArgumentTranslator.get(arguments[0]);

    }

    @Override
    public void reset() {
        // Irrelevant
    }

    @Override
    public NodeStatus run(Situation situation) throws MissingNodeException {

        // Determine time it will take for ball to hit next Y-positive wall
        double time = SimplePhysics.predictArrivalAtWallYPositive(situation.getBall(), Ball.RADIUS);

        // Find position when hitting wall
        Vector3 destination = BallPhysics.step(situation.getBall(), time).getPosition();

        // Determine area
        Box boxArea = (Box) areaFunc.apply(situation);

        if (boxArea.isPointInBoxArea(destination)) {
            return NodeStatus.DEFAULT_SUCCESS;
        } else {
            return NodeStatus.DEFAULT_FAILURE;
        }
    }
}
