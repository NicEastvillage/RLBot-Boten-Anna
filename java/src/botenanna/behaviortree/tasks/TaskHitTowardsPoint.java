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


    public TaskHitTowardsPoint(String[] arguments) throws IllegalArgumentException {
        super(arguments);
    }


    //old
    @Override
    public void reset() {
        // Irrelevant
    }

    @Override
    public NodeStatus run(AgentInput input) throws MissingNodeException {
        Vector2 aim = new Vector2(0,1000);
        // Finds the target based on the given aim
        Vector2 point =  input.ballLandingPosition.asVector2();
        double targetAngle = angleToTarget(input, aim);

       if (targetAngle==0){
           point = searchAngle(input,point,targetAngle);
       }else point = findCirclePoint(input, input.ballLandingPosition.asVector2());


        //Same as drive towards point, it will  turn toward the point and drive there.
        double ang = RLMath.carsAngleToPoint(input.myLocation.asVector2(), input.myRotation.yaw, point);
        // Smooth the angle to a steering amount - this avoids wobbling

        double steering = RLMath.steeringSmooth(ang);
        AgentOutput output = new AgentOutput().withAcceleration(1).withSteer(steering);

        //Sharp turning with slide
        if (ang > 2.0 || ang < -2.0) {
                output.withSlide();
            }
        //Speed boost with boost
        if (input.ballLocation.z<100 && input.myBoost>40){
            output.withBoost();
        }

        return new NodeStatus(Status.RUNNING, output,  this);
    }

    // TODO ADD PREDICTION ATM THE CAR DECIDES WHAT TO DO BASED ON itS CURRENT SITuAtiON AND NOT WHERE THE BALL AND CAR ARE SOING TO BE IN TIME:

    // Returns a vector point where if the car hits it it will shoot towards aim.
    double angleToTarget(AgentInput input, Vector2 target) {

        Vector2 ballPos = input.ballLandingPosition.asVector2();
        Vector2 ballV = input.ballVelocity.asVector2();
        Vector2 ballPoint = findCirclePoint(input, ballPos);

        // Direction of the force, vector  from point to ball CoM
        Vector2 direction = ballPoint.minus(ballPos);
        double directionMag = direction.getMagnitude();

        if (direction.isZero()) {
            directionMag = 1;
        }

        // Calculate it as a unit vector
        Vector2 unitVector = new Vector2(direction.x / directionMag, direction.y / directionMag);

        //Find the size of the cars max velocity TODO Adjust for boost potential.
        double carPotentialSpeed = 46;

        //Find the possible momentum.
        Vector2 maxMomentum = new Vector2(unitVector.x * carPotentialSpeed, unitVector.y * carPotentialSpeed);


        // Momentum vector gives the power from the current velocity to the direction of the ball
        Vector2 appliedForce = maxMomentum.plus(ballV);//maxMomentum.plus(input.ballVelocity.asVector2());

        // Finds the angle between the aim and the force TODO IT WORKS DO NOT TOUCH
        double atanForce = atan2(appliedForce.y - ballPos.y, appliedForce.x - ballPos.x);
        double atanTarget = atan2(target.y - ballPos.y, target.x - ballPos.x);
        double ang =  atanTarget - atanForce;

        if (ang < Math.PI) {
            //if the angle between the vectors is small enough then return the point to drive to.
            return ang;
        }

        return 0;
    }
    Vector2 searchAngle(AgentInput input, Vector2 target,  double angle){
        if (angle>0){

        }
        return target;
    }
    Vector2 findCirclePoint(AgentInput input, Vector2 ballPos){
        // The size of a circle point TODO Find the point on the circle to get a better angle
        double circlePoint = new Vector2(ballPos.x+94*cos(0),ballPos.y+94*cos(0)).getMagnitude();

        // Vector car to ball
        Vector2  carToBall = input.myLocation.asVector2().minus(ballPos);

        // Finds the unitvector of the car and adds the size of the circlepoint Circlevektor

        Vector2 carUnit = new Vector2((carToBall.x/carToBall.getMagnitude())*circlePoint,(carToBall.y/carToBall.getMagnitude())*circlePoint);

        //Finds the vector from
        Vector2 ballPoint = input.myLocation.asVector2().plus(carUnit.scale(-1));
        return ballPoint;
    }


}
