package tarehart.rlbot.steps.rotation;

import tarehart.rlbot.math.vector.Vector2;
import tarehart.rlbot.math.vector.Vector3;
import tarehart.rlbot.math.VectorUtil;

import java.time.Duration;

public class RotationUtil {

    public static double maxOrbitHeightAbovePlane(Vector3 axisOfRotation, Vector3 planeNormal) {
        double xVal = axisOfRotation.projectToPlane(planeNormal).magnitude();
        double angleAbovePlane = Math.acos(xVal);
        return Math.cos(angleAbovePlane);
    }

    public static double shortWay(double radians) {

        radians = radians % (Math.PI * 2);
        if (radians > Math.PI) {
            radians -= Math.PI * 2;
        }
        return radians;
    }

}
