package botenanna;

import botenanna.math.Vector3;
import botenanna.physics.Path;
import botenanna.physics.Rigidbody;
import rlbot.api.GameData;

/** Ball constants */
public class Ball {

    public static final double RADIUS = 92.2;
    public static final double DIAMETER = RADIUS * 2;
    public static final double SLIDE_DECCELERATION = -230;
    public static final double BALL_GROUND_BOUNCINESS = -0.6;
    public static final double BALL_WALL_BOUNCINESS = -0.6;

    /** Create a ball from the GameData's BallInfo. This way position, velocity,
     * acceleration, rotation and gravity is set immediately. */
    public Rigidbody get(GameData.BallInfo ball) {
        Rigidbody body = new Rigidbody();
        body.setPosition(Vector3.convert(ball.getLocation()));
        body.setVelocity(Vector3.convert(ball.getVelocity()));
        body.setAcceleration(Vector3.convert(ball.getAcceleration()));
        body.setRotation(Vector3.convert(ball.getRotation()));
        body.setAngularVelocity(Vector3.convert(ball.getAngularVelocity()));
        return body;
    }
}
