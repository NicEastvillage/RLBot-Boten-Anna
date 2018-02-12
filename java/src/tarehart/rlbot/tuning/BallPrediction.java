package tarehart.rlbot.tuning;

import tarehart.rlbot.math.vector.Vector3;

import java.time.LocalDateTime;

public class BallPrediction {

    public LocalDateTime predictedMoment;
    public Vector3 predictedLocation;

    public BallPrediction(Vector3 predictedLocation, LocalDateTime predictedMoment) {
        this.predictedLocation = predictedLocation;
        this.predictedMoment = predictedMoment;
    }

}
