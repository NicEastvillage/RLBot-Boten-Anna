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

    private GameData.GameTickPacket packet;
    private TimeTracker timeTracker;

    /* CAR */
    private int myPlayerIndex;
    private int myTeam;
    private Vector3 myLocation;
    private Vector3 myVelocity;
    private Vector3 myRotation;
    private int myBoost;
    private boolean myJumped;
    private boolean myDoubleJumped;
    private boolean myIsDemolished;
    private boolean myIsSupersonic;
    private boolean myIsCarOnGround;

    /* ENEMY */
    private int enemyPlayerIndex;
    private int enemyTeam;
    private Vector3 enemyLocation;
    private Vector3 enemyVelocity;
    private Vector3 enemyRotation;
    private int enemyBoost;
    private boolean enemyJumped;
    private boolean enemyDoubleJumped;
    private boolean enemyIsDemolished;
    private boolean enemyIsSupersonic;
    private boolean enemyIsCarOnGround;

    /* BALL */
    private Vector3 ballLocation;
    private Vector3 ballVelocity;
    private Vector3 ballAcceleration;
    private boolean ballHasAcceleration;

    /* GAME */
    private boolean gameIsKickOffPause;
    private boolean gameIsMatchEnded;
    private boolean gameIsOvertime;
    private boolean gameIsRoundActive;
    private int gamePlayerCount;

    /* UTILS */
    private double angleToBall;


    /** The constructor.
     * @param packet the GameTickPacket.
     * @param timeTracker the class that tracks and handles time. */
    public AgentInput(GameData.GameTickPacket packet, TimeTracker timeTracker){
        this.packet = packet;
        this.timeTracker = timeTracker;

        /* CAR */
        this.myPlayerIndex = packet.getPlayerIndex();
        this.myTeam = packet.getPlayers(myPlayerIndex).getTeam();
        this.myLocation = Vector3.convert(packet.getPlayers(myPlayerIndex).getLocation());
        this.myVelocity = Vector3.convert(packet.getPlayers(myPlayerIndex).getVelocity());
        this.myRotation = Vector3.convert(packet.getPlayers(myPlayerIndex).getRotation());
        this.myBoost = packet.getPlayers(myPlayerIndex).getBoost(); //TODO: Value?
        this.myJumped = packet.getPlayers(myPlayerIndex).getJumped();
        this.myDoubleJumped = packet.getPlayers(myPlayerIndex).getDoubleJumped();
        this.myIsDemolished = packet.getPlayers(myPlayerIndex).getIsDemolished();
        this.myIsSupersonic = packet.getPlayers(myPlayerIndex).getIsSupersonic();
        this.myIsCarOnGround = isCarOnGround(myPlayerIndex);

        /* ENEMY */
        this.enemyPlayerIndex = this.myPlayerIndex == 1 ? 0 :  1;
        this.enemyTeam = packet.getPlayers(this.enemyPlayerIndex).getTeam();
        this.enemyLocation = Vector3.convert(packet.getPlayers(enemyPlayerIndex).getLocation());
        this.enemyVelocity = Vector3.convert(packet.getPlayers(enemyPlayerIndex).getVelocity());
        this.enemyRotation = Vector3.convert(packet.getPlayers(enemyPlayerIndex).getRotation());
        this.enemyBoost = packet.getPlayers(enemyPlayerIndex).getBoost(); //TODO: Value?
        this.enemyJumped = packet.getPlayers(enemyPlayerIndex).getJumped();
        this.enemyDoubleJumped = packet.getPlayers(enemyPlayerIndex).getDoubleJumped();
        this.enemyIsDemolished = packet.getPlayers(enemyPlayerIndex).getIsDemolished();
        this.enemyIsSupersonic = packet.getPlayers(enemyPlayerIndex).getIsSupersonic();
        this.enemyIsCarOnGround = isCarOnGround(enemyPlayerIndex);

        /* BALL */
        this.ballLocation = Vector3.convert(packet.getBall().getLocation());
        this.ballVelocity = Vector3.convert(packet.getBall().getVelocity());
        this.ballAcceleration = Vector3.convert(packet.getBall().getAcceleration());
        this.ballHasAcceleration = packet.getBall().hasAcceleration();

        /* GAME */
        this.gameIsKickOffPause = packet.getGameInfo().getIsKickoffPause();
        this.gameIsMatchEnded = packet.getGameInfo().getIsMatchEnded();
        this.gameIsOvertime = packet.getGameInfo().getIsOvertime();
        this.gameIsRoundActive = packet.getGameInfo().getIsRoundActive();
        this.gamePlayerCount = packet.getPlayersCount();

        /* UTILS*/
        this.angleToBall = RLMath.carsAngleToPoint(new Vector2(this.ballLocation), this.myRotation.yaw, new Vector2(this.ballLocation));
    }

    /** Used to access GameTickPacket */
    public GameData.GameTickPacket getPacket() {
        return packet;
    }

    /** Used to access TimeTracker. */
    public TimeTracker getTimeTracker() {
        return timeTracker;
    }

    /** Returns true if the car is on the ground.
     *  This does not work if the car is upside down.
     * @param playerIndex the index of the player.
     * @return true if the car is on the ground. */
    private boolean isCarOnGround(int playerIndex){
        float carZ = this.packet.getPlayers(playerIndex).getLocation().getZ();

        if(carZ < 20)
            return true;
        else
            return false;
    }


    /* GETTERS: GENERATED */
    public int getMyPlayerIndex() {
        return myPlayerIndex;
    }

    public int getMyTeam() {
        return myTeam;
    }

    public Vector3 getMyLocation() {
        return myLocation;
    }

    public Vector3 getMyVelocity() {
        return myVelocity;
    }

    public Vector3 getMyRotation() {
        return myRotation;
    }

    public int getMyBoost() {
        return myBoost;
    }

    public boolean isMyJumped() {
        return myJumped;
    }

    public boolean isMyDoubleJumped() {
        return myDoubleJumped;
    }

    public boolean isMyIsDemolished() {
        return myIsDemolished;
    }

    public boolean isMyIsSupersonic() {
        return myIsSupersonic;
    }

    public boolean isMyIsCarOnGround() {
        return myIsCarOnGround;
    }

    public int getEnemyPlayerIndex() {
        return enemyPlayerIndex;
    }

    public int getEnemyTeam() {
        return enemyTeam;
    }

    public Vector3 getEnemyLocation() {
        return enemyLocation;
    }

    public Vector3 getEnemyVelocity() {
        return enemyVelocity;
    }

    public Vector3 getEnemyRotation() {
        return enemyRotation;
    }

    public int getEnemyBoost() {
        return enemyBoost;
    }

    public boolean isEnemyJumped() {
        return enemyJumped;
    }

    public boolean isEnemyDoubleJumped() {
        return enemyDoubleJumped;
    }

    public boolean isEnemyIsDemolished() {
        return enemyIsDemolished;
    }

    public boolean isEnemyIsSupersonic() {
        return enemyIsSupersonic;
    }

    public boolean isEnemyIsCarOnGround() {
        return enemyIsCarOnGround;
    }

    public Vector3 getBallLocation() {
        return ballLocation;
    }

    public Vector3 getBallVelocity() {
        return ballVelocity;
    }

    public Vector3 getBallAcceleration() {
        return ballAcceleration;
    }

    public boolean isBallHasAcceleration() {
        return ballHasAcceleration;
    }

    public boolean isGameIsKickOffPause() {
        return gameIsKickOffPause;
    }

    public boolean isGameIsMatchEnded() {
        return gameIsMatchEnded;
    }

    public boolean isGameIsOvertime() {
        return gameIsOvertime;
    }

    public boolean isGameIsRoundActive() {
        return gameIsRoundActive;
    }

    public int getGamePlayerCount() {
        return gamePlayerCount;
    }

    public double getAngleToBall() {
        return angleToBall;
    }
}
