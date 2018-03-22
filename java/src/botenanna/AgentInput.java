package botenanna;

import botenanna.math.RLMath;
import botenanna.math.Vector2;
import botenanna.math.Vector3;
import botenanna.physics.Rigidbody;
import botenanna.physics.TimeTracker;
import rlbot.api.GameData;

//TODO: add isUpsideDown, myGoal (2dVEC), myGoalLine (2dVEC)

/** This manages the input (packet).
 *  It handles calculations and other measures. */
public class AgentInput {

    public static final double ARENA_LENGTH = 10280;
    public static final double ARENA_WIDTH = 8240;
    public static final Vector3 BLUE_GOAL_BOX = Vector3.BACKWARDS.scale(5000);
    public static final Vector3 ORANGE_GOAL_BOX = Vector3.FORWARD.scale(5000);
    public static final Vector2 BLUE_GOALPOST_LEFT = new Vector2(-720, -5200);
    public static final Vector2 BLUE_GOALPOST_RIGHT = new Vector2(720, -5200);
    public static final Vector2 RED_GOALPOST_LEFT = new Vector2(-720, 5200);
    public static final Vector2 RED_GOALPOST_RIGHT = new Vector2(720, 5200);



    public static final Vector3[] BIG_BOOST_PADS = {new Vector3(-3070, 4100), new Vector3(3070,-4100), new Vector3(-3070,-4100),new Vector3(-3580,0), new Vector3(3580,0), new Vector3(3070, 4100)};


    private GameData.GameTickPacket packet;
    private TimeTracker timeTracker;

    /* ME */
    public final int myPlayerIndex;
    public final int myTeam;
    public final Vector3 myLocation;
    public final Vector3 myVelocity;
    public final Vector3 myRotation;
    public final Vector3 myAngularVelocity;
    public final Vector3 myUpVector;
    public final Vector3 myFrontVector;
    public final Vector3 mySideVector;
    public final int myBoost;
    public final boolean myHasJumped;
    public final boolean myHasDoubleJumped;
    public final boolean myIsDemolished;
    public final boolean myIsSupersonic;
    public final boolean myIsCarOnGround;
    public final boolean myIsMidAir;
    public final boolean myIsCarUpsideDown;
    public final double myDistanceToBall;

    /* ENEMY */
    public final int enemyPlayerIndex;
    public final int enemyTeam;
    public final Vector3 enemyLocation;
    public final Vector3 enemyVelocity;
    public final Vector3 enemyRotation;
    public final Vector3 enemyAngularVelocity;
    public final Vector3 enemyUpVector;
    public final Vector3 enemyFrontVector;
    public final Vector3 enemySideVector;
    public final int enemyBoost;
    public final boolean enemyHasJumped;
    public final boolean enemyHasDoubleJumped;
    public final boolean enemyIsDemolished;
    public final boolean enemyIsSupersonic;
    public final boolean enemyIsCarOnGround;
    public final boolean enemyIsMidAir;
    public final boolean enemyIsCarUpsideDown;
    public final double enemyDistanceToBall;

    /* BALL */
    public final Vector3 ballLocation;
    public final Vector3 ballVelocity;
    public final Vector3 ballAcceleration;
    public final boolean ballHasAcceleration;
    public final Ball ball;
    public final double ballLandingTime;
    public final Vector3 ballLandingPosition;

    /* GAME */
    public final boolean gameIsKickOffPause;
    public final boolean gameIsMatchEnded;
    public final boolean gameIsOvertime;
    public final boolean gameIsRoundActive;
    public final int gamePlayerCount;

    /* UTILS */
    public final double angleToBall;

    /** The constructor.
     * @param packet the GameTickPacket.
     * @param timeTracker the class that tracks and handles time. */
    public AgentInput(GameData.GameTickPacket packet, TimeTracker timeTracker){
        this.packet = packet;
        this.timeTracker = timeTracker;

        /* ME */
        this.myPlayerIndex = packet.getPlayerIndex();
        this.myTeam = packet.getPlayers(myPlayerIndex).getTeam();
        this.myLocation = Vector3.convert(packet.getPlayers(myPlayerIndex).getLocation());
        this.myVelocity = Vector3.convert(packet.getPlayers(myPlayerIndex).getVelocity());
        this.myRotation = Vector3.convert(packet.getPlayers(myPlayerIndex).getRotation());
        this.myAngularVelocity = Vector3.convert(packet.getPlayers(myPlayerIndex).getAngularVelocity());
        this.myUpVector = RLMath.carUpVector(Vector3.convert(packet.getPlayers(myPlayerIndex).getRotation()));
        this.myFrontVector = RLMath.carFrontVector(Vector3.convert(packet.getPlayers(myPlayerIndex).getRotation()));
        this.mySideVector = RLMath.carSideVector(Vector3.convert(packet.getPlayers(myPlayerIndex).getRotation()));
        this.myBoost = packet.getPlayers(myPlayerIndex).getBoost(); //TODO: Value?
        this.myHasJumped = packet.getPlayers(myPlayerIndex).getJumped();
        this.myHasDoubleJumped = packet.getPlayers(myPlayerIndex).getDoubleJumped();
        this.myIsDemolished = packet.getPlayers(myPlayerIndex).getIsDemolished();
        this.myIsSupersonic = packet.getPlayers(myPlayerIndex).getIsSupersonic();
        this.myIsCarOnGround = packet.getPlayers(myPlayerIndex).getLocation().getZ() < 20;
        this.myIsMidAir = packet.getPlayers(myPlayerIndex).getIsMidair();
        this.myIsCarUpsideDown = RLMath.carUpVector(Vector3.convert(packet.getPlayers(myPlayerIndex).getRotation())).z < 0;
        this.myDistanceToBall = Vector3.convert(packet.getPlayers(myPlayerIndex).getLocation()).getDistanceTo(Vector3.convert(packet.getBall().getLocation()));

        /* ENEMY */
        this.enemyPlayerIndex = this.myPlayerIndex == 1 ? 0 :  1;
        this.enemyTeam = packet.getPlayers(this.enemyPlayerIndex).getTeam();
        this.enemyLocation = Vector3.convert(packet.getPlayers(enemyPlayerIndex).getLocation());
        this.enemyVelocity = Vector3.convert(packet.getPlayers(enemyPlayerIndex).getVelocity());
        this.enemyRotation = Vector3.convert(packet.getPlayers(enemyPlayerIndex).getRotation());
        this.enemyAngularVelocity = Vector3.convert(packet.getPlayers(enemyPlayerIndex).getAngularVelocity());
        this.enemyUpVector = RLMath.carUpVector(Vector3.convert(packet.getPlayers(enemyPlayerIndex).getRotation()));
        this.enemyFrontVector = RLMath.carFrontVector(Vector3.convert(packet.getPlayers(enemyPlayerIndex).getRotation()));
        this.enemySideVector = RLMath.carSideVector(Vector3.convert(packet.getPlayers(enemyPlayerIndex).getRotation()));
        this.enemyBoost = packet.getPlayers(enemyPlayerIndex).getBoost(); //TODO: Value?
        this.enemyHasJumped = packet.getPlayers(enemyPlayerIndex).getJumped();
        this.enemyHasDoubleJumped = packet.getPlayers(enemyPlayerIndex).getDoubleJumped();
        this.enemyIsDemolished = packet.getPlayers(enemyPlayerIndex).getIsDemolished();
        this.enemyIsSupersonic = packet.getPlayers(enemyPlayerIndex).getIsSupersonic();
        this.enemyIsCarOnGround = packet.getPlayers(enemyPlayerIndex).getLocation().getZ() < 20;
        this.enemyIsMidAir = packet.getPlayers(enemyPlayerIndex).getIsMidair();
        this.enemyIsCarUpsideDown = RLMath.carUpVector(Vector3.convert(packet.getPlayers(enemyPlayerIndex).getRotation())).z < 0;;
        this.enemyDistanceToBall = Vector3.convert(packet.getPlayers(enemyPlayerIndex).getLocation()).getDistanceTo(Vector3.convert(packet.getBall().getLocation()));


        /* BALL */
        this.ballLocation = Vector3.convert(packet.getBall().getLocation());
        this.ballVelocity = Vector3.convert(packet.getBall().getVelocity());
        this.ballAcceleration = Vector3.convert(packet.getBall().getAcceleration());
        this.ballHasAcceleration = packet.getBall().hasAcceleration();
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

        /* UTILS*/
        this.angleToBall = RLMath.carsAngleToPoint(new Vector2(this.myLocation), this.myRotation.yaw, new Vector2(this.ballLocation));
    }

    public Vector3 getBestBoostPad(){
        double bestBoostUtility = 0;
        Vector3 bestBoostPad = null;
        int allBoostPads = packet.getBoostPadsCount();
/*        int[] bigBoostIndex = {7,8,9,10,11,12}; // Index of big boosts*/

        for (int i = 0; i < allBoostPads; i++) {
            GameData.BoostInfo boost = packet.getBoostPads(i);
            Vector3 boostLocation = Vector3.convert(boost.getLocation());

            if (boost.getIsActive()){
                double angleToBoost = RLMath.carsAngleToPoint(new Vector2(this.myLocation), this.myRotation.yaw, boostLocation.asVector2());
                double distance = this.myLocation.getDistanceTo(boostLocation);
                double distFunc = ((10280 - distance) / 10280); // Map width
                double newBoostUtility = (Math.cos(angleToBoost) * distFunc); // Utility formula

                if (newBoostUtility > bestBoostUtility){
                    bestBoostUtility = newBoostUtility;
                    bestBoostPad = boostLocation;
                }
            }
        }

        return bestBoostPad; // Return the boostPad with highest utility
    }

    /** Used to access GameTickPacket */
    public GameData.GameTickPacket getPacket() {
        return packet;
    }

    /** Used to access TimeTracker. */
    public TimeTracker getTimeTracker() {
        return timeTracker;
    }

    /** @return whether a player is super sonic. */
    public boolean isSuperSonic(int playerIndex) {
        return playerIndex == myPlayerIndex ? myIsSupersonic : enemyIsSupersonic;
    }

    /** @return whether a player is demolished. */
    public boolean isDemolished(int playerIndex) {
        return playerIndex == myPlayerIndex ? myIsDemolished : enemyIsDemolished;
    }

    /** @return whether a player has jumped. */
    public boolean hasJumped(int playerIndex) {
        return playerIndex == myPlayerIndex ? myHasJumped : enemyHasJumped;
    }

    /** @return whether a player has double jumped. */
    public boolean hasDoubleJumped(int playerIndex) {
        return playerIndex == myPlayerIndex ? myHasDoubleJumped : enemyHasDoubleJumped;
    }

    /** @return whether a player is on the ground. */
    public boolean isCarOnGround(int playerIndex) {
        return playerIndex == myPlayerIndex ? myIsCarOnGround : enemyIsCarOnGround;
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
        if (10 > ballVelocity.getMagnitude()) {
            isBallStill = true;
        }

        //The loop will find a spot where the distance of expected ball to car minus the carvelocity multiplied by predict is between -25 and 25.
        //That way the agent should always be able to choose the right amount of prediction seconds, although this will probably change a little bit every tick as
        //the carvelocity changes.
        while (predictSeconds < 0.1 && counter <= 5 && !isBallStill) {
            expectedBall = ballLocation.plus(ballVelocity.scale(predict));

            // If the car is not really driving, it should overextend its prediction to the future.
            if (myVelocity.getMagnitude() < 800) {
                velocity = 800;
            } else velocity = myVelocity.getMagnitude();

            if (-25 < expectedBall.minus(myLocation.plus(myFrontVector.scale(70))).getMagnitude() - velocity * predict && expectedBall.minus(myLocation.plus(myFrontVector.scale(70))).getMagnitude() - velocity * predict < 25) {
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
}
