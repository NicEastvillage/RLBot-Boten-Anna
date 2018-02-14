package tarehart.rlbot;

import com.google.gson.Gson;
import io.grpc.stub.StreamObserver;
import rlbot.api.BotGrpc;
import rlbot.api.GameData;

import java.util.HashMap;
import java.util.Map;

public class GrpcService extends BotGrpc.BotImplBase {

    private Map<Integer, Bot> bots = new HashMap<>();
    private Gson gson = new Gson();

    /* This is where we receive a message from the grpc server, and we wanna send
       something back as an answer. Our answer is a ControllerState
    */
    @Override
    public void getControllerState(GameData.GameTickPacket request, StreamObserver<GameData.ControllerState> responseObserver) {
        // Evaluate the message (GameTickPacket) and respond with a ControllerState
        responseObserver.onNext(evaluateGameTick(request));
        responseObserver.onCompleted();
    }

    /* This is the method were we evaluate the GameTickPacket from the grpc server.
       It returns a ControllerState, which is then sent to Rocket League.
       In other words, THIS IS WERE THE MAGIC HAPPENS
    */
    private GameData.ControllerState evaluateGameTick(GameData.GameTickPacket request) {
        try {
            int playerIndex = request.getPlayerIndex();

            // Do nothing if we know nothing about our car
            if (request.getPlayersCount() <= playerIndex) {
                return new AgentOutput().toControllerState();
            }

            // Setup bot from this packet if necessary
            synchronized (this) {
                if (!bots.containsKey(playerIndex)) {
                    Bot bot = new Bot(playerIndex);
                    bots.put(playerIndex, bot);
                }
            }

            // This is the bot that needs to think
            Bot bot = bots.get(playerIndex);

            // TODO This is a test. Always drive backwards!
            return GameData.ControllerState.newBuilder().setThrottle(-1).build();

        } catch (Exception e) {
            e.printStackTrace();
            // Return default ControllerState on errors
            return new AgentOutput().toControllerState();
        }

    }
}
