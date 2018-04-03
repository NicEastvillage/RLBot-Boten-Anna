package botenanna.overlayWindow;

import botenanna.AgentInput;
import botenanna.Ball;
import botenanna.Bot;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class MeasurementDisplay extends VBox {
    public static final Color BLUE = new Color(.34, .42, 1, 1);
    public static final Color ORANGE = new Color(1, 0.7, 0.3, 1);

    private Label infoLabel;

    public MeasurementDisplay(Bot bot) {
        super();
        HBox header = new HBox();
        getChildren().add(header);
        header.setPadding(new Insets(3, 5, 3, 5));
        Color color = bot.getTeam() == Bot.Team.BLUE ? BLUE : ORANGE;
        header.setBackground(new Background(new BackgroundFill(color, null, null)));

        Label headerLabel = new Label("Velocity - Car #" + bot.getPlayerIndex());
        header.getChildren().add(headerLabel);

        infoLabel = new Label("No data");
        infoLabel.setPadding(new Insets(2, 3, 4, 10));
        infoLabel.setFont(new Font("Courier New", 14));
        getChildren().add(infoLabel);
    }

    public void update(Bot bot) {
        AgentInput input = bot.getLastInputReceived();
        if (input == null)
            return;

        infoLabel.setText(String.format(
                "Vel: %f\n",
                input.myCar.velocity.getMagnitude()));
    }
}
