package botenanna.game.simulation;

import botenanna.Ball;
import botenanna.game.*;
import botenanna.math.RLMath;
import botenanna.math.Vector3;
import botenanna.physics.Rigidbody;
import javafx.util.Pair;
import java.util.ArrayList;
import static botenanna.game.Boostpads.*;
import static botenanna.game.Car.*;


public class Simulation {

    /** Simulates the car, ball, enemyCar and boostpads forward a step
     * @return A new simulated situation     */
    public static Situation  simulate(Situation situation, double step, ActionSet action){
        Ball simulatedBall = simulateBall(situation, step);
        Car simulatedMyCar = carWithActions(situation, action,  simulatedBall, step);
        Car simulatedEnemyCar = steppedCar(situation.enemyCar,  step);
        Boostpads simulatedBoostpadsList = simulateBoost(situation, simulatedEnemyCar, simulatedMyCar, step);
        return new Situation(simulatedMyCar, simulatedEnemyCar, simulatedBall , simulatedBoostpadsList);
    }

    /** Simulates  the boostpads, if any of the cars can pick up boost and they are stepped close to a pad deactivate them
     * @return an array of boostpads after simulation
     */
    private static Boostpads simulateBoost(Situation situation, Car enemy, Car myCar, double step) {
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
     * @return a new ball who has been stepped forward its path, if the ball is in the air the velocity will slow.
     * //TODO Add velocity loss on the ground and rotation
     */
    private static Ball simulateBall(Situation situation, double step)    {
       Vector3 pathPosition = situation.ball.getPath(step,100).getLastItem();

       if (situation.ball.getPosition().z>0 && pathPosition.z>0){
           return new Ball(pathPosition, situation.ball.getVelocity().scale(0.97*step), situation.ball.getRotation());
       }
        return new Ball(pathPosition,situation.ball.getVelocity(),situation.ball.getRotation());
    }

    /** Steppes the cars rigidbody forward 1 step
     * @param car is the car with no output
     * @return a simulated car                                    */
    private static Car steppedCar(Car car, double step) {
        Rigidbody copy = car.stepped(1*step);
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
    public static Car carWithActions(Situation situation, ActionSet action, Ball ball, double step){

        //AccelerationRate: if boost is used change acceleration to boostTier
        double accelerationRate = (action.isBoostDepressed() && situation.myCar.getBoost()!=0) ? ACCELERATION_BOOST/step : ACCELERATION/step;
        double acceleration = action.getThrottle()*(accelerationRate);
        Car startingCar = situation.myCar;
        Car simulatedCar = situation.myCar;
        Vector3 direction = RLMath.carFrontVector(startingCar.rotation);

        if (!situation.AgentIsWithinField(startingCar.getPosition().asVector2())){
            //TODO WALLRIDER!
        }
        // Car steer simulation
        if (action.getSteer()!=0 && (action.getThrottle()!=0 || startingCar.velocity.getMagnitude()!=0 || startingCar.isMidAir)){
            direction.asVector2().turn((action.getSteer()*TURN_RATE)*step);
            simulatedCar.rotation.yaw += action.getSteer()*TURN_RATE*step;
        }

        // Car Roll simulation SIMPLE VERSION
        if (startingCar.isMidAir  && action.getRoll()!=0){
            simulatedCar.setRotation(new Vector3(startingCar.getRotation().pitch,startingCar.getRotation().yaw,startingCar.rotation.roll+action.getRoll()));
        }
        // Car pitch simulation SIMPLE VERSION
        if (startingCar.isMidAir  && action.getPitch()!=0){
            simulatedCar.setRotation(new Vector3(startingCar.getRotation().pitch + action.getPitch(),startingCar.getRotation().yaw,startingCar.rotation.roll));
        }
        //Add acceleration to the current velocity
        if (action.getThrottle()!=0 && startingCar.velocity.asVector2().getMagnitude()<MAX_VELOCITY){
            //The simulated cars velocity plus, acceleration in the direction of the frontvector
            // If the car is sliding the car will keep moving in the direction of its original front vector
            if (action.isSlideDepressed()){
                simulatedCar.velocity.plus(situation.myCar.frontVector.getNormalized().scale(acceleration));
            }
            else simulatedCar.velocity.plus(direction.getNormalized().scale(acceleration));
        }
        else if (action.getThrottle()!=0){
            startingCar.velocity.plus(direction.getNormalized().scale(-acceleration));
        }
        if (startingCar.velocity.asVector2().getMagnitude()>0){
            //If the car is moving step it forward
            simulatedCar = steppedCar(startingCar, step);
        }

        //Check for boost  and adds 12 to boost if it is on a boost pad
        Boolean bigBoost = false;
        Boostpads boost = simulateBoost(situation, situation.enemyCar ,situation.myCar, step);

        for (int i = 0; i>NUM_PADS ;i++){
            for (int j = 0; j>NUM_BIGBOOST; j++){//Checks if the boost is big
                if (boost.get(i).getKey().asVector2().equals(Boostpads.bigBoostPad[j])){
                    bigBoost = true;
                    break;
                }else bigBoost = false;
            }
            if (boost.get(i).getKey().getDistanceTo(simulatedCar.position)<20 && startingCar.getBoost()<100 && boost.get(i).getValue()){
                if (bigBoost){
                    simulatedCar.setBoost(100);
                }
                simulatedCar.setBoost(startingCar.getBoost()+12);
            }
        }
        return new Car(startingCar,simulatedCar.position,simulatedCar.velocity,simulatedCar.angularVelocity,simulatedCar.rotation,ball);
    }
}
