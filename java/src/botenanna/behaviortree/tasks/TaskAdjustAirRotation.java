package botenanna.behaviortree.tasks;

import botenanna.AgentInput;
import botenanna.AgentOutput;
import botenanna.behaviortree.Leaf;
import botenanna.behaviortree.MissingNodeException;
import botenanna.behaviortree.NodeStatus;
import botenanna.behaviortree.Status;
import botenanna.math.RLMath;

public class TaskAdjustAirRotation extends Leaf {

    public TaskAdjustAirRotation(String[] arguments) throws IllegalArgumentException {
        super(arguments);

        // Takes no arguments
        if (arguments.length != 0) throw new IllegalArgumentException();
    }

    @Override
    public void reset() {
        // Irrelevant
    }

    @Override
    public NodeStatus run(AgentInput input) throws MissingNodeException {
        AgentOutput out = new AgentOutput();

        double smoothPitch = RLMath.steeringSmooth(-input.myRotation.pitch / 2);
        out.withPitch(smoothPitch);

        // It is not possible to adjust both roll and yaw at the same time
        // Luckily, if we just want to land on the wheels, we only need to adjust roll

        double smoothRoll = RLMath.steeringSmooth(-input.myRotation.roll / 2);
        out.withRoll(smoothRoll);

        return new NodeStatus(Status.RUNNING, out, this);
    }
}
