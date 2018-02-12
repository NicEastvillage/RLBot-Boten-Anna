package tarehart.rlbot;

import tarehart.rlbot.input.CarData;
import tarehart.rlbot.physics.ArenaModel;
import tarehart.rlbot.physics.BallPath;
import tarehart.rlbot.planning.*;
import tarehart.rlbot.steps.GoForKickoffStep;
import tarehart.rlbot.steps.landing.LandGracefullyStep;
import tarehart.rlbot.tuning.BotLog;

import java.time.Duration;
import java.util.Optional;

public class ReliefBot extends Bot {

    private TacticsAdvisor tacticsAdvisor;

    public ReliefBot(Team team, int playerIndex) {
        super(team, playerIndex);
        tacticsAdvisor = new TacticsAdvisor();
    }

    @Override
    protected AgentOutput getOutput(AgentInput input) {

        final CarData car = input.getMyCarData();

//        if (canInterruptPlanFor(Plan.Posture.OVERRIDE)) {
//            currentPlan = new Plan(Plan.Posture.OVERRIDE).withStep(new InterceptStep(new Vector3()));
//            currentPlan.begin();
//        }

        // Kickoffs can happen unpredictably because the bot doesn't know about goals at the moment.
        if (noActivePlanWithPosture(Plan.Posture.KICKOFF) && input.ballPosition.flatten().magnitudeSquared() == 0) {
            currentPlan = new Plan(Plan.Posture.KICKOFF).withStep(new GoForKickoffStep());
            currentPlan.begin();
        }

        if (canInterruptPlanFor(Plan.Posture.LANDING) && !ArenaModel.isCarOnWall(car) &&
                !ArenaModel.isNearFloorEdge(car) &&
                car.position.z > 5 &&
                !ArenaModel.isBehindGoalLine(car.position)) {
            currentPlan = new Plan(Plan.Posture.LANDING).withStep(new LandGracefullyStep(LandGracefullyStep.FACE_BALL));
            currentPlan.begin();
        }

        BallPath ballPath = ArenaModel.predictBallPath(input, input.time, Duration.ofSeconds(7));
        TacticalSituation situation = tacticsAdvisor.assessSituation(input, ballPath);

        if (situation.scoredOnThreat.isPresent() && canInterruptPlanFor(Plan.Posture.SAVE)) {
            BotLog.println("Need to go for save! Canceling current plan.", input.team);
            currentPlan = null;
        } else if (situation.needsDefensiveClear && canInterruptPlanFor(Plan.Posture.CLEAR)) {
            BotLog.println("Going for clear! Canceling current plan.", input.team);
            currentPlan = null;
        } else if (situation.shotOnGoalAvailable && canInterruptPlanFor(Plan.Posture.OFFENSIVE)) {
            BotLog.println("Shot opportunity! Canceling current plan.", input.team);
            currentPlan = null;
        }

        if (currentPlan == null || currentPlan.isComplete()) {
            currentPlan = tacticsAdvisor.makePlan(input, situation);
            currentPlan.begin();
        }

        if (currentPlan != null) {
            if (currentPlan.isComplete()) {
                currentPlan = null;
            } else {
                Optional<AgentOutput> output = currentPlan.getOutput(input);
                if (output.isPresent()) {
                    return output.get();
                }
            }
        }

        return SteerUtil.steerTowardGroundPosition(car, input.ballPosition);
    }
}
