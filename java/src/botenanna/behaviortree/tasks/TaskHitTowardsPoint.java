package botenanna.behaviortree.tasks;

import botenanna.AgentInput;
import botenanna.AgentOutput;
import botenanna.ArgumentTranslator;
import botenanna.behaviortree.*;
import botenanna.math.RLMath;
import botenanna.math.Vector2;
import botenanna.math.Vector3;

import java.util.Vector;
import java.util.function.Function;

import static java.lang.Math.acos;
import static java.lang.Math.atan2;
import static java.lang.Math.cos;


public class TaskHitTowardsPoint extends Leaf{

    private Function<AgentInput, Object> pointFunc;
    private static final double SLIDE_ANGLE = 1.7;
    private static double carPotentialSpeed = 1300;
    private boolean withBoost = true;
    private double precision = 1;

    /**<p>TaskHitTowardsPoint makes the agent try to hit the ball towards a given  point, if possible. If this is not possible it will try to find a better angle
     *The agent will simulate how much into the future it should predict.
     *This way the agent will be able to predict and hit the ball.</p>
     *
     * <p> The agent will try to drive towards a vector point on the ball that can shoot  the ball in the desired direction.
     * Because this is not always the case the again will also try to go to the oposit point of the ball and the direction point to get a better angle
     * The agent will try to get on the right side of the ball but will not drive around it,
     * so it needs to be protected towards aiming towards its own goal while adjusting for the angle</p>
     *
     * <p>It's signature is {@code TaskHitTowardsPoint <Vector3> <double>}</p>*/

    public TaskHitTowardsPoint(String[] arguments) throws IllegalArgumentException {
        super(arguments);
        if (arguments.length == 0 || arguments.length > 2) {
            throw new IllegalArgumentException();
        }
        pointFunc = ArgumentTranslator.get(arguments[0]);

        if (arguments.length > 1) {
            precision = Double.parseDouble(arguments[1]);
        }
    }
    //old
    @Override
    public void reset() {
        // Irrelevant
    }

    @Override
    public NodeStatus run(AgentInput input) throws MissingNodeException {

        //TODO Change current target to target aquired though arguments
        //TODO Version 2 add 3d and expand on circlePoint
        //Target point
        Vector3 target = (Vector3) pointFunc.apply(input);
        target =  target.plus(input.ballLandingPosition);

        //Ball
        Vector2 ballPos = input.ballLandingPosition.plus(input.ballVelocity.scale(input.getCollisionTime())).asVector2();
        Vector3 ballVelocity = input.ballVelocity;
        //Car
        Vector3 myPos = input.myLocation;
        //CirclePoint
        Vector2 point =  findCirclePoint(myPos,ballPos);

        //Do not boost while  finding angle
       if (!angleToTarget(ballPos, ballVelocity, point, target.asVector2())){
           point = searchAngle(input,point);
           withBoost=false;
       }

        //Same as drive towards point, it will  turn toward the point and drive there.
        double ang = RLMath.carsAngleToPoint(input.myLocation.asVector2(), input.myRotation.yaw, point);
        double steering = RLMath.steeringSmooth(ang);
        AgentOutput output = new AgentOutput().withAcceleration(1).withSteer(steering);
        //Sharp turning with slide
        if (ang > SLIDE_ANGLE || ang < -SLIDE_ANGLE) {
                output.withSlide();
            }
        //Speed boost towards hit
        if (input.myVelocity.asVector2().getMagnitude()<carPotentialSpeed
                && RLMath.carsAngleToPoint(ballPos, input.myRotation.yaw,input.ballLandingPosition.asVector2())>0.2
                && withBoost){
            output.withBoost();
        }

            return new NodeStatus(Status.RUNNING, output,  this);
    }

    /**  Checks if the angle to the ball is within the precision angle range
     * @param ballPos is the current position of the ball
     * @param ballV is the current velocity of the ball
     * @param ballPoint is a point on the ball½
     * @param target is the target for Task to hit towards
     * @return True if the angle between target and appliedForce is less than precision.
     */
    boolean angleToTarget(Vector2 ballPos, Vector3 ballV, Vector2 ballPoint, Vector2 target) {

        // Direction of the force, vector  from point to ball CoM - Not currently that important but usefull for more dimensions
        Vector2 direction = ballPoint.minus(ballPos).getNormalized();

        // The maximum momentum the car can generate
        Vector2 maxMomentum = direction.scale(carPotentialSpeed);
        Vector2 appliedForce = maxMomentum.plus(ballV.asVector2());

        // The angle between the aim and the balls new direction
        double atanForce = atan2(appliedForce.y - ballPos.y, appliedForce.x - ballPos.x);
        double atanTarget = atan2(target.y - ballPos.y, target.x - ballPos.x);
        double ang =  atanTarget - atanForce;

        if (ang > Math.PI) ang -= 2 * Math.PI;
            return ang <= precision;
    }

    /** Searches for vector2 point on the far side of the target and ball velocity vectors
     * @return a vector2 on the far side of the target and velocity scaled based on the distance/velocity to the ball.
     */
    Vector2 searchAngle(AgentInput input, Vector2 target){
        //Ball
        Vector2 ballPos = input.ballLandingPosition.asVector2();
        Vector2 ballV = input.ballVelocity.asVector2();
            return ballPos.plus(ballV.plus(target).getNormalized().scale(-1*((input.myDistanceToBall*400)/(input.ballVelocity.getMagnitude()+1))*input.getGoalDirection(input.myPlayerIndex)));
    }

    /** Finds a vector2 point on the ball*
     * @return  a Vector2 point on the ball in the direction of the car.
     */
    Vector2 findCirclePoint(Vector3 myPos, Vector2 ballPos){
        // TODO Can be expanded to find specific points and predict the balls movement more closely
        // The size of a circle point
        double circlePoint = new Vector2(ballPos.x+40*cos(0),ballPos.y+40*cos(0)).getMagnitude();
        Vector2  carToBall = myPos.asVector2().minus(ballPos).getNormalized();
        Vector2 carUnit = carToBall.scale(circlePoint);

            return ballPos.plus(carUnit.scale(-1));
    }

}