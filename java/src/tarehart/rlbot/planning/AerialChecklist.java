package tarehart.rlbot.planning;

public class AerialChecklist extends LaunchChecklist {
    public boolean notSkidding;
    public boolean hasBoost;

    public boolean readyToLaunch() {
        return hasBoost && notSkidding && super.readyToLaunch();
    }
}
