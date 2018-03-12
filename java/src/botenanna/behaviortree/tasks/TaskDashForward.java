package botenanna.behaviortree.tasks;

import botenanna.AgentInput;
import botenanna.AgentOutput;
import botenanna.ArgumentTranslator;
import botenanna.behaviortree.Leaf;
import botenanna.behaviortree.MissingNodeException;
import botenanna.behaviortree.NodeStatus;
import botenanna.behaviortree.Status;
import botenanna.math.RLMath;
import botenanna.math.Vector3;

import java.util.function.Function;

//TODO/improvement: Make use of timeline and make high priority
public class TaskDashForward extends Leaf {

    private boolean currentlyActive;

    /** The TaskDashForward makes the car dash forward.
     *
     *  It's signature is non existing */
    public TaskDashForward(String[] arguments) throws IllegalArgumentException {
        super(arguments);

        currentlyActive = false;

        if(arguments.length != 0){
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void reset() {
        this.currentlyActive = false;
    }

    @Override
    public NodeStatus run(AgentInput input) throws MissingNodeException {

        if(currentlyActive == false){ //Starting the timer
            input.getTimeTracker().startTimer();
            currentlyActive = true;
            return new NodeStatus(Status.RUNNING, new AgentOutput().withJump(false), this);
        }else{
            double timeDif = input.getTimeTracker().getElapsedSecondsTimer();

            if(timeDif > 1){ //DONE, should be on ground again
                this.reset();
                return new NodeStatus(Status.RUNNING, new AgentOutput().withJump(false), this);
            }else if(timeDif > 0.30){ //Reset
                return new NodeStatus(Status.RUNNING, new AgentOutput().withJump(false), this);
            }else if(timeDif > 0.20){ //Second jump
                return new NodeStatus(Status.RUNNING, new AgentOutput().withJump().withPitch(-1), this);
            }else if(timeDif > 0.15){ //Reset
                return new NodeStatus(Status.RUNNING, new AgentOutput().withJump(false), this);
            }else{ //First jump  //TODO: Can the first jump be 0?? When there is a guard before the this.
                return new NodeStatus(Status.RUNNING, new AgentOutput().withJump().withPitch(-1), this);
            }
        }
    }
}
