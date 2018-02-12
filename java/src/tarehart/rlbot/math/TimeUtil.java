package tarehart.rlbot.math;

import java.time.Duration;
import java.time.LocalDateTime;

public class TimeUtil {

    public static double secondsBetween(LocalDateTime a, LocalDateTime b) {
        return toSeconds(Duration.between(a, b));
    }

    public static Duration toDuration(double seconds) {
        return Duration.ofMillis((long) (seconds * 1000));
    }

    public static double toSeconds(Duration duration) {
        return duration.toMillis() / 1000.0;
    }
}
