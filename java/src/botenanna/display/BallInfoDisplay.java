package botenanna.display;

import botenanna.game.Situation;
import botenanna.physics.Rigidbody;
import javafx.scene.paint.Color;

/** A display for the ball info. */
public class BallInfoDisplay extends InfoDisplay {

    private static final Color HEADER_COLOR = new Color(0.7, 0.7, 0.7, 1);

    public BallInfoDisplay() {
        super("Ball", HEADER_COLOR);
    }

    /** Update info display. A new situation must be provided. */
    public void update(Situation input) {
        if (input == null)
            return;

        Rigidbody ball = input.getBall();
        infoLabel.setText(String.format(
                "Pos: %s\n" +
                "Vel: %s\n" +
                "Lands: %s\n" +
                "LandsIn: %f sec",
                ball.getPosition().toStringFixedSize(),
                ball.getVelocity().toStringFixedSize(),
                input.getBallLandingPosition().toStringFixedSize(),
                input.getBallLandingTime()));
    }
}
