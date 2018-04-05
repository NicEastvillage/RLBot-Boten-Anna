package botenanna.game.simulation;

import botenanna.game.ActionSet;
import botenanna.game.Car;
import botenanna.game.Situation;
import botenanna.game.Ball;
import botenanna.math.RLMath;
import botenanna.math.Vector3;

public class Simulation {
    //TODO Liste
    //Noget med en path ved simulering
    //Noget med simulering af bil som function
    //Noget med simulering af boldt som function
    private final int timesASec = 100;


    public final Situation Simulate(Situation Situation, int step, ActionSet action){



    // Simulate the ball forward first
        Ball ball = simulateBall(Situation);
    // Simulate the car forward
        Car myCar = simulateCarWOutput(Situation, action,  ball);
        Car enemyCar = simulateCarNoOutput(Situation.enemyCar);
        //List<Vector2> boost = simulateBoost(Situation);
        return new Situation(myCar, enemyCar,ball);
    }

    private Ball simulateBall(Situation input)    {
        return new Ball(input.ball.getPosition(),input.ball.getVelocity(),input.ball.getRotation());
    }
    
    private void simulateBoost(Situation input) {
        return;
    }
    
    private Car simulateCarNoOutput(Car car) {
        car.setPosition(car.position.plus(new Vector3(0,0,1)));
     return car;
    }

    public Car simulateCarWOutput(Situation input, ActionSet action, Ball ball){
        // FIXME: Every occurrence of "getThrottle()" used to be "getAcceleration()" and might not be exactly the same

        //Turnrate if the slide is on change turnrate
        double turnRate =  action.isSlideDepressed() ?  25 : 50;
        //AccelerationRate: if boost is used change acceleration to boostTier
        double accelerationRate = (action.isBoostDepressed() && input.myCar.getBoost()!=0) ? 2/timesASec : 1/timesASec;
        double acceleration = action.getThrottle()/(accelerationRate);
        //TODO Placeholder max Velocity
        double maxVelocity = 28.2;

        Car simulatedCar = input.myCar;
        Vector3 direction = RLMath.carFrontVector(simulatedCar.rotation);

        //First simulate the change in the cars rotation 
        //In doing so the simulation of the position can be done with a step in the rigidbody
        // Car steer simulation // TODO Add slide
        if (action.getSteer()!=0 && (action.getThrottle()!=0 || simulatedCar.velocity.getMagnitude()!=0 || simulatedCar.isMidAir)){
            // The simulated cars rotation, rotated in the direction of steer per times per second.//TODO Change direction use to  just use the yaw
            simulatedCar.rotation.angle(direction, action.getSteer()*turnRate/timesASec);
        }

        // Car Roll simulation SIMPLE VERSION
        if (simulatedCar.isMidAir  && action.getRoll()!=0){
            simulatedCar.setRotation(new Vector3(simulatedCar.getRotation().pitch,simulatedCar.getRotation().yaw,simulatedCar.rotation.roll+action.getRoll()));
        }
        // Car pitch simulation SIMPLE VERSION
        if (simulatedCar.isMidAir  && action.getPitch()!=0){
            simulatedCar.setRotation(new Vector3(simulatedCar.getRotation().pitch + action.getPitch(),simulatedCar.getRotation().yaw,simulatedCar.rotation.roll));
        }

        //Then if the car is accelerating, add the acceleration rate to the current velocity
        //Else step the pos one.
        // FIXME: following if-statements are wrong. getThrottle() used to be getAcceleration() and getDeceleration()
        if (action.getThrottle()!=0 && simulatedCar.velocity.asVector2().getMagnitude()<maxVelocity){
            //The simulated cars velocity plus, acceleration in the direction of the frontvector
            simulatedCar.velocity.plus(direction.getNormalized().scale(acceleration));
        }
        // Car Deceleration //TODO Check break speed/ deceleration speed
        else if (action.getThrottle()!=0){
            simulatedCar.velocity.plus(direction.getNormalized().scale(-acceleration));
        }
        if (simulatedCar.velocity.asVector2().getMagnitude()>0){
            //If the car is moving step it forward
            simulatedCar.stepped(timesASec);
        }


    return simulatedCar;

    }

}
