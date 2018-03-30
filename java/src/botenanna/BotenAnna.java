package botenanna;

import botenanna.behaviortree.builder.BehaviourTreeBuilder;
import botenanna.overlayWindow.BotInfoDisplay;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.*;

import java.util.HashMap;
import java.util.Map;

public class BotenAnna extends Application {

    public static BotenAnna instance; // FIXME We don't want BotenAnna to be a singleton;

    public static BehaviourTreeBuilder defaultBTBuilder;

    private Pane root;
    private GrpcServer grpc;
    private Map<Bot, BotInfoDisplay> botBotInfoDisplays;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        instance = this;

        createDefaultBehaviourTreeBuilder(stage);
        startGrpcServer();

        botBotInfoDisplays = new HashMap<>();

        root = new VBox();
        Scene scene = new Scene(root, 300, 300);
        stage.setScene(scene);
        stage.setTitle("Boten Anna - Debug");
        stage.setAlwaysOnTop(true);
        stage.show();
    }

    private void startGrpcServer() throws Exception {
        grpc = new GrpcServer();
        grpc.start();
        System.out.println(String.format("Grpc server started on port %s. Listening for Rocket League data!", grpc.getPort()));
    }

    private void createDefaultBehaviourTreeBuilder(Stage stage) {
        defaultBTBuilder = new BehaviourTreeBuilder(stage);
        defaultBTBuilder.setFileWithChooser();
        try {
            // Build a behaviour tree to make sure file is valid. The tree is immediate discarded
            defaultBTBuilder.build();
        } catch (Exception e) {
            System.out.println("Error when opening behaviour tree source file: " + e.getMessage());
            System.exit(-1);
        }
    }

    public void updateBotInfoDisplay(Bot bot, AgentInput input) {
        if (!botBotInfoDisplays.containsKey(bot)) {
            BotInfoDisplay display = new BotInfoDisplay();
            root.getChildren().add(display);
            botBotInfoDisplays.put(bot, display);
        }
        botBotInfoDisplays.get(bot).update(input);
    }
}
