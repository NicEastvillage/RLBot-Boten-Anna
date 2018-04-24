package botenanna.game;

import botenanna.Ball;
import botenanna.math.RLMath;
import botenanna.math.Vector2;
import botenanna.math.Vector3;
import botenanna.math.zone.Box;
import botenanna.physics.Rigidbody;
import botenanna.physics.SimplePhysics;
import botenanna.physics.TimeTracker;
import rlbot.api.GameData;

import java.util.List;

//TODO: add isUpsideDown, myGoal (2dVEC), myGoalLine (2dVEC)

/** This manages the input (packet).
 *  It handles calculations and other measures. */
public class Situation {

    public static final double ARENA_LENGTH = 10280;
    public static final double MAX_VELOCITY_NO_BOOST = 1410.1;
    public static final double ARENA_WIDTH = 8240;
    public static final Vector3 BLUE_GOAL_BOX = Vector3.BACKWARDS.scale(5000);
    public static final Vector3 ORANGE_GOAL_BOX = Vector3.FORWARD.scale(5000);
    public static final Box ORANGE_GOAL_BOX_AREA = new Box(new Vector3(-720 , 5200 , 0), new Vector3(720 , 4000 , 1000));
    public static final Box BLUE_GOAL_BOX_AREA = new Box(new Vector3(-720 , -5200 , 0), new Vector3(720 , -4000 , 1000));
    public static final Box Midfield = new Box(new Vector3(-4080, -2080, 4060), new Vector3(4080, 2080, 0));
    public static final Box Upper_Right_Cornor = new Box(new Vector3(-4080, -5080, 4060), new Vector3(0, 0, 0));
    public static final Box Upper_Left_Cornor = new Box(new Vector3(-4080, 5080, 4060), new Vector3(0, 0, 0));
    public static final Box Lover_Right_Cornor = new Box(new Vector3(4080, 5080, 4060), new Vector3(0, 0, 0));
    public static final Box Lover_Left_Cornor = new Box(new Vector3(4080, -5080, 4060), new Vector3(0, 0, 0));
    public static final Box Orange_Goal_Box = new Box(new Vector3(-720, 5200, 0), new Vector3(720, 7000, 1000));
    public static final Box Blue_Goal_Box = new Box(new Vector3(-720, -5200, 0), new Vector3(720, -7000, 1000));
    public static final Vector2 BLUE_GOALPOST_LEFT = new Vector2(-720, -5200);
    public static final Vector2 BLUE_GOALPOST_RIGHT = new Vector2(720, -5200);
    public static final Vector2 RED_GOALPOST_LEFT = new Vector2(-720, 5200);
    public static final Vector2 RED_GOALPOST_RIGHT = new Vector2(720, 5200);

    private double WALL_X = ARENA_WIDTH/2, WALL_Y = ARENA_LENGTH/2;

    private GameData.GameTickPacket packet;
    private TimeTracker timeTracker;

    /* CARS */
    public final int myPlayerIndex;
    public final int enemyPlayerIndex;
    public final Car myCar;
    public final Car enemyCar;

    /* BALL */
    public final Rigidbody ball;
    public final double ballLandingTime;
    public final Vector3 ballLandingPosition;

    /* GAME */
    public final boolean gameIsKickOffPause;
    public final boolean gameIsMatchEnded;
    public final boolean gameIsOvertime;
    public final boolean gameIsRoundActive;
    public final int gamePlayerCount;
    public final Boostpad[] boostpads;

    /** The constructor.
     * @param packet the GameTickPacket.
     * @param timeTracker the class that tracks and handles time. */
    public Situation(GameData.GameTickPacket packet, TimeTracker timeTracker){
        this.packet = packet;
        this.timeTracker = timeTracker;
        this.boostpads = constructBoostpadArray(packet.getBoostPadsList());

        /* CARS */
        myPlayerIndex = packet.getPlayerIndex();
        myCar = new Car(myPlayerIndex, packet);
        enemyPlayerIndex = this.myPlayerIndex == 1 ? 0 : 1;
        enemyCar = new Car(enemyPlayerIndex, packet);

        /* BALL */
        // this.ballHasAcceleration = packet.getBall().hasAcceleration(); // What is this?
        this.ball = Ball.get(packet.getBall());
        double landingTime = SimplePhysics.predictArrivalAtHeight(ball, Ball.RADIUS, true);
        if (Double.isNaN(landingTime)) {
            this.ballLandingTime = 0;
            this.ballLandingPosition = ball.getPosition();
        } else {
            this.ballLandingTime = landingTime;
            this.ballLandingPosition = SimplePhysics.step(ball.clone(), ballLandingTime, true).getPosition();
        }

        /* GAME */
        this.gameIsKickOffPause = packet.getGameInfo().getIsKickoffPause();
        this.gameIsMatchEnded = packet.getGameInfo().getIsMatchEnded();
        this.gameIsOvertime = packet.getGameInfo().getIsOvertime();
        this.gameIsRoundActive = packet.getGameInfo().getIsRoundActive();
        this.gamePlayerCount = packet.getPlayersCount();
    }

    // Constructor used by simulation
    public Situation(Car car, Car enemyCar, Rigidbody ball, Boostpad[] boostpads) {
        this.myPlayerIndex = car.getPlayerIndex();
        this.enemyPlayerIndex = enemyCar.getPlayerIndex();
        this.myCar = car;
        this.enemyCar = enemyCar;
        this.ball = ball;
        this.boostpads = boostpads;

        // Ball landing
        double landingTime = SimplePhysics.predictArrivalAtHeight(ball, Ball.RADIUS, true);
        if (Double.isNaN(landingTime)) {
            this.ballLandingTime = 0;
            this.ballLandingPosition = ball.getPosition();
        } else {
            this.ballLandingTime = landingTime;
            this.ballLandingPosition = SimplePhysics.step(ball.clone(), ballLandingTime, true).getPosition();
        }

        // TODO Hardcode Specific situations in simulation
        this.gameIsKickOffPause = false;
        this.gameIsMatchEnded = false;
        this.gameIsOvertime = false;
        this.gameIsRoundActive = true;
        this.gamePlayerCount = 2;
    }

    /** Construct an array of boost pads from the packet's list of BoostpadInfo. */
    private Boostpad[] constructBoostpadArray(List<GameData.BoostInfo> boostInfoList) {
        Boostpad[] boostpads = new Boostpad[Boostpad.COUNT_TOTAL_PADS];

        int j = 0;
        for (int i = 0; i < boostInfoList.size(); i++) {
            if (i != 6) { // Boostpad 6 is not actually in the game.
                GameData.BoostInfo info = boostInfoList.get(i);
                boostpads[j] = new Boostpad(info.getLocation().getX(), info.getLocation().getY(), info.getTimer());
                j++;
            }
        }

        return boostpads;
    }

    /** Used to get the best boostpad based on utility.
     * @return the best boostpad for myCar. */
    public Boostpad getBestBoostPad() {
        double bestBoostUtility = 0;
        Boostpad bestBoostpad = null;

        for (int i = 0; i < Boostpad.COUNT_TOTAL_PADS; i++) {

            Boostpad pad = boostpads[i];
            Vector3 position = pad.getPosition();

            if (pad.isActive()){
                double angleToBoost = RLMath.carsAngleToPoint(new Vector2(myCar.getPosition()), myCar.getPosition().yaw, position.asVector2());
                double distance = myCar.getPosition().getDistanceTo(position);
                double distFunc = ((ARENA_LENGTH - distance) / ARENA_LENGTH); // Map width
                double newBoostUtility = (Math.cos(angleToBoost) * distFunc); // Utility formula

                if (newBoostUtility > bestBoostUtility){
                    bestBoostUtility = newBoostUtility;
                    bestBoostpad = pad;
                }
            }
        }

        return bestBoostpad; // Return the boostPad with highest utility
    }

    /** Method that returns true if myCar has ball possession by comparing using utility
    * The car with the highest utility is the car with possession. */
    public boolean whoHasPossession(){
        double myUtility = possessionUtility(myCar);
        double enemyUtility = possessionUtility(enemyCar);

        return (myUtility >= enemyUtility);
    }

    /** Help function to calculate and return the possession utility of a given car
    * Currently weighed equally and therefore can be considered inaccurate. Requires more testing. */
    private double possessionUtility (Car car){
        double distanceUtility = 1-car.getPosition().getDistanceTo(ball.getPosition())/ARENA_LENGTH;
        double angleUtility = Math.cos(car.getAngleToBall());
        double velocityUtility = car.getVelocity().getMagnitude()/MAX_VELOCITY_NO_BOOST;

        // Returns the total utility points.
        return distanceUtility + angleUtility + velocityUtility;
    }

    public boolean isPointBehindCar(int playerIndex, Vector3 pointVector){
        Car car = getCar(playerIndex);

        // Determine vector to the given point from car pos
        Vector3 vectorToPoint = pointVector.minus(car.getPosition());

        // Find angle to the given point
        double ang = car.getFrontVector().getAngleTo(vectorToPoint);

        return ang < (Math.PI/2);
    }

    public Box getEnemyBoxArea(int playerIndex) {
        return playerIndex == 0 ? ORANGE_GOAL_BOX_AREA : BLUE_GOAL_BOX_AREA;
    }

    /** Used to access GameTickPacket */
    public GameData.GameTickPacket getPacket() {
        return packet;
    }

    /** Used to access TimeTracker. */
    public TimeTracker getTimeTracker() {
        return timeTracker;
    }

    /** @return a players Car */
    public Car getCar(int index) {
        return index == myPlayerIndex ? myCar : enemyCar;
    }

    /** @return either +1 or -1, depending on which end of the y-axis this player's goal is. */
    public int getGoalDirection(int playerIndex) {
        return playerIndex == 0 ? -1 : 1;
    }

    public static Vector3 getGoalBox(int playerIndex) {
        return playerIndex == 0 ? BLUE_GOAL_BOX : ORANGE_GOAL_BOX;
    }

    /** @return the vector of the big boost in the quadrant requested */
    public Vector3 getCorner(int x, int y){
        return new Vector3(x * 3070, y * 4100);
        }

    /** @return a vector relative to the players team  */
    public Vector3 getMyCorner(int x){
        return new Vector3(x*3070, 4100*getGoalDirection(myPlayerIndex));
    }

        /** @return a double for the time to collision between ball and car  */
    public double getCollisionTime() {

        // TODO CLEAN UP THE CODE AND IMPROVE PREDICTION
        Vector3 expectedBall;
        double predictSeconds = 0;
        double predict = 0.02;
        double counter = 0.02;
        double velocity;
        boolean isBallStill = false;

        //If the ball is really slow or still, skip the loop and don't predict.
        if (10 > ball.getVelocity().getMagnitude()) {
            isBallStill = true;
        }

        //The loop will find a spot where the distance of expected ball to car minus the carvelocity multiplied by predict is between -25 and 25.
        //That way the agent should always be able to choose the right amount of prediction seconds, although this will probably change a little bit every tick as
        //the carvelocity changes.
        while (predictSeconds < 0.1 && counter <= 5 && !isBallStill) {
            expectedBall = ball.getVelocity().plus(ball.getVelocity().scale(predict));

            // If the car is not really driving, it should overextend its prediction to the future.
            if (myCar.getVelocity().getMagnitude() < 800) {
                velocity = 800;
            } else velocity = myCar.getVelocity().getMagnitude();

            if (-25 < expectedBall.minus(myCar.getPosition().plus(myCar.getFrontVector().scale(70))).getMagnitude() - velocity * predict && expectedBall.minus(myCar.getPosition().plus(myCar.getFrontVector().scale(70))).getMagnitude() - velocity * predict < 25) {
                predictSeconds = predict;
            }

            predict += 0.02;
            counter += 0.02;
        }
        // If it runs through loop without choosing one, then don't predict (Probably not needed)
        if (counter > 5) {
            predictSeconds = 0;
        }
        // if ball is still, don't predict
        if (isBallStill) {
            predictSeconds = 0;
        }
        return predictSeconds;
    }

    /**checks if the ball is within the field*/
    public boolean IsBallWithinField(Vector2 point) {
        return (point.x >= WALL_X-Ball.RADIUS*3 && point.x <= -WALL_X+Ball.RADIUS*3 && point.y >= WALL_Y-Ball.RADIUS*3 && point.y <= -WALL_Y+Ball.RADIUS*3);
    }

    /**Checks if the agent is on the wall*/
    public boolean IsAgentWithinField(Vector2 point) {
        return (point.x >= WALL_X-30&& point.x <= -WALL_X+30 && point.y >= WALL_Y-30 && point.y <= -WALL_Y+30);
    }
}
