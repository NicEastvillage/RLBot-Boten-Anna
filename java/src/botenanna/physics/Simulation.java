package botenanna.physics;

import botenanna.AgentOutput;
import botenanna.Car;
import botenanna.Situation;
import botenanna.Ball;
import botenanna.math.Vector2;
import botenanna.math.Vector3;

import java.util.List;

public class Simulation {
    //TODO Liste
    //Noget med en path ved simulering
    //Noget med simulering af bil som function
    //Noget med simulering af boldt som function
    //Noget med en ny branch
    //Noget med parsing af AgentOutput
  /*  public Situation(int myPlayerIndex, int enemyPlayerIndex, Situation.Car myCar, Situation.Car enemyCar, Ball ball, double ballLandingTime, Vector3 ballLandingPosition, boolean gameIsKickOffPause, boolean gameIsMatchEnded, boolean gameIsOvertime, boolean gameIsRoundActive, int gamePlayerCount) {
*/
    public final Situation Simulation(Situation Situation, int step, AgentOutput output){

        Car myCar = simulateCarWOutput(Situation, output);
        Car enemyCar = simulateCarNoOutput(Situation.enemyCar);
        Ball ball = simulateBall(Situation);
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



    public Car simulateCarWOutput(Situation input, AgentOutput output){

        //Add Setters for Car class
        Car predictCar = input.myCar;
        Vector3 startingVel = predictCar.velocity;
        Vector3 startingPos = predictCar.position;
        Vector3 startingRot = predictCar.rotation;
        //TODO PLACEHOLDER
        double Acceleration=1;
        int timesASec = 100;

        double acceleration = output.getAcceleration()*Acceleration/timesASec;//TODO add madices acceleration math.
        double maxVelocity = 28.2;
        Vector3 predictVel = startingVel;
        Vector3 predictRot = startingRot;
        Vector3 predictPos = startingPos;
        Vector3 direction = predictCar.frontVector;

        // Currently adds the acceleration until max, Predicts position to t - First if tests if the car got acceleration running, if not then if the speed is non zero then it predicts its path.
            if (output.getAcceleration()!=0){
                if (predictVel.getMagnitude()<maxVelocity)
                predictVel.plus(direction.getNormalized().scale(acceleration));
            }else if (startingVel.getMagnitude()>0){
                predictVel.plus(startingVel.scale(-timesASec));
            }

        // Car steer simulation // TODO Add slide
            if (output.getSteer()!=0 && (output.getAcceleration()!=0 || output.getDeceleration()!=0 || startingVel.getMagnitude()!=0 || predictCar.isMidAir)){
                direction.angle(direction, output.getSteer()/timesASec);
            }

        // Car Roll simulation
            if (predictCar.isMidAir  && output.getRoll()!=0){
            }

        // Car pitch simulation
            if (predictCar.isMidAir  && output.getPitch()!=0){
            }
        // Car deceleration
            if (output.getDeceleration()!=0){
            }


         //Updates the car pos and rotation
        predictCar.position = predictPos.plus(predictVel);
        predictCar.position = predictPos.plus(predictVel);
        predictCar.rotation = predictRot.plus(predictRot);
        predictCar.velocity = predictVel;



    return predictCar;

    }

}
