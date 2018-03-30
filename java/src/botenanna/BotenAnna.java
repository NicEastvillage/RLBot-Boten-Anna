package botenanna;

import botenanna.behaviortree.builder.BehaviourTreeBuilder;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class BotenAnna extends Application {

    public static BehaviourTreeBuilder defaultBTBuilder;

    private GrpcServer grpc;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        createDefaultBehaviourTreeBuilder(stage);
        startGrpcServer();

        VBox root = new VBox();
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
}
