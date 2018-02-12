package tarehart.rlbot.planning;

import tarehart.rlbot.math.vector.Vector2;
import tarehart.rlbot.math.vector.Vector3;
import tarehart.rlbot.AgentInput;
import tarehart.rlbot.input.CarData;
import tarehart.rlbot.math.SpaceTime;
import tarehart.rlbot.math.SpaceTimeVelocity;
import tarehart.rlbot.math.TimeUtil;
import tarehart.rlbot.math.VectorUtil;
import tarehart.rlbot.physics.ArenaModel;
import tarehart.rlbot.physics.BallPath;
import tarehart.rlbot.physics.DistancePlot;
import tarehart.rlbot.steps.CatchBallStep;
import tarehart.rlbot.steps.DribbleStep;
import tarehart.rlbot.steps.GetBoostStep;
import tarehart.rlbot.steps.GetOnOffenseStep;
import tarehart.rlbot.steps.defense.GetOnDefenseStep;
import tarehart.rlbot.steps.defense.WhatASaveStep;
import tarehart.rlbot.steps.strikes.*;
import tarehart.rlbot.steps.wall.DescendFromWallStep;
import tarehart.rlbot.steps.wall.MountWallStep;
import tarehart.rlbot.steps.wall.WallTouchStep;
import tarehart.rlbot.tuning.BotLog;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

public class TacticsAdvisor {

    private static final double LOOKAHEAD_SECONDS = 2;

    public TacticsAdvisor() {
    }

    public Plan makePlan(AgentInput input, TacticalSituation situation) {

        if (situation.scoredOnThreat.isPresent()) {
            return new Plan(Plan.Posture.SAVE).withStep(new WhatASaveStep());
        }

        if (situation.needsDefensiveClear) {
            return new Plan(Plan.Posture.CLEAR).withStep(new IdealDirectedHitStep(new KickAwayFromOwnGoal(), input));
        }

        if (situation.shotOnGoalAvailable) {
            return new Plan(Plan.Posture.OFFENSIVE).withStep(new IdealDirectedHitStep(new KickAtEnemyGoal(), input));
        }

        Duration planHorizon = Duration.ofSeconds(5);

        CarData car = input.getMyCarData();
        BallPath ballPath = ArenaModel.predictBallPath(input, input.time, planHorizon);
        DistancePlot distancePlot = AccelerationModel.simulateAcceleration(car, planHorizon, car.boost);

        Optional<Intercept> interceptStepOffering = InterceptStep.getSoonestIntercept(input.getMyCarData(), ballPath, distancePlot, new Vector3());
        LocalDateTime ourExpectedContactTime = interceptStepOffering.map(Intercept::getTime).orElse(ballPath.getEndpoint().getTime());

        if (situation.ownGoalFutureProximity > 100) {
            return makePlanWithPlentyOfTime(input, situation, ballPath);
        }

        double raceResult = TimeUtil.secondsBetween(ourExpectedContactTime, situation.expectedEnemyContact.time);

        if (raceResult > 2) {
            // We can take our sweet time. Now figure out whether we want a directed kick, a dribble, an intercept, a catch, etc
            return makePlanWithPlentyOfTime(input, situation, ballPath);
        }

        if (raceResult > .5) {
            return new Plan(Plan.Posture.OFFENSIVE).withStep(new IdealDirectedHitStep(new KickAtEnemyGoal(), input));
        }

        if (raceResult > -.5) {

            if (!interceptStepOffering.isPresent()) {
                // Nobody is getting to the ball any time soon.
                return makePlanWithPlentyOfTime(input, situation, ballPath);
            }

            if (situation.enemyOffensiveApproachError < Math.PI / 3) {

                // Enemy is threatening us

                if (GetOnOffenseStep.getYAxisWrongSidedness(input) < 0) {

                    // Consider this to be a 50-50. Go hard for the intercept
                    Vector3 ownGoalCenter = GoalUtil.getOwnGoal(input.team).getCenter();
                    Vector3 interceptPosition = interceptStepOffering.get().getSpace();
                    Vector3 toOwnGoal = ownGoalCenter.minus(interceptPosition);
                    Vector3 interceptModifier = toOwnGoal.normaliseCopy();

                    return new Plan(Plan.Posture.OFFENSIVE).withStep(new InterceptStep(interceptModifier));
                } else {
                    // We're not in a good position to go for a 50-50. Get on defense.
                    return new Plan(Plan.Posture.DEFENSIVE).withStep(new GetOnDefenseStep());
                }
            } else {
                // Doesn't matter if enemy wins the race, they are out of position.
                return makePlanWithPlentyOfTime(input, situation, ballPath);
            }
        }

        // The enemy is probably going to get there first.
        if (situation.enemyOffensiveApproachError < Math.PI / 3 && situation.distanceBallIsBehindUs > -50) {
            // Enemy can probably shoot on goal, so get on defense
            return new Plan(Plan.Posture.DEFENSIVE).withStep(new GetOnDefenseStep());
        } else {
            // Enemy is just gonna hit it for the sake of hitting it, presumably. Let's try to stay on offense if possible.
            // TODO: make sure we don't own-goal it with this
            return new Plan(Plan.Posture.OFFENSIVE).withStep(new GetOnOffenseStep()).withStep(new InterceptStep(new Vector3()));
        }

    }

    private Plan makePlanWithPlentyOfTime(AgentInput input, TacticalSituation situation, BallPath ballPath) {

        CarData car = input.getMyCarData();

        if (situation.distanceFromEnemyBackWall < 20) {
            Optional<SpaceTime> catchOpportunity = SteerUtil.getCatchOpportunity(car, ballPath, car.boost);
            if (catchOpportunity.isPresent()) {
                return new Plan(Plan.Posture.OFFENSIVE).withStep(new CatchBallStep(catchOpportunity.get())).withStep(new DribbleStep());
            }
            return new Plan(Plan.Posture.OFFENSIVE).withStep(new IdealDirectedHitStep(new FunnelTowardEnemyGoal(), input));
        }

        if (DribbleStep.canDribble(input, false) && input.ballVelocity.magnitude() > 15) {
            BotLog.println("Beginning dribble", input.team);
            return new Plan(Plan.Posture.OFFENSIVE).withStep(new DribbleStep());
        }  else if (WallTouchStep.hasWallTouchOpportunity(input, ballPath)) {
            return new Plan(Plan.Posture.OFFENSIVE).withStep(new MountWallStep()).withStep(new WallTouchStep()).withStep(new DescendFromWallStep());
        } else if (DirectedNoseHitStep.canMakeDirectedKick(input, new KickAtEnemyGoal())) {
            return new Plan(Plan.Posture.OFFENSIVE).withStep(new IdealDirectedHitStep(new KickAtEnemyGoal(), input));
        } else if (car.boost < 50) {
            return new Plan().withStep(new GetBoostStep());
        } else if (GetOnOffenseStep.getYAxisWrongSidedness(input) > 0) {
            BotLog.println("Getting behind the ball", input.team);
            return new Plan(Plan.Posture.NEUTRAL).withStep(new GetOnOffenseStep());
        } else {
            return new Plan(Plan.Posture.OFFENSIVE).withStep(new InterceptStep(new Vector3()));
        }
    }

    public TacticalSituation assessSituation(AgentInput input, BallPath ballPath) {

        Optional<SpaceTime> enemyIntercept = getEnemyIntercept(input, ballPath);

        SpaceTimeVelocity futureBallMotion = ballPath.getMotionAt(input.time.plus(TimeUtil.toDuration(LOOKAHEAD_SECONDS))).orElse(ballPath.getEndpoint());

        TacticalSituation situation = new TacticalSituation();
        situation.expectedEnemyContact = enemyIntercept.orElse(ballPath.getEndpoint().toSpaceTime());
        situation.ownGoalFutureProximity = VectorUtil.flatDistance(GoalUtil.getOwnGoal(input.team).getCenter(), futureBallMotion.getSpace());
        situation.distanceBallIsBehindUs = measureOutOfPosition(input);
        situation.enemyOffensiveApproachError = measureEnemyApproachError(input, situation.expectedEnemyContact);
        double enemyGoalY = GoalUtil.getEnemyGoal(input.team).getCenter().y;
        situation.distanceFromEnemyBackWall = Math.abs(enemyGoalY - futureBallMotion.space.y);
        situation.distanceFromEnemyCorner = getDistanceFromEnemyCorner(futureBallMotion, enemyGoalY);

        situation.scoredOnThreat = GoalUtil.predictGoalEvent(GoalUtil.getOwnGoal(input.team), ballPath);
        situation.needsDefensiveClear = GoalUtil.ballLingersInBox(GoalUtil.getOwnGoal(input.team), ballPath);
        situation.shotOnGoalAvailable = GoalUtil.ballLingersInBox(GoalUtil.getEnemyGoal(input.team), ballPath) &&
                input.getMyCarData().position.distance(input.ballPosition) < 80;

        return situation;
    }

    private double getDistanceFromEnemyCorner(SpaceTimeVelocity futureBallMotion, double enemyGoalY) {
        Vector2 positiveCorner = ArenaModel.CORNER_ANGLE_CENTER;
        double goalSign = Math.signum(enemyGoalY);

        Vector2 corner1 = new Vector2(positiveCorner.x, positiveCorner.y * goalSign);
        Vector2 corner2 = new Vector2(-positiveCorner.x, positiveCorner.y * goalSign);

        Vector2 ballFutureFlat = futureBallMotion.space.flatten();

        return Math.min(ballFutureFlat.distance(corner1), ballFutureFlat.distance(corner2));
    }

    private Optional<SpaceTime> getEnemyIntercept(AgentInput input, BallPath ballPath) {
        return input.getEnemyCarData().flatMap(enemyCar -> SteerUtil.getInterceptOpportunityAssumingMaxAccel(enemyCar, ballPath, enemyCar.boost));
    }

    private double measureEnemyApproachError(AgentInput input, SpaceTime enemyContact) {

        Optional<CarData> enemyCarOpt = input.getEnemyCarData();
        if (!enemyCarOpt.isPresent()) {
            return Double.MAX_VALUE;
        }
        CarData enemyCar = input.getEnemyCarData().get();
        Goal myGoal = GoalUtil.getOwnGoal(input.team);
        Vector3 ballToGoal = myGoal.getCenter().minus(enemyContact.space);

        Vector3 carToBall = enemyContact.space.minus(enemyCar.position);

        return Vector2.angle(ballToGoal.flatten(), carToBall.flatten());
    }


    private double measureOutOfPosition(AgentInput input) {
        CarData car = input.getMyCarData();
        Goal myGoal = GoalUtil.getOwnGoal(input.team);
        Vector3 ballToGoal = myGoal.getCenter().minus(input.ballPosition);
        Vector3 carToBall = input.ballPosition.minus(car.position);
        Vector3 wrongSideVector = VectorUtil.project(carToBall, ballToGoal);
        return wrongSideVector.magnitude() * Math.signum(wrongSideVector.dotProduct(ballToGoal));
    }

}
