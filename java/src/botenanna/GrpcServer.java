package botenanna;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.stream.Stream;

public class GrpcServer {

    private static final int DEFAULT_PORT = 25368;
    private static final String PORT_FILE_NAME = "port.txt";

    private final int port;
    private final Server server;

    public GrpcServer() throws IOException {

        // Scenario: you finished your bot and submitted it to a tournament. Your opponent hard-coded the same
        // as you, and the match can't start because of the conflict. Because of this line, you can ask the
        // organizer make a file called "port.txt" in the same directory as your .jar, and put some other number in it.
        // This matches code in JavaAgent.py
        port = readPortFromFile().orElse(DEFAULT_PORT);

        server = ServerBuilder.forPort(port).addService(new GrpcService()).build();
    }

    /** Start serving requests. */
    public void start() throws IOException {
        server.start();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            // Use stderr here since the logger may has been reset by its JVM shutdown hook.
            System.err.println("*** shutting down gRPC server since JVM is shutting down");
            GrpcServer.this.stop();
            System.err.println("*** server shut down");
        }));
    }

    /** Stop serving requests and shutdown resources. */
    public void stop() {
        if (server != null) {
            server.shutdown();
        }
    }

    private static Optional<Integer> readPortFromFile() {
        try {
            Stream<String> lines = Files.lines(Paths.get(PORT_FILE_NAME));
            Optional<String> firstLine = lines.findFirst();
            return firstLine.map(Integer::parseInt);
        } catch (NumberFormatException e) {
            System.out.println("Failed to parse port file! Will proceed with hard-coded port number.");
            return Optional.empty();
        } catch (Throwable e) {
            return Optional.empty();
        }
    }

    public int getPort() {
        return port;
    }
}
