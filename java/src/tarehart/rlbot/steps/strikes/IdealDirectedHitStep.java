package tarehart.rlbot.steps.strikes;

import tarehart.rlbot.AgentInput;
import tarehart.rlbot.AgentOutput;
import tarehart.rlbot.steps.Step;

import java.util.Optional;

public class IdealDirectedHitStep implements Step {

    private final KickStrategy kickStrategy;
    private Step proxyStep;

    public IdealDirectedHitStep(KickStrategy kickStrategy, AgentInput input) {
        this.kickStrategy = kickStrategy;

        DirectedNoseHitStep noseHit = new DirectedNoseHitStep(kickStrategy);

        if (noseHit.getOutput(input).isPresent() && Math.abs(noseHit.getEstimatedAngleOfKickFromApproach()) < Math.PI / 2) {
            proxyStep = noseHit;
        } else {
            proxyStep = new DirectedSideHitStep(kickStrategy);
        }
    }

    @Override
    public Optional<AgentOutput> getOutput(AgentInput input) {
        return proxyStep.getOutput(input);
    }

    @Override
    public boolean isBlindlyComplete() {
        return proxyStep.isBlindlyComplete();
    }

    @Override
    public void begin() {

    }

    @Override
    public String getSituation() {
        return proxyStep.getSituation();
    }

    @Override
    public boolean canInterrupt() {
        return proxyStep.canInterrupt();
    }
}
