package botenanna;

import botenanna.math.RLMath;
import botenanna.math.Vector2;
import botenanna.math.Vector3;
import botenanna.physics.TimeTracker;
import rlbot.api.GameData;

//TODO: add isUpsideDown, myGoal (2dVEC), myGoalLine (2dVEC)

/** This manages the input (packet).
 *  It handles calculations and other measures. */
public class AgentInput {

    public static final double ARENA_LENGTH = 10280;
    public static final double MAX_VELOCITY_NO_BOOST = 1410.1;
    public static final double ARENA_WIDTH = 8240;
    public static final Vector3 BLUE_GOAL_BOX = Vector3.BACKWARDS.scale(5000);
    public static final Vector3 ORANGE_GOAL_BOX = Vector3.FORWARD.scale(5000);
    public static final Vector2 BLUE_GOALPOST_LEFT = new Vector2(-720, -5200);
    public static final Vector2 BLUE_GOALPOST_RIGHT = new Vector2(720, -5200);
    public static final Vector2 RED_GOALPOST_LEFT = new Vector2(-720, 5200);
    public static final Vector2 RED_GOALPOST_RIGHT = new Vector2(720, 5200);
    public static final Vector3[] BIG_BOOST_PADS = {new Vector3(-3070, 4100), new Vector3(3070,-4100), new Vector3(-3070,-4100),new Vector3(-3580,0), new Vector3(3580,0), new Vector3(3070, 4100)};
    private double UPPERLEFT_CORNER_X1 = 10280/2-Ball.RADIUS*3, UPPERRIGHT_CORNER_y1=8240/2-Ball.RADIUS*3;   // lower left
    private double LOWERLEFT_CORNER_X2 = -10280/2+Ball.RADIUS*3, LOWERRIGHT_CORNER_y2=-8240/2+Ball.RADIUS*3;   // upper right
    private double q1 = 10280/2-30, w1=8240/2-30;   // lower left
    private double q2 = -10280/2+30, w2=-8240/2+30;   // upper right





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

    /** The constructor.
     * @param packet the GameTickPacket.
     * @param timeTracker the class that tracks and handles time. */
    public AgentInput(GameData.GameTickPacket packet, TimeTracker timeTracker){
        this.packet = packet;
        this.timeTracker = timeTracker;


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

    public Vector3 getBestBoostPad(){
        double bestBoostUtility = 0;
        Vector3 bestBoostPad = null;
        int allBoostPads = packet.getBoostPadsCount();
        /*int[] bigBoostIndex = {7,8,9,10,11,12}; // Index of big boosts*/

        for (int i = 0; i < allBoostPads; i++) {
            GameData.BoostInfo boost = packet.getBoostPads(i);
            Vector3 boostLocation = Vector3.convert(boost.getLocation());

            if (boost.getIsActive()){
                double angleToBoost = RLMath.carsAngleToPoint(new Vector2(myCar.position), myCar.position.yaw, boostLocation.asVector2());
                double distance = myCar.position.getDistanceTo(boostLocation);
                double distFunc = ((ARENA_LENGTH - distance) / ARENA_LENGTH); // Map width
                double newBoostUtility = (Math.cos(angleToBoost) * distFunc); // Utility formula

                if (newBoostUtility > bestBoostUtility){
                    bestBoostUtility = newBoostUtility;
                    bestBoostPad = boostLocation;
                }
            }
        }

        return bestBoostPad; // Return the boostPad with highest utility
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
        double distanceUtility = 1-car.position.getDistanceTo(ball.getPosition())/ARENA_LENGTH;
        double angleUtility = Math.cos(car.angleToBall);
        double velocityUtility = car.velocity.getMagnitude()/MAX_VELOCITY_NO_BOOST;

        // Returns the total utility points.
        return distanceUtility + angleUtility + velocityUtility;
    }

    public double whichSideOfPlane(Vector3 pointVector){
        // Determine vector to the given point from front vector
        Vector3 vectorToPoint = pointVector.minus(myCar.position);

        // Find angle to the given point
        return myCar.frontVector.getAngleTo(vectorToPoint);
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

    public Vector3 getGoalBox(int playerIndex) {
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
            if (myCar.velocity.getMagnitude() < 800) {
                velocity = 800;
            } else velocity = myCar.velocity.getMagnitude();

            if (-25 < expectedBall.minus(myCar.position.plus(myCar.frontVector.scale(70))).getMagnitude() - velocity * predict && expectedBall.minus(myCar.position.plus(myCar.frontVector.scale(70))).getMagnitude() - velocity * predict < 25) {
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

    //checks if the ball is within the field

    public boolean BallIsWithinField(Vector2 point) {

        return (point.x >= UPPERLEFT_CORNER_X1 && point.x <= LOWERLEFT_CORNER_X2 && point.y >= UPPERRIGHT_CORNER_y1 && point.y <= LOWERRIGHT_CORNER_y2);

    }

    //

    public boolean AgentIsWithinField(Vector2 point) {
        return (point.x >= q1 && point.x <= q2 && point.y >= w1 && point.y <= w2);
    }




    public class Car {
        public final int playerIndex;
        public final int team;
        public final Vector3 position;
        public final Vector3 velocity;
        public final Vector3 rotation;
        public final Vector3 angularVelocity;
        public final Vector3 upVector;
        public final Vector3 frontVector;
        public final Vector3 sideVector;
        public final int boost;
        public final boolean hasJumped;
        public final boolean hasDoubleJumped;
        public final boolean isDemolished;
        public final boolean isSupersonic;
        public final boolean isCarOnGround;
        public final boolean isMidAir;
        public final boolean isCarUpsideDown;
        public final double distanceToBall;
        public final double angleToBall;

        public Car(int index, GameData.GameTickPacket packet) {
            playerIndex = index;
            GameData.PlayerInfo info = packet.getPlayers(index);
            team = info.getTeam();
            position = Vector3.convert(info.getLocation());
            velocity = Vector3.convert(info.getVelocity());
            rotation = Vector3.convert(info.getRotation());
            angularVelocity = Vector3.convert(info.getAngularVelocity());
            upVector = RLMath.carUpVector(Vector3.convert(info.getRotation()));
            frontVector = RLMath.carFrontVector(Vector3.convert(info.getRotation()));
            sideVector = RLMath.carSideVector(Vector3.convert(info.getRotation()));
            boost = info.getBoost();
            hasJumped = info.getJumped();
            hasDoubleJumped = info.getDoubleJumped();
            isDemolished = info.getIsDemolished();
            isSupersonic = info.getIsSupersonic();
            isCarOnGround = info.getLocation().getZ() < 20;
            isMidAir = info.getIsMidair();
            isCarUpsideDown = RLMath.carUpVector(Vector3.convert(info.getRotation())).z < 0;
            distanceToBall = Vector3.convert(info.getLocation()).getDistanceTo(Vector3.convert(packet.getBall().getLocation()));
            angleToBall = RLMath.carsAngleToPoint(position.asVector2(), rotation.yaw, Vector3.convert(packet.getBall().getLocation()).asVector2());
        }
    }
}
