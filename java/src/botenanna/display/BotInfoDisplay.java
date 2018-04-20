package botenanna.display;

import botenanna.BotenAnna;
import botenanna.behaviortree.BehaviorTree;
import botenanna.game.Situation;
import botenanna.Bot;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.io.IOException;

public class BotInfoDisplay extends VBox {

    public static final Color BLUE = new Color(.34, .42, 1, 1);
    public static final Color ORANGE = new Color(1, 0.7, 0.3, 1);

    private Bot bot;
    private Label infoLabel;

    public BotInfoDisplay(Bot bot) {
        super();
        this.bot = bot;

        HBox header = new HBox();
        getChildren().add(header);
        header.setPadding(new Insets(3, 5, 3, 5));
        Color color = bot.getTeam() == Bot.Team.BLUE ? BLUE : ORANGE;
        header.setBackground(new Background(new BackgroundFill(color, null, null)));

        Label headerLabel = new Label("Car #" + bot.getPlayerIndex());
        header.getChildren().add(headerLabel);

        Pane fillPane = new Pane();
        header.getChildren().add(fillPane);
        HBox.setHgrow(fillPane, Priority.ALWAYS);

        Button changeBt = new Button("Tree");
        changeBt.setFont(new Font(10));
        changeBt.setPadding(new Insets(1, 4, 1, 4));
        changeBt.setPrefHeight(16);
        changeBt.setOnAction(e -> changeBehaviourTree());
        header.getChildren().add(changeBt);

        infoLabel = new Label("No data");
        infoLabel.setPadding(new Insets(2, 3, 4, 10));
        infoLabel.setFont(new Font("Courier New", 14));
        getChildren().add(infoLabel);
    }

    public void update() {
        Situation input = bot.getLastInputReceived();
        if (input == null || input.myCar.getPosition() == null)
            return;

        infoLabel.setText(String.format(
                "Pos: %s\n" +
                "Vel: %s\n" +
                "Rot: %s\n" +
                "AngToBall: %f\n" +
                "HasPossession: %b",
                input.myCar.getPosition().toStringFixedSize(),
                input.myCar.getVelocity().toStringFixedSize(),
                input.myCar.getRotation().toStringFixedSize(),
                input.myCar.getAngleToBall(),
                input.whoHasPossession()));
    }

    /** Change the behaviour of the bot connected to this display. */
    private void changeBehaviourTree() {
        try {
            BehaviorTree tree = BotenAnna.defaultBTBuilder.buildFromFileChooser();
            if (tree != null) {
                bot.setBehaviorTree(tree);
            }
        } catch (IOException e) {
            // Do nothing
        }
    }
}
