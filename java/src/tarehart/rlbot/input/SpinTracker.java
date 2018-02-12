package tarehart.rlbot.input;

import tarehart.rlbot.math.vector.Vector3;
import tarehart.rlbot.Bot;
import tarehart.rlbot.math.VectorUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class SpinTracker {

    private Map<Bot.Team, CarOrientation> previousOrientations = new HashMap<>();
    private Map<Bot.Team, CarSpin> spins = new HashMap<>();

    public void readInput(CarOrientation orientation, Bot.Team team, double secondsElapsed) {
        if (secondsElapsed > 0) {
            if (previousOrientations.containsKey(team)) {
                spins.put(team, getCarSpin(previousOrientations.get(team), orientation, secondsElapsed));
            }
            previousOrientations.put(team, orientation);
        }
    }

    private CarSpin getCarSpin(CarOrientation prevData, CarOrientation currData, double secondsElapsed) {

        double rateConversion = 1 / secondsElapsed;

        double pitchAmount = getRotationAmount(currData.noseVector, prevData.roofVector);
        double yawAmount = getRotationAmount(currData.noseVector, prevData.rightVector);
        double rollAmount = getRotationAmount(currData.roofVector, prevData.rightVector);

        return new CarSpin(pitchAmount * rateConversion, yawAmount * rateConversion, rollAmount * rateConversion);
    }

    private double getRotationAmount(Vector3 currentMoving, Vector3 previousOrthogonal) {
        Vector3 projection = VectorUtil.project(currentMoving, previousOrthogonal);
        return Math.asin(projection.magnitude() * Math.signum(projection.dotProduct(previousOrthogonal)));
    }

    public CarSpin getSpin(Bot.Team team) {
        return Optional.ofNullable(spins.get(team)).orElse(new CarSpin(0, 0, 0));
    }
}
