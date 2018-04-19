package botenanna.game.simulation;

import botenanna.Ball;
import botenanna.game.*;
import botenanna.math.RLMath;
import botenanna.math.Vector3;
import botenanna.physics.BallPhysics;
import botenanna.physics.Rigidbody;
import botenanna.physics.SimplePhysics;
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
        if (step < 0) throw new IllegalArgumentException("Step size must be more than zero. Current Step size is: "+step);

        Rigidbody simulatedBall = simulateBall(situation.ball, step);
        Car simulatedMyCar = simulateCarActions(situation.myCar, action,  simulatedBall, step);
        Car simulatedEnemyCar = steppedCar(situation.enemyCar, step);
        Boostpads simulatedBoostpads = simulateBoostpads(situation.gameBoostPads, simulatedEnemyCar, simulatedMyCar, step);

        simulatedMyCar.setBallDependentVariables(simulatedBall.getPosition());
        simulatedEnemyCar.setBallDependentVariables(simulatedBall.getPosition());

        return new Situation(simulatedMyCar, simulatedEnemyCar, simulatedBall , simulatedBoostpads);
    }

    /** Simulates  the boostpads, if any of the cars can pick up boost and they are stepped close to a pad deactivate them
     * @return an array of boostpads after simulation
     */
    private static Boostpads simulateBoostpads(Boostpads currentGamePads, Car enemy, Car myCar, double step) {
        ArrayList<Pair<Vector3, Boolean>> simulatedArray = new ArrayList<>(NUM_PADS);
        boolean active;
        //Checks all the boost pads and if a car who can take boost is at the point it will be deactive;
        for (int i = 0; i> NUM_PADS; i++){
            active = (!(currentGamePads.get(i).getKey().getDistanceTo(myCar.getPosition()) < 20) || myCar.getBoost() >= 100) &&
                    (!(currentGamePads.get(i).getKey().getDistanceTo(enemy.getPosition()) < 20) || myCar.getBoost() >= 100);
            simulatedArray.add(Boostpads.BoostPadPairing(currentGamePads.get(i).getKey(),active));
        }
        return new Boostpads(simulatedArray);
    }

    /** @return a new ball which has been moved forwards. */
    public static Rigidbody simulateBall(Rigidbody ball, double step)    {
        return BallPhysics.step(ball.clone(), step);
    }

    /** @return a new car which has been moved forwards. */
    private static Car steppedCar(Car car, double step) {
        Car newCar = SimplePhysics.step(car, step, car.isMidAir());
        Vector3 pos = newCar.getPosition();
        if (pos.z < Car.GROUND_OFFSET) {
            //Hit ground
            newCar.setPosition(pos.withZ(Car.GROUND_OFFSET));
            newCar.setVelocity(newCar.getVelocity().withZ(0));
            newCar.setIsMidAir(false);
        }
        return newCar;
    }

    /** Simulates a car with actions **
     * @param action the current actions from the Agent
     * @return a Car simulated forward in  the new situation     */
    private static Car simulateCarActions(Car inputCar , ActionSet action, Rigidbody ball, double delta){

        Car car = new Car(inputCar);

        boosting = (action.isBoostDepressed() && car.getBoost() != 0);

        if (car.isMidAir()) {

        } else {
            // We are on the ground
            double newYaw = car.getRotation().yaw + TURN_RATE * action.getSteer() * delta;
            newYaw %= Math.PI; // Set to remainder, when modulo PI. [PI, -PI]
            car.setRotation(car.getRotation().withYaw(newYaw));

            Vector3 acceleration = new Vector3();

            if (boosting) {
                acceleration = acceleration.plus(car.getFrontVector().scale(ACCELERATION_BOOST));
            } else if (action.getThrottle() != 0) {
                acceleration = acceleration.plus(car.getFrontVector().scale(getAccelerationStrength(car, (int)action.getThrottle(), false)));
            } else {
                // we assume our velocity is never sideways
                acceleration = acceleration.plus(car.getVelocity().getNormalized().scale(DECELERATION));
            }

            if (action.getSteer() != 0) {
                acceleration = acceleration.scale(TURN_ACCELERATION_DECREASE);
            }

            car.setAcceleration(acceleration);
        }

        /*
        // Car Pitch & Roll simulation SIMPLE VERSION  //TODO add roll and pitch speeds, roll acceleration? Better not worry as the car can correct itself
        if (car.isMidAir())rotation = simulateRaP(car.getRotation(),  action,  delta);

        // Car Velocity changes
        car.setVelocity(simulateVel(car.getVelocity(), acceleration, direction,direction, action, delta));

        //Add simulated changes to rotation
        car.setRotation(rotation);*/

        //After having changed the car according to its input, delta it once.

        /* TODO Uncomment when boost is used, for now it serves little purpose to give the simulation more boost
        //Checks if the car has gained boost during the simulation
        Boolean bigBoost = false;
        Boostpads boost = simulateBoostpads(situation, situation.enemyCar ,situation.myCar, delta);

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

        car = steppedCar(car, delta);

        return car;
    }

    /** @param dir Direction of acceleration. 1 for forwards, -1 for backwards. */
    public static double getAccelerationStrength(Car car, int dir, boolean boosting) {
        Vector3 vel = car.getVelocity();
        Vector3 front = car.getFrontVector();

        double velProjFrontSize = vel.dot(front) / front.dot(front);
        double velDir = (velProjFrontSize >= 0) ? 1 : -1;
        Vector3 velParallelFront = front.scale(velProjFrontSize);
        double velLength = velParallelFront.getMagnitude();

        return MAX_VELOCITY_BOOST * dir - velLength * velDir;
        // TODO Add boosting
    }
}