package botenanna.behaviortree.tasks;

import botenanna.game.ActionSet;
import botenanna.game.Situation;
import botenanna.behaviortree.Leaf;
import botenanna.behaviortree.MissingNodeException;
import botenanna.behaviortree.NodeStatus;
import botenanna.behaviortree.Status;
import botenanna.physics.TimeLine;


public class TaskDashForward extends Leaf {

    private TimeLine<NodeStatus> timeLine;
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
        timeLine = new TimeLine<>();

        //Setting time stamps
        timeLine.addTimeStamp(0, new NodeStatus(Status.RUNNING, new ActionSet().withJump().withPitch(-1).withThrottle(1), this, true));
        timeLine.addTimeStamp(0.15, new NodeStatus(Status.RUNNING, new ActionSet().withJump(false).withThrottle(1), this, true));
        timeLine.addTimeStamp(0.20, new NodeStatus(Status.RUNNING, new ActionSet().withJump().withPitch(-1).withThrottle(1), this, true));
        timeLine.addTimeStamp(0.30, new NodeStatus(Status.RUNNING, new ActionSet().withJump(false).withThrottle(1), this, true));
        timeLine.addTimeStamp(1, new NodeStatus(Status.RUNNING, new ActionSet().withJump(false).withThrottle(1), this, false));
        timeLine.addTimeStamp(1.35, null);
    }

    @Override
    public void reset() {
        this.currentlyActive = false;
        timeLine.reset();
    }

    @Override
    public NodeStatus run(Situation input) throws MissingNodeException {

        if(currentlyActive == false){
            timeLine.reset();
            currentlyActive = true;
            return new NodeStatus(Status.RUNNING, new ActionSet().withJump(false).withThrottle(1), this, true);
        }

        if (timeLine.evaluate() == null){
            currentlyActive = false;
            return new NodeStatus(Status.RUNNING, new ActionSet().withJump(false).withThrottle(1), this, true);
        }

        return timeLine.evaluate();
    }
}
