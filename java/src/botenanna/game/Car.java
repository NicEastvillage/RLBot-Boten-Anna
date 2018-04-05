package botenanna.game;

import botenanna.math.RLMath;
import botenanna.math.Vector3;
import botenanna.physics.Rigidbody;
import rlbot.api.GameData;

public class Car extends Rigidbody {

        public final int playerIndex;
        public final int team;
        public Vector3 position;
        public  Vector3 velocity;
        public  Vector3 rotation;
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
        public final boolean isOnWall;
        public final double distanceToBall;
        public final double angleToBall;

        public Car(int index, GameData.GameTickPacket packet) {

        //Extends RigidBody
            setPosition(Vector3.convert(packet.getPlayers(index).getLocation()));
            setVelocity(Vector3.convert(packet.getPlayers(index).getVelocity()));
            setRotation(Vector3.convert(packet.getPlayers(index).getRotation()));
            setAcceleration(Vector3.convert(packet.getBall().getAcceleration()));
            setAffectedByGravity(true);

        //Car Specific details
            playerIndex = index;
            GameData.PlayerInfo info = packet.getPlayers(index);
            team = info.getTeam();
            angularVelocity = Vector3.convert(packet.getPlayers(index).getAngularVelocity());
            upVector = RLMath.carUpVector(Vector3.convert(packet.getPlayers(index).getRotation()));
            frontVector = RLMath.carFrontVector(Vector3.convert(packet.getPlayers(index).getRotation()));
            sideVector = RLMath.carSideVector(Vector3.convert(packet.getPlayers(index).getRotation()));
            boost = packet.getPlayers(index).getBoost();
            hasJumped = packet.getPlayers(index).getJumped();
            hasDoubleJumped = packet.getPlayers(index).getDoubleJumped();
            isDemolished = packet.getPlayers(index).getIsDemolished();
            isSupersonic = packet.getPlayers(index).getIsSupersonic();
            isCarOnGround = packet.getPlayers(index).getLocation().getZ() < 20;
            isMidAir = packet.getPlayers(index).getIsMidair();
            isCarUpsideDown = RLMath.carUpVector(Vector3.convert(packet.getPlayers(index).getRotation())).z < 0;
            distanceToBall = Vector3.convert(packet.getPlayers(index).getLocation()).getDistanceTo(Vector3.convert(packet.getBall().getLocation()));
            angleToBall = RLMath.carsAngleToPoint(position.asVector2(), rotation.yaw, Vector3.convert(packet.getBall().getLocation()).asVector2());
            isOnWall = position.y==Situation.ARENA_LENGTH || position.x == Situation.ARENA_WIDTH || position.x == -Situation.ARENA_WIDTH || position.y == -Situation.ARENA_LENGTH;

        }
        // Constructor for simulation
        public Car(Car car, Vector3 position, Vector3 velocity, Vector3 angleVel, Vector3 rotation, Ball ball) {
        //Team Indicators
            team = car.team;
            playerIndex = car.playerIndex;
        //RigidBody
            setPosition(position);
            setVelocity(velocity);
            setRotation(rotation);
            setAffectedByGravity(true);

        //Is calculated as change in angle over time, set as default, but can be calculated as angle change after simulation
            angularVelocity = angleVel;

            upVector = RLMath.carUpVector(rotation);
            frontVector = RLMath.carFrontVector(rotation);
            sideVector = RLMath.carSideVector(rotation);

        //Opdater naar boost er en ting
            boost = car.boost;
            hasJumped = car.hasJumped;
            hasDoubleJumped = car.hasDoubleJumped;
            isDemolished = car.isDemolished;
            isSupersonic = car.isSupersonic;
            isCarOnGround = position.z < 20;
            isMidAir = car.isMidAir;
            isCarUpsideDown = rotation.z < 0;
            distanceToBall = position.getDistanceTo(ball.getPosition());
            angleToBall = RLMath.carsAngleToPoint(position.asVector2(), rotation.yaw, ball.getPosition().asVector2());
            //TODO NEEDS TWEAKING
            isOnWall = position.y==Situation.ARENA_LENGTH || position.x == Situation.ARENA_WIDTH || position.x == -Situation.ARENA_WIDTH || position.y == -Situation.ARENA_LENGTH;

        }
        //Getter

    public int getPlayerIndex() {
        return playerIndex;
    }

    public int getTeam() {
        return team;
    }

    @Override
    public Vector3 getPosition() {
        return position;
    }

    @Override
    public Vector3 getVelocity() {
        return velocity;
    }

    @Override
    public Vector3 getRotation() {
        return rotation;
    }

    public Vector3 getAngularVelocity() {
        return angularVelocity;
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

    public int getBoost() {
        return boost;
    }

    public boolean isHasJumped() {
        return hasJumped;
    }

    public boolean isHasDoubleJumped() {
        return hasDoubleJumped;
    }

    public boolean isDemolished() {
        return isDemolished;
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
