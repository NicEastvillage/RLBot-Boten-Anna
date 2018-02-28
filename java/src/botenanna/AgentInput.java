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
    public final int myPlayerIndex;
    public final int myTeam;
    public final Vector3 myLocation;
    public final Vector3 myVelocity;
    public final Vector3 myRotation;
    //public final Vector3 myUpVector;
    public final int myBoost;
    public final boolean myHasJumped;
    public final boolean myHasDoubleJumped;
    public final boolean myIsDemolished;
    public final boolean myIsSupersonic;
    public final boolean myIsCarOnGround;

    /* ENEMY */
    public final int enemyPlayerIndex;
    public final int enemyTeam;
    public final Vector3 enemyLocation;
    public final Vector3 enemyVelocity;
    public final Vector3 enemyRotation;
    //public final Vector3 enemyUpVector;
    public final int enemyBoost;
    public final boolean enemyHasJumped;
    public final boolean enemyHasDoubleJumped;
    public final boolean enemyIsDemolished;
    public final boolean enemyIsSupersonic;
    public final boolean enemyIsCarOnGround;

    /* BALL */
    public final Vector3 ballLocation;
    public final Vector3 ballVelocity;
    public final Vector3 ballAcceleration;
    public final boolean ballHasAcceleration;

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

        /* CAR */
        this.myPlayerIndex = packet.getPlayerIndex();
        this.myTeam = packet.getPlayers(myPlayerIndex).getTeam();
        this.myLocation = Vector3.convert(packet.getPlayers(myPlayerIndex).getLocation());
        this.myVelocity = Vector3.convert(packet.getPlayers(myPlayerIndex).getVelocity());
        this.myRotation = Vector3.convert(packet.getPlayers(myPlayerIndex).getRotation());
        //this.myUpVector = RLMath.carUpVector(Vector3.convert(packet.getPlayers(myPlayerIndex).getRotation()));
        this.myBoost = packet.getPlayers(myPlayerIndex).getBoost(); //TODO: Value?
        this.myHasJumped = packet.getPlayers(myPlayerIndex).getJumped();
        this.myHasDoubleJumped = packet.getPlayers(myPlayerIndex).getDoubleJumped();
        this.myIsDemolished = packet.getPlayers(myPlayerIndex).getIsDemolished();
        this.myIsSupersonic = packet.getPlayers(myPlayerIndex).getIsSupersonic();
        this.myIsCarOnGround = packet.getPlayers(myPlayerIndex).getLocation().getZ() < 20;

        /* ENEMY */
        this.enemyPlayerIndex = this.myPlayerIndex == 1 ? 0 :  1;
        this.enemyTeam = packet.getPlayers(this.enemyPlayerIndex).getTeam();
        this.enemyLocation = Vector3.convert(packet.getPlayers(enemyPlayerIndex).getLocation());
        this.enemyVelocity = Vector3.convert(packet.getPlayers(enemyPlayerIndex).getVelocity());
        this.enemyRotation = Vector3.convert(packet.getPlayers(enemyPlayerIndex).getRotation());
        //this.enemyUpVector = RLMath.carUpVector(Vector3.convert(packet.getPlayers(enemyPlayerIndex).getRotation()));
        this.enemyBoost = packet.getPlayers(enemyPlayerIndex).getBoost(); //TODO: Value?
        this.enemyHasJumped = packet.getPlayers(enemyPlayerIndex).getJumped();
        this.enemyHasDoubleJumped = packet.getPlayers(enemyPlayerIndex).getDoubleJumped();
        this.enemyIsDemolished = packet.getPlayers(enemyPlayerIndex).getIsDemolished();
        this.enemyIsSupersonic = packet.getPlayers(enemyPlayerIndex).getIsSupersonic();
        this.enemyIsCarOnGround = packet.getPlayers(enemyPlayerIndex).getLocation().getZ() < 20;

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

}
