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
    private static double maxVel;
    private static boolean boosting;


    /** Simulates the situation forward a step measured in seconds.
     * The simulatio, simulates the player car, enemy car, ball and boostpads to create a new situation
     * @return A new simulated situation
     **/
    public static Situation  simulate(Situation situation, double step, ActionSet action){
        if (step>0){
            throw new IllegalArgumentException("Step size must be more than zero. Current Step size is: "+step);
        }
        System.out.println("Simulating");
        Ball simulatedBall = simulateBall(situation.ball, step);
        System.out.println("Simulating Ball");
        Car simulatedMyCar = simulateCarActions(situation, action,  simulatedBall, step);
        System.out.println("Simulating My Car");
        Car simulatedEnemyCar = steppedCar(situation.enemyCar,  step);
        System.out.println("Simulating enemy Car");
        Boostpads simulatedBoostpads = simulateBoostpads(situation, simulatedEnemyCar, simulatedMyCar, step);
        System.out.println("Simulating Boost pads");

        return new Situation(simulatedMyCar, simulatedEnemyCar, simulatedBall , simulatedBoostpads);
    }

    /** Simulates  the boostpads, if any of the cars can pick up boost and they are stepped close to a pad deactivate them
     * @return an array of boostpads after simulation
     */
    private static Boostpads simulateBoostpads(Situation situation, Car enemy, Car myCar, double step) {
       Boostpads currentGamePads = situation.gameBoostPads;
       ArrayList<Pair<Vector3, Boolean>> simulatedArray = new ArrayList<>(NUM_PADS);
       boolean active;
       //Checks all the boost pads and if a car who an take boost is at the point it will be deactive;
        for (int i = 0; i> NUM_PADS; i++){
            active = (!(currentGamePads.get(i).getKey().getDistanceTo(myCar.position) < 20) || myCar.getBoost() >= 100) &&
                    (!(currentGamePads.get(i).getKey().getDistanceTo(enemy.position) < 20) || myCar.getBoost() >= 100);
            simulatedArray.add(Boostpads.BoostPadPairing(currentGamePads.get(i).getKey(),active));
        }
        return new Boostpads(simulatedArray);
    }

    /**
     * @return a new ball who has been stepped forward its path, if the ball is in the air the velocity will slow.
     * //TODO Add velocity loss on the ground
     */
    public static Ball simulateBall(Ball ball, double step)    {
       Vector3 pathPosition = ball.getPath(step,100).getLastItem();
       if (ball.getPosition().z>0 && pathPosition.z>0){
           return new Ball(pathPosition, ball.getVelocity().scale(0.97*step), ball.getRotation());
       }
        return new Ball(pathPosition,ball.getVelocity(),ball.getRotation());
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
    private static Car simulateCarActions(Situation situation, ActionSet action, Ball ball, double step){

        //Cars and starting direction
        Car simulatedCar = situation.myCar;
        Vector3 direction = RLMath.carFrontVector(simulatedCar.getRotation());
        double accelerationRate = (action.isBoostDepressed() && situation.myCar.getBoost()!=0) ? ACCELERATION_BOOST*step : simulatedCar.acceleration*step;
        double acceleration = accelerationRate*action.getThrottle();

        boosting = (action.isBoostDepressed() && simulatedCar.getBoost()!=0);
        maxVel = boosting ? MAX_VELOCITY : MAX_VELOCITY_BOOST;

        // Car steer simulation also set car yaw //  TODO add steer speed as function of the velocity - As in how many degress the car turns per second given a certain speed
        if (action.getSteer()!=0 && (action.getThrottle()!=0 || simulatedCar.getVelocity().getMagnitude()!=0 || simulatedCar.isMidAir)){
            //If the car can and is turning, change the direction of the car and the simulated cars rotation
            direction.asVector2().turn((action.getSteer()*TURN_RATE)*step);
            simulatedCar.rotation.yaw += action.getSteer()*TURN_RATE*step;
        }

        // Car Pitch & Roll simulation SIMPLE VERSION  //TODO add roll and pitch speeds, roll acceleration? Better not worry as the car can correct itself
        if (simulatedCar.isMidAir)simulatedCar.setRotation(simulateRaP(simulatedCar.getRotation(),  action,  step));

        // Car Velocity changes
        simulatedCar.setVelocity(simulateVel(simulatedCar.getVelocity(), acceleration, direction,direction, action, step));

        //After having changed the car according to its input, step it once.
        simulatedCar = steppedCar(simulatedCar, step);

        /* TODO Add when boost is used, for now it serves little purpose to give the simulation more boost
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
        }*/
        return new Car(situation.myCar,simulatedCar.position,simulatedCar.velocity,simulatedCar.angularVelocity,simulatedCar.rotation,ball);
    }

    /** Simulates the car's rotation roll and pitch based on the actionSet given
     * @return the simulated car but with a new rotation                     */
    private static Vector3 simulateRaP(Vector3 rotation,  ActionSet action, double step){
       if (action.getRoll() != 0) {
            rotation.roll += action.getRoll()*step;
       }
       if (action.getPitch() != 0) {
            rotation.pitch += action.getPitch()*step;
       }
        return rotation;
    }

    /** Simulates the cars velocity based on the actions and current acceleration
     * @return a velocity vector
     */
    private static Vector3 simulateVel(Vector3 velocity, double acceleration, Vector3 originalDirection, Vector3 direction, ActionSet action, double step){
        //Add acceleration to the current velocity
        if ((boosting || acceleration!=0) && velocity.asVector2().getMagnitude()<maxVel){
            // If the car is sliding the car will keep moving in the direction of its original front vector
            if (action.isSlideDepressed()){
               velocity = (velocity.plus(originalDirection.scale(acceleration*step)));
            }
            else velocity = velocity.plus(direction.scale(acceleration*step));
        }
        else if (!boosting){
            if (acceleration<0){
                velocity =  velocity.plus(direction.scale(acceleration*step));
            }
            if (acceleration==0 ){
                velocity = velocity.scale(DECELERATION*step);
            }
        }
        return velocity;
    }

}