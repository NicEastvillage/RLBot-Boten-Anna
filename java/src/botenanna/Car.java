package botenanna;

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
        public final double distanceToBall;
        public final double angleToBall;

        public Car(int index, GameData.GameTickPacket packet) {

            /*setAcceleration(Vector3.convert(ball.getAcceleration()));
            setAffectedByGravity(true);*/
        //Extends RigidBody
            setPosition(Vector3.convert(packet.getPlayers(index).getLocation()));
            setVelocity(Vector3.convert(packet.getPlayers(index).getVelocity()));
            setRotation(Vector3.convert(packet.getPlayers(index).getRotation()));

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
        }
        // Constructor for simulation
        public Car(Car MyCar, Vector3 position, Vector3 velocity, Vector3 angleVel, Vector3 rotation, Ball ball) {
            team = MyCar.team;
            playerIndex = MyCar.playerIndex;
            setPosition(position);
            setVelocity(velocity);
            setRotation(rotation);
            angularVelocity = angleVel;
            upVector = RLMath.carUpVector(rotation);
            frontVector = RLMath.carFrontVector(rotation);
            sideVector = RLMath.carSideVector(rotation);
            //OPdater naar boost er en ting
            boost = MyCar.boost;
            hasJumped = MyCar.hasJumped;
            hasDoubleJumped = MyCar.hasDoubleJumped;
            isDemolished = MyCar.isDemolished;
            isSupersonic = MyCar.isSupersonic;
            isCarOnGround = position.z < 20;
            isMidAir = MyCar.isMidAir;
            isCarUpsideDown = rotation.z < 0;
            distanceToBall = position.getDistanceTo(ball.getPosition());
            angleToBall = RLMath.carsAngleToPoint(position.asVector2(), rotation.yaw, ball.getPosition().asVector2());
        
    }
}
