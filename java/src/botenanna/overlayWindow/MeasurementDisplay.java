package botenanna.overlayWindow;

import botenanna.AgentInput;
import botenanna.Ball;
import botenanna.Bot;
import botenanna.physics.TimeTracker;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class MeasurementDisplay extends VBox {

    public static final Color HEADER_COLOR = new Color(0.7, 0.7, 0.7, 1);

    private Label infoLabel;
    private TimeTracker time;
    private double timeSinceStart = 0;
    private TextField timeInfo = new TextField("0");

    public MeasurementDisplay() {
        super();
        HBox header = new HBox();
        getChildren().add(header);
        header.setPadding(new Insets(3, 5, 3, 5));
        header.setBackground(new Background(new BackgroundFill(HEADER_COLOR, null, null)));

        Button button = new Button();
        button.setText("Start timer");


        button.setOnMouseClicked(event -> {
            button.setText("Stop Timer");
            time.startTimer();
        });


        Label headerLabel = new Label("Car Velocity");
        header.getChildren().add(headerLabel);
        //timeSinceStart = time.getElapsedSecondsTimer();
        //timeInfo.setText(Double.toString((timeSinceStart)));

        infoLabel = new Label("No data");
        infoLabel.setPadding(new Insets(2, 3, 4, 10));
        infoLabel.setFont(new Font("Courier New", 14));
        getChildren().add(infoLabel);
        getChildren().add(button);
        //getChildren().add(timeInfo);
    }

    public void update(AgentInput input) {
        if (input == null)
            return;
        TimeTracker time = new TimeTracker();
        AgentInput.Car car = input.myCar;
        infoLabel.setText(
                "Vel: %f\n" +
                 car.velocity.getMagnitude());
    }
}