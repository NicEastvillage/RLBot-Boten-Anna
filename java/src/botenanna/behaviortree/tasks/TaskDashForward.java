package botenanna.behaviortree.tasks;

import botenanna.AgentInput;
import botenanna.AgentOutput;
import botenanna.behaviortree.Leaf;
import botenanna.behaviortree.MissingNodeException;
import botenanna.behaviortree.NodeStatus;
import botenanna.behaviortree.Status;
import botenanna.physics.SteppedTimeLine;


public class TaskDashForward extends Leaf {

    private SteppedTimeLine<NodeStatus> steppedTimeLine;
    private boolean currentlyActive;

    /** <p>The TaskDashForward makes the car dash forward.</p>
     *  <p>It's signature is non existing </p>*/
    public TaskDashForward(String[] arguments) throws IllegalArgumentException {
        super(arguments);


        if(arguments.length != 0){
            throw new IllegalArgumentException();
        }

        currentlyActive = false;

        //Creating timeline object
        steppedTimeLine = new SteppedTimeLine<>();

        //Setting time stamps
        steppedTimeLine.addTimeStep(0, new NodeStatus(Status.RUNNING, new AgentOutput().withJump().withPitch(-1).withAcceleration(1), this, true));
        steppedTimeLine.addTimeStep(0.15, new NodeStatus(Status.RUNNING, new AgentOutput().withJump(false).withAcceleration(1), this, true));
        steppedTimeLine.addTimeStep(0.20, new NodeStatus(Status.RUNNING, new AgentOutput().withJump().withPitch(-1).withAcceleration(1), this, true));
        steppedTimeLine.addTimeStep(0.30, new NodeStatus(Status.RUNNING, new AgentOutput().withJump(false).withAcceleration(1), this, true));
        steppedTimeLine.addTimeStep(1, new NodeStatus(Status.RUNNING, new AgentOutput().withJump(false).withAcceleration(1), this, false));
        steppedTimeLine.addTimeStep(1.35, null);
    }

    @Override
    public void reset() {
        this.currentlyActive = false;
        steppedTimeLine.reset();
    }

    @Override
    public NodeStatus run(AgentInput input) throws MissingNodeException {

        if(currentlyActive == false){
            steppedTimeLine.reset();
            currentlyActive = true;
            return new NodeStatus(Status.RUNNING, new AgentOutput().withJump(false).withAcceleration(1), this, true);
        }

        if (steppedTimeLine.evaluate() == null){
            currentlyActive = false;
            return new NodeStatus(Status.RUNNING, new AgentOutput().withJump(false).withAcceleration(1), this, true);
        }

        return steppedTimeLine.evaluate();
    }
}
