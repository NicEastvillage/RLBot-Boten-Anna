package tarehart.rlbot;

import com.google.gson.Gson;
import io.grpc.stub.StreamObserver;
import rlbot.api.BotGrpc;
import rlbot.api.GameData;

import java.util.HashMap;
import java.util.Map;

public class GrpcService extends BotGrpc.BotImplBase {

    //private Map<Integer, Bot> bots = new HashMap<>();
    private Gson gson = new Gson();

    private long frameCount = 0;

    @Override
    public void getControllerState(GameData.GameTickPacket request, StreamObserver<GameData.ControllerState> responseObserver) {
        responseObserver.onNext(doGetControllerState(request));
        responseObserver.onCompleted();
    }

    private GameData.ControllerState doGetControllerState(GameData.GameTickPacket request) {


        try {
            int playerIndex = request.getPlayerIndex();

            // Do nothing if we know nothing about our car
            if (request.getPlayersCount() <= playerIndex) {
                return new AgentOutput().toControllerState();
            }

            // Setup bot from this packet if necessary
            /*synchronized (this) {
                if (!bots.containsKey(playerIndex)) {
                    ReliefBot bot = new ReliefBot(translatedInput.team, playerIndex);
                    bots.put(playerIndex, bot);
                    statusSummary.markTeamRunning(translatedInput.team, playerIndex, bot.getDebugWindow());
                }
            }*/

            // Bot bot = bots.get(playerIndex);

            //return bot.processInput(translatedInput).toControllerState();

            return GameData.ControllerState.newBuilder().setThrottle(-1).build();
        } catch (Exception e) {
            e.printStackTrace();
            return new AgentOutput().toControllerState();
        }

    }
}
