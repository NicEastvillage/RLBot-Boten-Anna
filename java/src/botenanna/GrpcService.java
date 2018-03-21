package botenanna;

import botenanna.behaviortree.BehaviorTree;
import botenanna.physics.TimeTracker;
import io.grpc.stub.StreamObserver;
import rlbot.api.BotGrpc;
import rlbot.api.GameData;

import java.util.HashMap;
import java.util.Map;

public class GrpcService extends BotGrpc.BotImplBase {
    public Bot bot;
    private TimeTracker timeTracker = new TimeTracker();
    private Map<Integer, Bot> registeredBots = new HashMap<>();

    /**
     * This is where we receive a message from the grpc server, and we wanna send
     * something back as an answer. Our answer is a ControllerState
     */
    @Override
    public void getControllerState(GameData.GameTickPacket request, StreamObserver<GameData.ControllerState> responseObserver) {
        // Evaluate the message (GameTickPacket) and respond with a ControllerState
        responseObserver.onNext(evaluateGameTick(request));
        responseObserver.onCompleted();
    }

    /**
     * This is the method were we evaluate the GameTickPacket from the grpc server.
     * It returns a ControllerState, which is then sent to Rocket League.
     * In other words, THIS IS WHERE THE MAGIC HAPPENS
     */
    public GameData.ControllerState evaluateGameTick(GameData.GameTickPacket request) {
        try {
            int playerIndex = request.getPlayerIndex();

            // If the index of this player is greater than the playerCount,
            // then we don't know anything about this car
            if (request.getPlayersCount() <= playerIndex) {
                return new AgentOutput().toControllerState();
            }

            request.getGameInfo().getGameTimeRemaining();

            // Rework the package
            AgentInput input = new AgentInput(request, timeTracker);

            // Create and register bot from this packet if necessary
            synchronized (this) {
                if (!registeredBots.containsKey(playerIndex)) {
                    int teamIndex = request.getPlayers(playerIndex).getTeam() % 2;
                    BehaviorTree tree = GrpcServer.statusWindow.getBtBuilder().build();
                    Bot bot = new Bot(playerIndex, teamIndex, tree);
                    registeredBots.put(playerIndex, bot);
                }
            }

            // This is the bot that needs to think
            Bot bot = registeredBots.get(playerIndex);


            // Update status window with new data
            GrpcServer.statusWindow.updateData(input, bot);

            return bot.process(input).toControllerState();

        } catch (Exception e) {
            e.printStackTrace();
            // Return default ControllerState on errors
            return new AgentOutput().toControllerState();
        }
    }
}
