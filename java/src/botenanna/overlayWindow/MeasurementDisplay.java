package botenanna.overlayWindow;

import botenanna.AgentInput;
import botenanna.physics.TimeTracker;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.io.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MeasurementDisplay extends VBox {

    public static final Color HEADER_COLOR = new Color(0.7, 0.7, 0.7, 1);

    private Label infoLabel;
    private TimeTracker time = new TimeTracker();
    private AgentInput.Car car;

    public MeasurementDisplay() throws FileNotFoundException {
        super();
        HBox header = new HBox();
        getChildren().add(header);
        header.setPadding(new Insets(3, 5, 3, 5));
        header.setBackground(new Background(new BackgroundFill(HEADER_COLOR, null, null)));

        Button button = new Button();
        button.setText("Start Timer");

        ScheduledExecutorService timer;
        Runnable timerRunnable;
        Label headerLabel;

        timer = new ScheduledThreadPoolExecutor(1);
        NumberFormat nf = new DecimalFormat("##.#");
        timerRunnable = () -> {
            System.out.println(time.getElapsedSecondsTimer());
            try (PrintWriter out = new PrintWriter(new FileWriter("/Users/mathiashindsgaul/Desktop/velocity.txt",true))) {
                out.println("Time: \t" + nf.format(time.getElapsedSecondsTimer()) + "\t\tVelocity: \t "); //+ nf.format(car.velocity.getMagnitude()));
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        };

        button.setOnMouseClicked(event -> {
            time.startTimer();
            button.setText("Reset Timer");
            final ScheduledFuture<?> timerHandle = timer.scheduleAtFixedRate(
                    timerRunnable, 0, 1, TimeUnit.SECONDS);
            timer.schedule(() -> {
                timerHandle.cancel(true);
            }, 5, TimeUnit.SECONDS);
            try (PrintWriter out = new PrintWriter(new FileWriter("/Users/mathiashindsgaul/Desktop/velocity.txt",true))) {
                out.println("");
                out.println("Instance");
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        });

        addEventFilter(KeyEvent.KEY_PRESSED,event -> {
            if(event.getCode() == KeyCode.L) {
                time.startTimer();
                button.setText("Reset Timer");
                final ScheduledFuture<?> timerHandle = timer.scheduleAtFixedRate(
                        timerRunnable, 0, 1, TimeUnit.SECONDS);
                        timer.schedule(() -> {
                            timerHandle.cancel(true);
                            }, 5, TimeUnit.SECONDS);
                try (PrintWriter out = new PrintWriter(new FileWriter("/Users/mathiashindsgaul/Desktop/velocity.txt",true))) {
                    out.println("");
                    out.println("Instance");
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        headerLabel = new Label("Car Velocity");
        header.getChildren().add(headerLabel);


        infoLabel = new Label("No data");
        infoLabel.setPadding(new Insets(2, 3, 4, 10));
        infoLabel.setFont(new Font("Courier New", 14));
        getChildren().add(infoLabel);
        getChildren().add(button);

    }

    public void update(AgentInput input) {
        if (input == null)
            return;
        if (input.myCar.playerIndex == 0) {
            car = input.myCar;
            infoLabel.setText(
                    "Vel: %f\n" +
                            car.velocity.getMagnitude());
        }
    }
}