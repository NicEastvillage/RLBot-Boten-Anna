package botenanna.behaviortree.guards;

import botenanna.game.Arena;
import botenanna.game.Car;
import botenanna.game.Situation;
import botenanna.behaviortree.Leaf;
import botenanna.behaviortree.MissingNodeException;
import botenanna.behaviortree.NodeStatus;
import botenanna.math.RLMath;
import botenanna.math.Vector2;
import botenanna.math.Vector3;
import botenanna.physics.Rigidbody;

public class GuardHasGoalOpportunity extends Leaf {
    /**<p>The guard will try to determine whether the agent has a goal opportunity or not.
     * If the ball and the agent is approximately in the middle of the y axis there is a goal opportunity.
     * There is also a goal opportunity if the agents angle is towards the ball and the goal at the same time.</p>
     *
     * <p>The agent also have to be at the correct side relatively to the ball, for a goal opportunity to appear.</p>
     *
     * <p>It's signature is {@code GuardHasGoalOpportunity}</p>*/

    public GuardHasGoalOpportunity(String[] arguments) throws IllegalArgumentException {
        super(arguments);

        // Takes no arguments
        if (arguments.length != 0) throw new IllegalArgumentException();
    }

    @Override
    public void reset() {
        //Irrelevant
    }

    // Calculating goal opportunities whether the agent is Team Orange or Team Blue.
    @Override
    public NodeStatus run(Situation input) throws MissingNodeException {

        Car myCar = input.getMyCar();
        Rigidbody ball = input.getBall();
        Vector2 myPosition = myCar.getPosition().asVector2();
        Vector3 myRotation = myCar.getRotation();
        double angToBall = myCar.getAngleToBall();

        if(myCar.getTeam() == 1) {
            double rightGoalPost = RLMath.carsAngleToPoint(myPosition, myRotation.yaw, Arena.BLUE_GOALPOST_RIGHT);
            double leftGoalPost = RLMath.carsAngleToPoint(myPosition, myRotation.yaw, Arena.BLUE_GOALPOST_LEFT);
            if (myPosition.x <= 900 && myPosition.x >= -900 && ball.getPosition().x <= 900 && ball.getPosition().x >= -900 && myPosition.y >= ball.getPosition().y)
                return NodeStatus.DEFAULT_SUCCESS;
            if (angToBall < 0.5 && angToBall > -0.5 && rightGoalPost < 0.5 && leftGoalPost > -0.5)
                return NodeStatus.DEFAULT_SUCCESS;
        }

        if(myCar.getTeam() == 0) {
            double rightGoalPost = RLMath.carsAngleToPoint(myPosition, myRotation.yaw, Arena.RED_GOALPOST_RIGHT);
            double leftGoalPost = RLMath.carsAngleToPoint(myPosition, myRotation.yaw, Arena.RED_GOALPOST_LEFT);
            if (myPosition.x <= 900 && myPosition.x >= -900 && ball.getPosition().x <= 900 && ball.getPosition().x >= -900 && myPosition.y <= ball.getPosition().y)
                return NodeStatus.DEFAULT_SUCCESS;
            if (angToBall < 0.5 && angToBall > -0.5 && rightGoalPost < 0.5 && leftGoalPost > -0.5)
                return NodeStatus.DEFAULT_SUCCESS;
        }

        return NodeStatus.DEFAULT_FAILURE;
    }
}
