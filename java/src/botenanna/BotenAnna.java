package botenanna;

import botenanna.behaviortree.builder.BehaviourTreeBuilder;
import botenanna.overlayWindow.BallInfoDisplay;
import botenanna.overlayWindow.BotInfoDisplay;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;

public class BotenAnna extends Application {

    public static BehaviourTreeBuilder defaultBTBuilder;

    private Pane root;
    private Pane botInfoDisplayRoot;
    private GrpcServer grpc;
    private Map<Bot, BotInfoDisplay> botInfoDisplays;
    private BallInfoDisplay ballInfoDisplay;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        // Technical stuff
        createDefaultBehaviourTreeBuilder(stage);
        startGrpcServerAndInputListener();

        root = new VBox();

        // Info displays
        botInfoDisplayRoot = new VBox();
        root.getChildren().add(botInfoDisplayRoot);
        botInfoDisplays = new HashMap<>();
        ballInfoDisplay = new BallInfoDisplay();
        root.getChildren().add(ballInfoDisplay);

        Scene scene = new Scene(root, 440, 340);
        stage.setScene(scene);
        stage.setTitle("Boten Anna - Debug");
        stage.setAlwaysOnTop(true);
        stage.show();
    }

    private void startGrpcServerAndInputListener() throws Exception {
        // The javafx application thread does not allow other threads to call its methods
        // so a multi-thread safe queue is used to send data between the window and the server
        final ArrayBlockingQueue<Bot> botUpdateQueue = new ArrayBlockingQueue<>(3);
        grpc = new GrpcServer(botUpdateQueue);
        grpc.start();
        System.out.println(String.format("Grpc server started on port %s. Listening for Rocket League data!", grpc.getPort()));

        // Setup listener that acts whenever a bot update is placed in botUpdateQueue
        final LongProperty lastUpdate = new SimpleLongProperty();
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (now - lastUpdate.get() > 0) {
                    final Bot bot = botUpdateQueue.poll();
                    if (bot != null) {
                        updateBotInfoDisplay(bot);
                        ballInfoDisplay.update(bot.getLastInputReceived());
                    }
                    lastUpdate.set(now);
                }
            }
        };
        timer.start();
    }

    private void createDefaultBehaviourTreeBuilder(Stage stage) {
        defaultBTBuilder = new BehaviourTreeBuilder(stage);
        defaultBTBuilder.setupDefaultFile();
        try {
            // Build a behaviour tree to make sure file is valid. The tree is immediately discarded
            defaultBTBuilder.buildUsingDefault();
        } catch (Exception e) {
            System.out.println("Error when opening behaviour tree source file: " + e.getMessage());
            System.exit(-1);
        }
    }

    public void updateBotInfoDisplay(Bot bot) {
        // Create new display if it is a new bot
        if (!botInfoDisplays.containsKey(bot)) {
            BotInfoDisplay display = new BotInfoDisplay(bot);
            botInfoDisplayRoot.getChildren().add(display);
            botInfoDisplays.put(bot, display);
        }
        botInfoDisplays.get(bot).update(bot);
    }
}
