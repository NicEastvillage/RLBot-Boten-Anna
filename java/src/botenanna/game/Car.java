package botenanna.game;

import botenanna.Ball;
import botenanna.math.RLMath;
import botenanna.math.Vector3;
import botenanna.physics.Rigidbody;
import rlbot.api.GameData;

public class Car extends Rigidbody {

    // Global Variables
    public final static double ACCELERATION_BOOST = 650;
    public final static double ACCELERATION = 400;
    public final static double TURN_RATE = Math.toRadians(5); // TODO Turn rate is a function of current speed
    public final static double MAX_VELOCITY = 1410;
    public final static double MAX_VELOCITY_BOOST = 2300;
    public final static double SUPERSONIC_SPEED_REQUIRED = MAX_VELOCITY_BOOST * 0.95;
    public final static double DECELERATION = 18;
    public final static double GROUND_OFFSET = 17.03;

    private final int team;
    private final int playerIndex;

    // Modifiable
    private int boost;
    private boolean hasJumped;
    private boolean hasDoubleJumped;
    private boolean isDemolished;

    // Dependent
    private Vector3 upVector;
    private Vector3 frontVector;
    private Vector3 sideVector;
    private boolean isSupersonic;
    private boolean isCarOnGround;
    private boolean isMidAir; // TODO Undefined when creating custom car
    private boolean isCarUpsideDown;
    private boolean isOnWall; // TODO Use upcoming zones to determine this
    private double distanceToBall;
    private double angleToBall;

    public Car(int index, GameData.GameTickPacket packet) {

        playerIndex = index;
        GameData.PlayerInfo info = packet.getPlayers(index);
        team = info.getTeam();

        setPosition(Vector3.convert(info.getLocation()));
        setVelocity(Vector3.convert(info.getVelocity()));
        setRotation(Vector3.convert(info.getRotation()));
        setAngularVelocity(Vector3.convert(info.getAngularVelocity()));

        boost = packet.getPlayers(index).getBoost();
        hasJumped = packet.getPlayers(index).getJumped();
        hasDoubleJumped = packet.getPlayers(index).getDoubleJumped();
        isDemolished = packet.getPlayers(index).getIsDemolished();
        isSupersonic = packet.getPlayers(index).getIsSupersonic();
        isMidAir = packet.getPlayers(index).getIsMidair();
        setBallDependentVariables(Vector3.convert(packet.getBall().getLocation()));

        isOnWall = getPosition().y==Situation.ARENA_LENGTH || getPosition().x == Situation.ARENA_WIDTH || getPosition().x == -Situation.ARENA_WIDTH || getPosition().y == -Situation.ARENA_LENGTH;
    }

    /** Constructor for new car based on an old instance of car */
    public Car(Car oldCar) {

        team = oldCar.team;
        playerIndex = oldCar.playerIndex;

        setPosition(oldCar.getPosition());
        setVelocity(oldCar.getVelocity());
        setRotation(oldCar.getRotation());
        setAngularVelocity(oldCar.getAngularVelocity());

        boost = oldCar.boost;
        hasJumped = oldCar.hasJumped;
        hasDoubleJumped = oldCar.hasDoubleJumped;
        isDemolished = oldCar.isDemolished;
        isSupersonic = oldCar.isSupersonic;
        isMidAir = oldCar.isMidAir;
        distanceToBall = oldCar.distanceToBall;
        angleToBall = oldCar.angleToBall;

        isOnWall = getPosition().y==Situation.ARENA_LENGTH || getPosition().x == Situation.ARENA_WIDTH || getPosition().x == -Situation.ARENA_WIDTH || getPosition().y == -Situation.ARENA_LENGTH;
    }

    @Override
    public Car clone() {
        return new Car(this);
    }

    public int getTeam() {
        return team;
    }

    public int getPlayerIndex() {
        return playerIndex;
    }

    public int getBoost() {
        return boost;
    }

    @Override
    public void setPosition(Vector3 position) {
        super.setPosition(position);
        isCarOnGround = position.z < 20;
    }

    @Override
    public void setVelocity(Vector3 velocity) {
        super.setVelocity(velocity);
        isSupersonic = velocity.getMagnitude() >= SUPERSONIC_SPEED_REQUIRED;
    }

    @Override
    public void setAcceleration(Vector3 acceleration) {
        super.setAcceleration(acceleration);
    }

    @Override
    public void setRotation(Vector3 rotation) {
        super.setRotation(rotation);
        upVector = RLMath.carUpVector(rotation);
        frontVector = RLMath.carFrontVector(rotation);
        sideVector = RLMath.carSideVector(rotation);
        isCarUpsideDown = upVector.z < 0;
    }

    @Override
    public void setAngularVelocity(Vector3 angularVelocity) {
        super.setAngularVelocity(angularVelocity);
    }

    public void setBallDependentVariables(Vector3 ballPosition) {
        angleToBall = RLMath.carsAngleToPoint(getPosition().asVector2(),  getRotation().yaw, ballPosition.asVector2());
        distanceToBall = getPosition().getDistanceTo(ballPosition);
    }

    public void setBoost(int amount) {
        this.boost = Math.min(Math.max(0, amount), 100);
    }

    public void addBoost(int amount) {
        this.boost = Math.min(Math.max(0, this.boost + amount), 100);
    }

    public boolean isHasJumped() {
        return hasJumped;
    }

    public void setHasJumped(boolean hasJumped) {
        this.hasJumped = hasJumped;
    }

    public boolean isHasDoubleJumped() {
        return hasDoubleJumped;
    }

    public void setHasDoubleJumped(boolean hasDoubleJumped) {
        if (hasDoubleJumped) hasJumped = true;
        this.hasDoubleJumped = hasDoubleJumped;
    }

    public boolean isDemolished() {
        return isDemolished;
    }

    public void setDemolished(boolean demolished) {
        isDemolished = demolished;
    }

    public Vector3 getUpVector() {
        return upVector;
    }

    public Vector3 getFrontVector() {
        return frontVector;
    }

    public Vector3 getSideVector() {
        return sideVector;
    }

    public boolean isSupersonic() {
        return isSupersonic;
    }

    public boolean isCarOnGround() {
        return isCarOnGround;
    }

    public boolean isMidAir() {
        return isMidAir;
    }

    public boolean isCarUpsideDown() {
        return isCarUpsideDown;
    }

    public boolean isOnWall() {
        return isOnWall;
    }

    public double getDistanceToBall() {
        return distanceToBall;
    }

    public double getAngleToBall() {
        return angleToBall;
    }
}