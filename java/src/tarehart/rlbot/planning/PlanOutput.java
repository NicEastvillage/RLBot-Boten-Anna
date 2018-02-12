package tarehart.rlbot.planning;

import tarehart.rlbot.AgentOutput;
import tarehart.rlbot.tuning.BallTelemetry;

public class PlanOutput {
    private AgentOutput agentOutput;
    private BallTelemetry telemetry;

    public PlanOutput withAgentOutput(AgentOutput agentOutput) {
        this.agentOutput = agentOutput;
        return this;
    }

    public PlanOutput withTelemetry(BallTelemetry telemetry) {
        this.telemetry = telemetry;
        return this;
    }

    public AgentOutput getAgentOutput() {
        return agentOutput;
    }
}
