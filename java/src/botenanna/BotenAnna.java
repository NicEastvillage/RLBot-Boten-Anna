package botenanna;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class BotenAnna extends Application {

    private GrpcServer grpc;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        grpc = new GrpcServer();
        grpc.start();
        System.out.println(String.format("Grpc server started on port %s. Listening for Rocket League data!", grpc.getPort()));

        VBox root = new VBox();
        Scene scene = new Scene(root, 300, 300);
        stage.setScene(scene);
        stage.setTitle("Boten Anna - Debug");
        stage.setAlwaysOnTop(true);
        stage.show();
    }
}
