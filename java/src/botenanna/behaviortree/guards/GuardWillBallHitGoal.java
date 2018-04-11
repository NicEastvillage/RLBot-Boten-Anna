package botenanna.behaviortree.guards;

import botenanna.AgentInput;
import botenanna.ArgumentTranslator;
import botenanna.Ball;
import botenanna.behaviortree.Leaf;
import botenanna.behaviortree.MissingNodeException;
import botenanna.behaviortree.NodeStatus;
import botenanna.math.Vector2;
import botenanna.math.Vector3;
import botenanna.physics.Path;

import java.util.function.Function;

public class GuardWillBallHitGoal extends Leaf {

    public GuardWillBallHitGoal(String[] arguments) throws IllegalArgumentException {
        super(arguments);

        if (arguments.length != 0){
            throw new IllegalArgumentException();
        }

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

        // TESTS
        // System.out.println(input.ball.clone().stepped(0).getPosition());
        System.out.println("Time: ["+time+"]"); // Works!
        path.analyze();
        /*for (int i = 0; i <= time; i++) {
            System.out.println("Evaluated " + i + ": ["+path.evaluate(i)+"]");
        }*/
  /*      System.out.println("Path: " + finalDestination); // Returns NaN € x,y,z*/
        System.out.println("");

        /*// Check if this vector is inside goal area (2D)
            // Orange goal (A,C € x,y) (Perpendicular)
        int Ax = -720, Ay = 5200;
        int Cx = 720, Cy = 4000;*/

        /*if (Ax < finalDestination.x && finalDestination.x < Cx
                && Ay < finalDestination.y && finalDestination.y < Cy) {
            System.out.println("Ball will hit goal box");
        } else {
            System.out.println("Ball will not hit goal box");
        }*/

        return NodeStatus.DEFAULT_SUCCESS;
    }
}
