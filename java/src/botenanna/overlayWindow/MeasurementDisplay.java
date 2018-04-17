package botenanna.overlayWindow;

import botenanna.AgentInput;
import botenanna.physics.TimeTracker;
import io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue;
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
import sun.management.Agent;

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
    private boolean timerIsRunning;
    private double newVelocity;
    private double lastVelocity;
    private double newRotation;
    private double lastRotation;

    public MeasurementDisplay() throws FileNotFoundException {
        super();
        HBox header = new HBox();
        getChildren().add(header);
        header.setPadding(new Insets(3, 5, 3, 5));
        header.setBackground(new Background(new BackgroundFill(HEADER_COLOR, null, null)));

        Button button = new Button();
        button.setText("Start Timer");

        ScheduledExecutorService timer;
        Label headerLabel;

        timer = new ScheduledThreadPoolExecutor(1);
        NumberFormat nf = new DecimalFormat("##.#####");

        button.setOnMouseClicked(event -> {
            startMeasurement(button, timer);
        });

        addEventFilter(KeyEvent.KEY_PRESSED,event -> {
            if(event.getCode() == KeyCode.L) {
                startMeasurement(button, timer);
            }
        });

        addEventFilter(KeyEvent.KEY_PRESSED,event -> {
            if(event.getCode() == KeyCode.ESCAPE) {
                System.exit(0);
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

    private void startMeasurement(Button button, ScheduledExecutorService timer) {
        time.startTimer();
        button.setText("Reset Timer");
        timerIsRunning = true;

        final ScheduledFuture<?> timerHandle = timer.scheduleAtFixedRate(() -> {
            //System.out.println(time.getElapsedSecondsTimer());
            //new Velocity
        }, 0, 17, TimeUnit.MILLISECONDS);

        timer.schedule(() -> {
            timerHandle.cancel(true);
            timerIsRunning = false;
        }, 4, TimeUnit.SECONDS);
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
        if(timerIsRunning) {
            File file = new File(System.getProperty("user.home"), "/Desktop/velocity.csv");
            NumberFormat nf = new DecimalFormat("##.#####");
            newVelocity = car.velocity.getMagnitude();
            newRotation = car.rotation.yaw;
            if (newVelocity != lastVelocity) {
                System.out.println(time.getElapsedSecondsTimer());
                try (PrintWriter out = new PrintWriter(new FileWriter(file, true))) {
                            out.println(nf.format(time.getElapsedSecondsTimer()) + ";"
                            + nf.format(lastVelocity) + ";"
                            + nf.format((newVelocity - lastVelocity)/input.getDeltaTime()) + ";"
                            + nf.format(lastRotation) + ";"
                            + nf.format((newRotation-lastRotation)/input.getDeltaTime()) + ";"
                            + nf.format(car.angularVelocity + ";"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                lastVelocity = newVelocity;
                lastRotation = newRotation;
            }
        }

    }
}