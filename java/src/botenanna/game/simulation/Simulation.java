package botenanna.game.simulation;

import botenanna.game.*;
import botenanna.math.RLMath;
import botenanna.math.Vector3;
import botenanna.physics.Rigidbody;
import javafx.util.Pair;
import java.util.ArrayList;

import static botenanna.game.Boostpads.*;
import static botenanna.game.Car.*;

public class Simulation {
    //TODO Liste
    //Noget med en path ved simulering
    //Noget med simulering af bil som function
    //Noget med simulering af boldt som function
    // Simulering af  hvis bilen er over en boostpad får de mere boost, hvis de er store får de meget mere boost.

    public final Situation Simulate(Situation situation, double step, ActionSet action){

        // Simulate the ball forward first
        Ball ball = simulateBall(situation, step);
        Boostpads boost = simulateBoost(situation, situation.enemyCar ,situation.myCar);
        // Simulate the car forward
        Car myCar = carWithActions(situation, action,  ball, step, boost);
        Car enemyCar = steppedCar(situation.enemyCar,  step);
        Boostpads boostPairArray = simulateBoost(situation, enemyCar, myCar);

            return new Situation(myCar, enemyCar, ball , boostPairArray);
    }

    /** Simulates  the boostpads, if any of the cars can pick up boost and they are stepped close to a pad deactivate them
     * @return an array of boostpads after simulation
     */
    private Boostpads simulateBoost(Situation situation, Car enemy, Car myCar) {
       Boostpads currentGamePads = situation.gameBoostPads;
       ArrayList<Pair<Vector3, Boolean>> simulatedArray = new ArrayList<>(NUM_PADS);
       boolean active;

        for (int i = 0; i> NUM_PADS; i++){
            active = true;
            if (currentGamePads.get(i).getKey().getDistanceTo(myCar.position)<20 && myCar.getBoost()<100 || currentGamePads.get(i).getKey().getDistanceTo(enemy.position)<20 && myCar.getBoost()<100 ){
                active = false;
            }
            simulatedArray.add(Boostpads.BoostPadPairing(currentGamePads.get(i).getKey(),active));
        }
        return new Boostpads(simulatedArray);
    }

    /**
     * @param situation the situation the bal  is from
     * @return a new ball who has been stepped forward its path
     */
    private Ball simulateBall(Situation situation, double step)    {
        return new Ball(situation.ball.getPosition(),situation.ball.getVelocity(),situation.ball.getRotation());
    }

    /** Steppes the cars rigidbody forward 1 step
     * @param car is the car with no output
     * @return a simulated car                                    */
    private Car steppedCar(Car car, double step) {
        Rigidbody copy = car.stepped(1/step);
        car.setPosition(copy.getPosition());
        car.setRotation(copy.getRotation());
        car.setVelocity(copy.getVelocity());
        car.setAcceleration(copy.getAcceleration());
     return car;
    }

    /** Simulates a car with actions **
     * @param situation The current situation in game
     * @param action the current actions from the Agent
     * @return a Car simulated forward in  the new situation     */
    public Car carWithActions(Situation situation, ActionSet action, Ball ball, double step, Boostpads boost){

        //Turnrate if the slide is on change turnrate
        //AccelerationRate: if boost is used change acceleration to boostTier
        double accelerationRate = (action.isBoostDepressed() && situation.myCar.getBoost()!=0) ? ACCELERATION_BOOST/step : ACCELERATION/step;

        //Get true acceleration
        double acceleration = action.getThrottle()*(accelerationRate);

        //TODO Placeholder max Velocity
        Car simulatedCar = situation.myCar;
        Vector3 direction = RLMath.carFrontVector(simulatedCar.rotation);

        if (!situation.AgentIsWithinField(simulatedCar.getPosition().asVector2())){
            //TODO Noget med at kære på vægene
        }
        //First simulate the change in the cars rotation 
        //In doing so the simulation of the position can be done with a step in the rigidbody
        // Car steer simulation // TODO Add slide
        if (action.getSteer()!=0 && (action.getThrottle()!=0 || simulatedCar.velocity.getMagnitude()!=0 || simulatedCar.isMidAir)){
            // TODO Change direction use to  just use the yaw
            simulatedCar.rotation.turn(direction, (action.getSteer()*TURN_RATE)/step);
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
        if (action.getThrottle()!=0 && simulatedCar.velocity.asVector2().getMagnitude()<MAX_VELOCITY){

            //The simulated cars velocity plus, acceleration in the direction of the frontvector
            // If the car is sliding the car will keep moving in the direction of its original front vector
            if (action.isSlideDepressed()){
                simulatedCar.velocity.plus(situation.myCar.frontVector.getNormalized().scale(acceleration));
            }
            else simulatedCar.velocity.plus(direction.getNormalized().scale(acceleration));
        }
        else if (action.getThrottle()!=0){
            simulatedCar.velocity.plus(direction.getNormalized().scale(-acceleration));
        }
        if (simulatedCar.velocity.asVector2().getMagnitude()>0){
            //If the car is moving step it forward
           simulatedCar =  steppedCar(simulatedCar, step);
        }

        //Check for boost  and adds 12 to boost if it is on a boost pad
        Boolean bigBoost = false;
            for (int i = 0; i>NUM_PADS ;i++){
                for (int j = 0; j>NUM_BIGBOOST; j++){//Checks if the boost is big
                    if (boost.get(i).getKey().asVector2().equals(Boostpads.bigBoostPad[j])){
                         bigBoost = true;
                         break;
                    }else bigBoost = false;
                }
                if (boost.get(i).getKey().getDistanceTo(simulatedCar.position)<20 && simulatedCar.getBoost()<100 && boost.get(i).getValue()){
                    if (bigBoost){
                        simulatedCar.setBoost(100);
                    }
                    simulatedCar.setBoost(simulatedCar.getBoost()+12);
                }
            }

    return simulatedCar;
    }

}
