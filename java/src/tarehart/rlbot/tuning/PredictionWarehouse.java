package tarehart.rlbot.tuning;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.Optional;

public class PredictionWarehouse {

    private LinkedList<BallPrediction> ballPredictions = new LinkedList<>();

    public Optional<BallPrediction> getPredictionOfMoment(LocalDateTime moment) {
        if (ballPredictions.isEmpty()) {
            return Optional.empty();
        }

        if (moment.isBefore(ballPredictions.getFirst().predictedMoment)) {
            return Optional.empty();
        }


        BallPrediction oldest;
        do {
            if (ballPredictions.isEmpty()) {
                return Optional.empty();
            }

            oldest = ballPredictions.removeFirst();
        } while (moment.isAfter(oldest.predictedMoment));

        return Optional.of(oldest);
    }

    public void addPrediction(BallPrediction prediction) {
        ballPredictions.add(prediction);
    }
}
