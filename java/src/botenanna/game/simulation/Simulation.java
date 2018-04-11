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

    /** Simulates the situation forward a step measured in seconds.
     * The simulatio, simulates the player car, enemy car, ball and boostpads to create a new situation
     * @return A new simulated situation
     **/
    public static Situation  simulate(Situation situation, double step, ActionSet action){
        if (step>0){
            throw new IllegalArgumentException("Step size must be more than zero. Current Step size is: "+step);
        }
        Ball simulatedBall = simulateBall(situation, step);
        Car simulatedMyCar = simulateCarActions(situation, action,  simulatedBall, step);
        Car simulatedEnemyCar = steppedCar(situation.enemyCar,  step);
        Boostpads simulatedBoostpads = simulateBoostpads(situation, simulatedEnemyCar, simulatedMyCar, step);

        return new Situation(simulatedMyCar, simulatedEnemyCar, simulatedBall , simulatedBoostpads);
    }

    /** Simulates  the boostpads, if any of the cars can pick up boost and they are stepped close to a pad deactivate them
     * @return an array of boostpads after simulation
     */
    private static Boostpads simulateBoostpads(Situation situation, Car enemy, Car myCar, double step) {
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
     * //TODO Add velocity loss on the ground
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
        Rigidbody placeholder = car.stepped(1*step);
        car.setPosition(placeholder.getPosition());
        car.setRotation(placeholder.getRotation());
        car.setVelocity(placeholder.getVelocity());
        car.setAcceleration(placeholder.getAcceleration());
     return car;
    }

    /** Simulates a car with actions **
     * @param situation The current situation in game
     * @param action the current actions from the Agent
     * @return a Car simulated forward in  the new situation     */
    public static Car simulateCarActions(Situation situation, ActionSet action, Ball ball, double step){

        //IF boost can and is used then max velocity and rate of acceleration is higher.
        boolean boosting = (action.isBoostDepressed() && situation.myCar.getBoost()!=0);
        double maxVel = boosting ? MAX_VELOCITY : MAX_VELOCITY_BOOST;

        //Cars and starting direction
        Car inputCar = situation.myCar;
        Car simulatedCar = situation.myCar;
        Vector3 direction = RLMath.carFrontVector(inputCar.rotation);
        double accelerationRate = (action.isBoostDepressed() && situation.myCar.getBoost()!=0) ? ACCELERATION_BOOST*step : inputCar.acceleration*step;
        double acceleration = accelerationRate*action.getThrottle();

        // Car steer simulation also set car yaw //  TODO add steer speed as function of the velocity
        if (action.getSteer()!=0 && (action.getThrottle()!=0 || simulatedCar.velocity.getMagnitude()!=0 || inputCar.isMidAir)){
            //If the car can and is turning, change the direction of the car and the simulated cars rotation
            direction.asVector2().turn((action.getSteer()*TURN_RATE)*step);
            simulatedCar.rotation.yaw += action.getSteer()*TURN_RATE*step;
        }

        // Car Pitch & Roll simulation SIMPLE VERSION  //TODO add roll and pitch speed/acceleration
        if (simulatedCar.isMidAir) {
            double roll = inputCar.getRotation().roll;
            double pitch = inputCar.getRotation().pitch;
            if (action.getRoll() != 0) {
                roll += action.getRoll()*step;
            }
            if (action.getPitch() != 0) {
                pitch += action.getPitch()*step;
            }
            simulatedCar.setRotation(new Vector3(pitch, inputCar.getRotation().yaw, roll));
        }
    
        //Add acceleration to the current velocity
        if ((boosting || action.getThrottle()>0) && inputCar.velocity.asVector2().getMagnitude()<maxVel){
            // If the car is sliding the car will keep moving in the direction of its original front vector
            if (action.isSlideDepressed()){
                simulatedCar.velocity.plus(situation.myCar.frontVector.getNormalized().scale(acceleration*step));
            }
            else simulatedCar.velocity.plus(direction.scale(acceleration*step));
        }
        else if (!boosting){
            if (action.getThrottle()<0){
                simulatedCar.velocity.plus(direction.scale(-acceleration*step));
            }
            if (action.getThrottle()==0 ){
                simulatedCar.getVelocity().scale(DECELERATION*step);
            }
        }

        if (simulatedCar.velocity.asVector2().getMagnitude()>0){
            simulatedCar = steppedCar(inputCar, step);
        }

        //Checks if the car has gained boost during the simulation
        Boolean bigBoost = false;
        Boostpads boost = simulateBoostpads(situation, situation.enemyCar ,situation.myCar, step);

        for (int i = 0; i>NUM_PADS ;i++){
            for (int j = 0; j>NUM_BIGBOOST; j++){//Checks if the boost is big
                if (boost.get(i).getKey().asVector2().equals(Boostpads.bigBoostPad[j])){
                    bigBoost = true;
                    break;
                }else bigBoost = false;
            }
            if (boost.get(i).getKey().getDistanceTo(simulatedCar.position)<20 && inputCar.getBoost()<100 && boost.get(i).getValue()){
                if (bigBoost){
                    simulatedCar.setBoost(100);
                }
                simulatedCar.setBoost(inputCar.getBoost()+12);
            }
        }
        return new Car(inputCar,simulatedCar.position,simulatedCar.velocity,simulatedCar.angularVelocity,simulatedCar.rotation,ball);
    }
}
