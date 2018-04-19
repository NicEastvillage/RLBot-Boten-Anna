package botenanna.game;

import botenanna.Ball;
import botenanna.math.RLMath;
import botenanna.math.Vector2;
import botenanna.math.Vector3;
import botenanna.math.zone.Box;
import botenanna.physics.TimeTracker;
import rlbot.api.GameData;

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
    public static final Vector2 BLUE_GOALPOST_LEFT = new Vector2(-720, -5200);
    public static final Vector2 BLUE_GOALPOST_RIGHT = new Vector2(720, -5200);
    public static final Vector2 RED_GOALPOST_LEFT = new Vector2(-720, 5200);
    public static final Vector2 RED_GOALPOST_RIGHT = new Vector2(720, 5200);
    public static final Vector3[] BIG_BOOST_PADS = {new Vector3(-3070, 4100), new Vector3(3070,-4100), new Vector3(-3070,-4100),new Vector3(-3580,0), new Vector3(3580,0), new Vector3(3070, 4100)};

    private double WALL_X = ARENA_WIDTH/2, WALL_Y = ARENA_LENGTH/2;

    private GameData.GameTickPacket packet;
    private TimeTracker timeTracker;

    /* CARS */
    public final int myPlayerIndex;
    public final int enemyPlayerIndex;
    public final Car myCar;
    public final Car enemyCar;

    /* BALL */
    public final Ball ball;
    public final double ballLandingTime;
    public final Vector3 ballLandingPosition;

    /* GAME */
    public final boolean gameIsKickOffPause;
    public final boolean gameIsMatchEnded;
    public final boolean gameIsOvertime;
    public final boolean gameIsRoundActive;
    public final int gamePlayerCount;
    public Boostpads gameBoostPads;

    /** The constructor.
     * @param packet the GameTickPacket.
     * @param timeTracker the class that tracks and handles time. */
    public Situation(GameData.GameTickPacket packet, TimeTracker timeTracker){
        this.packet = packet;
        this.timeTracker = timeTracker;
        this.gameBoostPads = new Boostpads();
        this.gameBoostPads.updateBoostpadList(packet.getBoostPadsList());


        /* CARS */
        myPlayerIndex = packet.getPlayerIndex();
        myCar = new Car(myPlayerIndex, packet);
        enemyPlayerIndex = this.myPlayerIndex == 1 ? 0 : 1;
        enemyCar = new Car(enemyPlayerIndex, packet);

        /* BALL */
        // this.ballHasAcceleration = packet.getBall().hasAcceleration(); // What is this?
        this.ball = new Ball(packet.getBall());
        double landingTime = ball.predictArrivalAtHeight(Ball.RADIUS);
        if (Double.isNaN(landingTime)) {
            this.ballLandingTime = 0;
            this.ballLandingPosition = ball.getPosition();
        } else {
            this.ballLandingTime = landingTime;
            this.ballLandingPosition = ball.stepped(ballLandingTime).getPosition();
        }

        /* GAME */
        this.gameIsKickOffPause = packet.getGameInfo().getIsKickoffPause();
        this.gameIsMatchEnded = packet.getGameInfo().getIsMatchEnded();
        this.gameIsOvertime = packet.getGameInfo().getIsOvertime();
        this.gameIsRoundActive = packet.getGameInfo().getIsRoundActive();
        this.gamePlayerCount = packet.getPlayersCount();
    }
    // Constructor  for simulation
    public Situation(Car car, Car enemyCar, Ball ball, Boostpads pads) {
        this.myPlayerIndex = car.playerIndex;
        this.enemyPlayerIndex = enemyCar.playerIndex;
        this.myCar = car;
        this.enemyCar = enemyCar;
        this.ball = ball;
        this.gameBoostPads = pads;
        this.gameBoostPads.updateBoostpadList(packet.getBoostPadsList());


        // Udregn
        double landingTime = ball.predictArrivalAtHeight(Ball.RADIUS);
        if (Double.isNaN(landingTime)) {
            this.ballLandingTime = 0;
            this.ballLandingPosition = ball.getPosition();
        } else {
            this.ballLandingTime = landingTime;
            this.ballLandingPosition = ball.stepped(ballLandingTime).getPosition();
        }
        // TODO Hardcode Specific situations in simulation
        this.gameIsKickOffPause = false;
        this.gameIsMatchEnded = false;
        this.gameIsOvertime = false;
        this.gameIsRoundActive = true;
        this.gamePlayerCount = 2;
    }

    /** Used to get the best boostpad based on utility.
     * @return a vector3 for the best boostpad. */
    public Vector3 getBestBoostPad(){
        double bestBoostUtility = 0;
        Vector2 bestBoostPad = null;
        int totalBoostPads = Boostpads.COUNT_TOTAL_PADS;
        /*int[] bigBoostIndex = {7,8,9,10,11,12}; // Index of big boosts //TODO not true in new packet*/

        for (int i = 0; i < totalBoostPads; i++) {

            Boostpads.Boostpad boostpad = gameBoostPads.getBoostpad(i);
            Vector2 boostLocation = boostpad.getLocation();

            if (boostpad.isActive()){
                double angleToBoost = RLMath.carsAngleToPoint(new Vector2(myCar.getPosition()), myCar.getPosition().yaw, boostLocation);
                double distance = myCar.getPosition().getDistanceTo(boostLocation.asVector3());
                double distFunc = ((ARENA_LENGTH - distance) / ARENA_LENGTH); // Map width
                double newBoostUtility = (Math.cos(angleToBoost) * distFunc); // Utility formula

                if (newBoostUtility > bestBoostUtility){
                    bestBoostUtility = newBoostUtility;
                    bestBoostPad = boostLocation;
                }
            }
        }

        return bestBoostPad.asVector3(); // Return the boostPad with highest utility
    }

    /* Method that returns true if myCar has ball possession by comparing using utility
    * The car with the highest utility is the car with possession. */
    public boolean whoHasPossession(){
        double myUtility = possessionUtility(myCar);
        double enemyUtility = possessionUtility(enemyCar);

        return (myUtility >= enemyUtility);
    }

    /* Help function to calculate and return the possession utility of a given car
    * Currently weighed equally and therefore can be considered inaccurate. Requires more testing. */
    private double possessionUtility (Car car){
        double distanceUtility = 1-car.getPosition().getDistanceTo(ball.getPosition())/ARENA_LENGTH;
        double angleUtility = Math.cos(car.angleToBall);
        double velocityUtility = car.getVelocity().getMagnitude()/MAX_VELOCITY_NO_BOOST;

        // Returns the total utility points.
        return distanceUtility + angleUtility + velocityUtility;
    }

    public double whichSideOfPlane(Vector3 pointVector){
        // Determine vector to the given point from front vector
        Vector3 vectorToPoint = pointVector.minus(myCar.getPosition());

        // Find angle to the given point
        return myCar.frontVector.getAngleTo(vectorToPoint);
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

            if (-25 < expectedBall.minus(myCar.getPosition().plus(myCar.frontVector.scale(70))).getMagnitude() - velocity * predict && expectedBall.minus(myCar.getPosition().plus(myCar.frontVector.scale(70))).getMagnitude() - velocity * predict < 25) {
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
