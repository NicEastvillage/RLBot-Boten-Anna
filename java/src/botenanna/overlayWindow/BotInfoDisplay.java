package botenanna.overlayWindow;

import botenanna.AgentInput;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class BotInfoDisplay extends VBox {

    private Label infoLabel;

    public BotInfoDisplay() {
        super();
        infoLabel = new Label("New bot ...");
        getChildren().add(infoLabel);
    }

    public void update(AgentInput input) {
        infoLabel.setText(String.format(
                "Pos: %s\n" +
                "Vel: %s\n" +
                "Acc: %s\n" +
                "Rot: %s\nAngToBall: %f",
                input.myLocation.toString(),
                input.myVelocity.toString(),
                input.myAngularVelocity.toString(),
                input.myRotation.toString(),
                input.angleToBall));
    }
}
