package tarehart.rlbot.tuning;

import com.google.gson.Gson;
import tarehart.rlbot.math.SpaceTimeVelocity;
import tarehart.rlbot.physics.BallPath;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class BallRecorder {

    public static final String DIRECTORY = "ballpath";

    // This is going to be an actual ballpath, not predicted.
    private static BallPath ballPath;
    private static LocalDateTime endTime;
    private static Gson gson = new Gson();

    public static void startRecording(SpaceTimeVelocity startPoint, LocalDateTime endTime) {

        if (ballPath == null) {
            ballPath = new BallPath(startPoint);
            BallRecorder.endTime = endTime;
        }
    }

    public static void recordPosition(SpaceTimeVelocity ballPosition) {
        if (ballPath != null) {

            if (ballPosition.getTime().isAfter(endTime)) {
                // Write to a file
                Path path = Paths.get("./" + DIRECTORY + "/" + endTime.atOffset(ZoneOffset.UTC).toEpochSecond() + ".json");
                try {
                    Files.write(path, gson.toJson(ballPath).getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ballPath = null;
            } else {
                ballPath.addSlice(ballPosition);
            }
        }
    }

}
