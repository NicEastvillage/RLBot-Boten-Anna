package tarehart.rlbot.steps;

import tarehart.rlbot.math.vector.Vector2;
import tarehart.rlbot.math.vector.Vector3;
import tarehart.rlbot.AgentInput;
import tarehart.rlbot.AgentOutput;
import tarehart.rlbot.input.CarData;
import tarehart.rlbot.math.SpaceTimeVelocity;
import tarehart.rlbot.math.VectorUtil;
import tarehart.rlbot.physics.ArenaModel;
import tarehart.rlbot.physics.BallPath;
import tarehart.rlbot.physics.DistancePlot;
import tarehart.rlbot.planning.*;
import tarehart.rlbot.tuning.BotLog;

import java.time.Duration;
import java.util.Optional;

public class GetOnOffenseStep implements Step {

    private Plan plan;

    public static double getYAxisWrongSidedness(AgentInput input) {
        Vector3 ownGoalCenter = GoalUtil.getOwnGoal(input.team).getCenter();
        double playerToBallY = input.ballPosition.y - input.getMyCarData().position.y;
        return playerToBallY * Math.signum(ownGoalCenter.y);
    }

    public Optional<AgentOutput> getOutput(AgentInput input) {

        if (plan != null && !plan.isComplete()) {
            Optional<AgentOutput> output = plan.getOutput(input);
            if (output.isPresent()) {
                return output;
            }
        }

        CarData car = input.getMyCarData();

        if (car.boost < 10 && GetBoostStep.seesOpportunisticBoost(car, input.fullBoosts)) {
            plan = new Plan().withStep(new GetBoostStep());
            plan.begin();
            return plan.getOutput(input);
        }

        Goal enemyGoal = GoalUtil.getEnemyGoal(input.team);
        Goal ownGoal = GoalUtil.getOwnGoal(input.team);

        BallPath ballPath = ArenaModel.predictBallPath(input, input.time, Duration.ofSeconds(2));

        Vector3 target = input.ballPosition;
        SpaceTimeVelocity futureMotion = ballPath.getMotionAt(input.time.plusSeconds(2)).get();
        if (input.ballVelocity.y * (enemyGoal.getCenter().y - input.ballPosition.y) < 0) {
            // if ball is rolling away from the enemy goal
            target = futureMotion.getSpace();
        }

        if (futureMotion.getSpace().distance(enemyGoal.getCenter())  < ArenaModel.SIDE_WALL * .8) {
            // Get into a strike position, 10 units behind the ball
            Vector3 goalToBall = target.minus(enemyGoal.getCenter());
            Vector3 goalToBallNormal = goalToBall.normaliseCopy();
            target = target.plus(goalToBallNormal.scaled(10));

        } else {
            // Get into a backstop position
            Vector3 goalToBall = target.minus(ownGoal.getCenter());
            Vector3 goalToBallNormal = goalToBall.normaliseCopy();
            target = target.minus(goalToBallNormal.scaled(10));
        }



        double flatDistance = VectorUtil.flatDistance(target, car.position);
        if (getYAxisWrongSidedness(input) < 0) {
            return Optional.empty();
        }
        Vector3 targetToBallFuture = futureMotion.getSpace().minus(target);

        DistancePlot plot = AccelerationModel.simulateAcceleration(car, Duration.ofSeconds(4), 0);


        Optional<Vector2> circleTurnOption = SteerUtil.getWaypointForCircleTurn(car, plot, target.flatten(), targetToBallFuture.flatten().normaliseCopy());

        if (circleTurnOption.isPresent()) {
            Vector2 circleTurn = circleTurnOption.get();
            Optional<Plan> sensibleFlip = SteerUtil.getSensibleFlip(car, circleTurn);
            if (sensibleFlip.isPresent()) {
                BotLog.println("Front flip onto offense", input.team);
                this.plan = sensibleFlip.get();
                this.plan.begin();
                return this.plan.getOutput(input);
            }

            return Optional.of(SteerUtil.steerTowardGroundPosition(car, circleTurn));
        }

        return Optional.of(SteerUtil.steerTowardGroundPosition(car, target));
    }

    @Override
    public boolean isBlindlyComplete() {
        return false;
    }

    @Override
    public void begin() {
    }

    @Override
    public boolean canInterrupt() {
        return plan == null || plan.canInterrupt();
    }

    @Override
    public String getSituation() {
        return "Getting on offense";
    }
}
