package botenanna.overlayWindow;

import botenanna.AgentInput;
import botenanna.Ball;
import botenanna.Bot;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class BallInfoDisplay extends VBox {

    public static final Color HEADER_COLOR = new Color(0.7, 0.7, 0.7, 1);

    private Label infoLabel;

    public BallInfoDisplay() {
        super();
        HBox header = new HBox();
        getChildren().add(header);
        header.setPadding(new Insets(3, 5, 3, 5));
        header.setBackground(new Background(new BackgroundFill(HEADER_COLOR, null, null)));

        Label headerLabel = new Label("Ball");
        header.getChildren().add(headerLabel);

        infoLabel = new Label("No data");
        infoLabel.setPadding(new Insets(2, 3, 4, 10));
        infoLabel.setFont(new Font("Courier New", 14));
        getChildren().add(infoLabel);
    }

    public void update(AgentInput input) {
        if (input == null)
            return;

        Ball ball = input.ball;
        infoLabel.setText(String.format(
                "Pos: %s\n" +
                "Vel: %s\n" +
                "Lands: %s\n" +
                "LandsIn: %f sec",
                ball.getPosition().toStringFixedSize(),
                ball.getVelocity().toStringFixedSize(),
                input.ballLandingPosition.toStringFixedSize(),
                input.ballLandingTime));
    }
}
