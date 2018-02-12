package tarehart.rlbot;

import tarehart.rlbot.math.SpaceTimeVelocity;
import tarehart.rlbot.physics.ArenaModel;
import tarehart.rlbot.physics.BallPath;
import tarehart.rlbot.planning.Plan;
import tarehart.rlbot.tuning.BallTelemetry;
import tarehart.rlbot.tuning.BotLog;
import tarehart.rlbot.ui.Readout;

import javax.swing.*;
import java.time.Duration;
import java.util.Objects;

public abstract class Bot {

    private final Team team;
    private final int playerIndex;
    Plan currentPlan = null;
    private Readout readout;
    private String previousSituation = null;

    private ArenaModel arenaModel;

    public enum Team {
        BLUE,
        ORANGE
    }

    public Bot(Team team, int playerIndex) {
        this.team = team;
        this.playerIndex = playerIndex;
        readout = new Readout();
        arenaModel = new ArenaModel();
    }


    public AgentOutput processInput(AgentInput input) {

        // Just for now, always calculate ballpath so we can learn some stuff.
        BallPath ballPath = arenaModel.simulateBall(new SpaceTimeVelocity(input.ballPosition, input.time, input.ballVelocity), Duration.ofSeconds(5));
        BallTelemetry.setPath(ballPath, input.team);

        //BallRecorder.recordPosition(new SpaceTimeVelocity(input.ballPosition, input.time, input.ballVelocity));
        //Optional<SpaceTimeVelocity> afterBounce = ballPath.getMotionAfterWallBounce(1);
        // Just for data gathering / debugging.
        //afterBounce.ifPresent(stv -> BallRecorder.startRecording(new SpaceTimeVelocity(input.ballPosition, input.time, input.ballVelocity), stv.getTime().plusSeconds(1)));


        AgentOutput output = getOutput(input);
        Plan.Posture posture = currentPlan != null ? currentPlan.getPosture() : Plan.Posture.NEUTRAL;
        String situation = currentPlan != null ? currentPlan.getSituation() : "";
        if (!Objects.equals(situation, previousSituation)) {
            BotLog.println("[Sitch] " + situation, input.team);
        }
        previousSituation = situation;
        readout.update(input, posture, situation, BotLog.collect(input.team), BallTelemetry.getPath(input.team).get());
        BallTelemetry.reset(input.team);
        return output;
    }

    protected abstract AgentOutput getOutput(AgentInput input);

    protected boolean canInterruptPlanFor(Plan.Posture posture) {
        return currentPlan == null || currentPlan.getPosture().lessUrgentThan(posture) && currentPlan.canInterrupt();
    }

    protected boolean noActivePlanWithPosture(Plan.Posture posture) {
        return currentPlan == null || currentPlan.getPosture() != posture;
    }

    public JFrame getDebugWindow() {
        JFrame frame = new JFrame("Debug - " + team.name());
        frame.setContentPane(readout.getRootPanel());
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.pack();
        return frame;
    }
}
