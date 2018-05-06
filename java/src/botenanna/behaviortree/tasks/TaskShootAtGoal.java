package botenanna.behaviortree.tasks;

import botenanna.game.Car;
import botenanna.game.Situation;
import botenanna.game.ActionSet;
import botenanna.behaviortree.*;
import botenanna.math.RLMath;
import botenanna.math.Vector2;
import botenanna.math.Vector3;
import botenanna.physics.Rigidbody;

public class TaskShootAtGoal extends Leaf {

    /**<p>Make the agent try to shoot the ball towards the enemy goal, if possible. The agent will simulate how much into the future it should predict.
     *This way the agent will be able to predict and hit the ball towards the opponents goal.</p>
     *
     * <p> The agent will always try to drive towards a vector point that should be able to shoot the ball towards goal,
     * because of this, the agent needs to be at the correct position relative to the ball, else the agent can shoot
     * the ball towards its own goal.</p>
     *
     * <p>It's signature is {@code TaskShootAtGoal}</p>*/

    public TaskShootAtGoal(String[] arguments) throws IllegalArgumentException {
        super(arguments);

        // Takes no arguments
        if (arguments.length != 0) throw new IllegalArgumentException();
    }

    @Override
    public void reset() {
        // Irrelevant
    }

    @Override
    public NodeStatus run(Situation input) throws MissingNodeException {

        Rigidbody ball = input.getBall();
        Car myCar = input.getMyCar();

        Vector3 expectedBall;
        double predictSeconds = 0;
        double predict = 0.02;
        double counter = 0.02;
        double velocity;
        boolean isBallStill = false;

        //If the ball is really slow or still, skip the loop and don't predict.
        if(10 > ball.getVelocity().getMagnitude()){
            isBallStill = true;
        }

        //The loop will find a spot where the distance of expected ball to car minus the carvelocity multiplied by predict is between -25 and 25.
        //That way the agent should always be able to choose the right amount of prediction seconds, although this will probably change a little bit every tick as
        //the carvelocity changes.
        while(predictSeconds < 0.02 && counter <= 5 && !isBallStill){
            expectedBall = ball.getPosition().plus(ball.getVelocity().scale(predict));

            // If the car is not really driving, it should overextend its prediction to the future.
            if (myCar.getVelocity().getMagnitude() < 800){
                velocity = 800;
            }
            else velocity = myCar.getVelocity().getMagnitude();

            if (-25 < expectedBall.minus(myCar.getPosition().plus(myCar.getFrontVector().scale(70))).getMagnitude() - velocity*predict && expectedBall.minus(myCar.getPosition().plus(myCar.getFrontVector().scale(70))).getMagnitude() - velocity*predict < 25) {
                predictSeconds = predict;
            }

            predict += 0.02;
            counter += 0.02;
        }

        // If it runs through loop without choosing a prediction, then don't predict (Probably not needed)
        if(counter > 5) {
            predictSeconds = 0;
        }

        // if ball is still, don't predict
        if (isBallStill){
            predictSeconds = 0;
        }

        //double predictSeconds = input.getCollisionTime();
        Vector3 expectedBallLocation = ball.getPosition().plus(ball.getVelocity().scale(predictSeconds));

        Vector2 middleOfGoal;

        // Chooses opponents goal
        if (myCar.getTeam() == 1) {
            middleOfGoal = new Vector2(0,-5200);
        }
        else {
            middleOfGoal = new Vector2(0,5200);
        }

        //Places a vector at ball, this is the location the agent is supposed to hit the ball for scoring
        //Note, collision with ball with high velocity is without consideration
        middleOfGoal = middleOfGoal.minus(expectedBallLocation.asVector2());
        middleOfGoal = middleOfGoal.getNormalized();
        middleOfGoal = middleOfGoal.scale(-80);
        middleOfGoal = middleOfGoal.plus(expectedBallLocation.asVector2());


        // Get the needed positions and rotations
        Vector3 myPos = myCar.getPosition().plus(myCar.getFrontVector().scale(70));
        Vector3 myRotation = myCar.getPosition();

        double ang = 0;


        ang = RLMath.carsAngleToPoint(myPos.asVector2(), myRotation.yaw, middleOfGoal);

        // Smooth the angle to a steering amount - this avoids wobbling
        double steering = RLMath.steeringSmooth(ang);


        //When the agent should boost
        boolean boost = false;

        if(myCar.getBoost() > 30 && 1400 > expectedBallLocation.asVector2().minus(myPos.asVector2()).getMagnitude() && 1.5 > myCar.getAngleToBall() && myCar.getAngleToBall() > -1.5) {
            boost = true;
        }

        return new NodeStatus(Status.RUNNING, new ActionSet().withThrottle(1).withSteer(steering).withBoost(boost), this);
    }
}
