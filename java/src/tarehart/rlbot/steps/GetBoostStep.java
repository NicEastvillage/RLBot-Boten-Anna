package tarehart.rlbot.steps;

import tarehart.rlbot.math.vector.Vector2;
import tarehart.rlbot.math.vector.Vector3;
import tarehart.rlbot.AgentInput;
import tarehart.rlbot.AgentOutput;
import tarehart.rlbot.input.CarData;
import tarehart.rlbot.input.FullBoost;
import tarehart.rlbot.math.TimeUtil;
import tarehart.rlbot.math.VectorUtil;
import tarehart.rlbot.physics.ArenaModel;
import tarehart.rlbot.physics.BallPath;
import tarehart.rlbot.physics.DistancePlot;
import tarehart.rlbot.planning.*;
import tarehart.rlbot.tuning.BotLog;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

public class GetBoostStep implements Step {
    private FullBoost targetLocation = null;

    private Plan plan;

    public Optional<AgentOutput> getOutput(AgentInput input) {

        if (targetLocation == null) {
            init(input);
        }

        Optional<FullBoost> matchingBoost = input.fullBoosts.stream().filter(b -> b.location.distance(targetLocation.location) < 1).findFirst();
        if (!matchingBoost.isPresent()) {
            return Optional.empty();
        }

        targetLocation = matchingBoost.get();

        if (!targetLocation.isActive) {
            return Optional.empty();
        }

        CarData car = input.getMyCarData();

        double distance = SteerUtil.getDistanceFromCar(car, targetLocation.location);

        if (plan != null && !plan.isComplete()) {
            Optional<AgentOutput> output = plan.getOutput(input);
            if (output.isPresent()) {
                return output;
            }
        }

        if (distance < 3) {
            return Optional.empty();
        } else {

            CarData carData = input.getMyCarData();
            Vector2 myPosition = carData.position.flatten();
            Vector3 target = targetLocation.location;
            Vector2 toBoost = target.flatten().minus(myPosition);



            DistancePlot distancePlot = AccelerationModel.simulateAcceleration(car, Duration.ofSeconds(4), car.boost);
            Vector2 facing = VectorUtil.orthogonal(target.flatten(), v -> v.dotProduct(toBoost) > 0).normaliseCopy();

            SteerPlan planForCircleTurn = SteerUtil.getPlanForCircleTurn(car, distancePlot, target.flatten(), facing);

            Optional<Plan> sensibleFlip = SteerUtil.getSensibleFlip(car, planForCircleTurn.waypoint);
            if (sensibleFlip.isPresent()) {
                BotLog.println("Flipping toward boost", input.team);
                plan = sensibleFlip.get();
                plan.begin();
                return plan.getOutput(input);
            }

            return Optional.of(planForCircleTurn.immediateSteer);
        }
    }

    private void init(AgentInput input) {
        targetLocation = getTacticalBoostLocation(input);
    }

    private static FullBoost getTacticalBoostLocation(AgentInput input) {
        FullBoost nearestLocation = null;
        double minTime = Double.MAX_VALUE;
        CarData carData = input.getMyCarData();
        DistancePlot distancePlot = AccelerationModel.simulateAcceleration(carData, Duration.ofSeconds(4), carData.boost);
        for (FullBoost boost: input.fullBoosts) {
            Optional<Double> travelSeconds = AccelerationModel.getTravelSeconds(carData, distancePlot, boost.location);
            if (travelSeconds.isPresent() && travelSeconds.get() < minTime &&
                    (boost.isActive || travelSeconds.get() - TimeUtil.secondsBetween(input.time, boost.activeTime) > 1)) {

                minTime = travelSeconds.get();
                nearestLocation = boost;
            }
        }
        if (minTime < 1.5) {
            return nearestLocation;
        }

        BallPath ballPath = ArenaModel.predictBallPath(input, input.time, Duration.ofSeconds(4));
        Vector3 endpoint = ballPath.getEndpoint().getSpace();
        // Add a defensive bias.
        Vector3 idealPlaceToGetBoost = new Vector3(endpoint.x, 40 * Math.signum(GoalUtil.getOwnGoal(input.team).getCenter().y), 0);
        return getNearestBoost(input.fullBoosts, idealPlaceToGetBoost);
    }

    private static FullBoost getNearestBoost(List<FullBoost> boosts, Vector3 position) {
        FullBoost location = null;
        double minDistance = Double.MAX_VALUE;
        for (FullBoost boost: boosts) {
            if (boost.isActive) {
                double distance = position.distance(boost.location);
                if (distance < minDistance) {
                    minDistance = distance;
                    location = boost;
                }
            }
        }
        return location;
    }


    @Override
    public boolean isBlindlyComplete() {
        return false;
    }

    @Override
    public void begin() {
    }

    public static boolean seesOpportunisticBoost(CarData carData, List<FullBoost> boosts) {
        FullBoost boost = getNearestBoost(boosts, carData.position);
        return boost.location.distance(carData.position) < 20 &&
                Math.abs(SteerUtil.getCorrectionAngleRad(carData, boost.location)) < Math.PI / 6;

    }

    @Override
    public boolean canInterrupt() {
        return plan == null || plan.canInterrupt();
    }

    @Override
    public String getSituation() {
        return "Going for boost";
    }
}
