package botenanna.game;

import botenanna.math.Vector3;
import botenanna.physics.Rigidbody;
import rlbot.api.GameData;

/** A ball */
public class Ball extends Rigidbody {

    public static final double RADIUS = 92.2;
    public static final double DIAMETER = RADIUS * 2;

    public Ball(Vector3 position, Vector3 velocity, Vector3 rotation) {
        setPosition(position);
        setVelocity(velocity);
        setRotation(rotation);
        setAffectedByGravity(true);
    }

    /** Create a ball from the GameData's BallInfo. This way position, velocity,
     * acceleration, rotation and gravity is set immediately. */
    public Ball(GameData.BallInfo ball) {
        setPosition(Vector3.convert(ball.getLocation()));
        setVelocity(Vector3.convert(ball.getVelocity()));
        setAcceleration(Vector3.convert(ball.getAcceleration()));
        setRotation(Vector3.convert(ball.getRotation()));
        setAffectedByGravity(true);
    }
}
