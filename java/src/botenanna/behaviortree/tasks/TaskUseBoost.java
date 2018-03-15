package botenanna.behaviortree.tasks;

import botenanna.AgentInput;
import botenanna.AgentOutput;
import botenanna.ArgumentTranslator;
import botenanna.behaviortree.Leaf;
import botenanna.behaviortree.MissingNodeException;
import botenanna.behaviortree.NodeStatus;
import botenanna.behaviortree.Status;

import java.util.function.Function;

public class TaskUseBoost extends Leaf{

    private int requestAmountOfBoost;
    private Function<AgentInput, Object> pointFunc;
    private int boostLeft;
    private boolean completed;

    /**
     *
     *  0 = all
     *  <p> It's signature is {@code TaskUseBoost <requestAmountOfBoost:int> <to:Vector3>}</p> */
    public TaskUseBoost(String[] arguments) throws IllegalArgumentException {
        super(arguments);

        if(arguments.length == 0 || arguments.length > 2)
            throw new IllegalArgumentException();

        completed = false;

        requestAmountOfBoost = Integer.parseInt(arguments[0]);

        if(arguments.length == 2)
            pointFunc = ArgumentTranslator.get(arguments[1]);
    }



    @Override
    public void reset() {
        completed = true;
    }

    @Override
    public NodeStatus run(AgentInput input) throws MissingNodeException {
        if(completed == true){ //How much boost should be left when done?
            if(requestAmountOfBoost == 0)
                boostLeft = input.myBoost;
            else
                boostLeft = input.myBoost - requestAmountOfBoost;

            completed = false;
        }

        if(input.myBoost <= boostLeft) { //Have we used the desired amount of boost
            return NodeStatus.DEFAULT_FAILURE; //Done //TODO: CORRECT?
        }

        if(pointFunc == null){ //Boost without point
            return new NodeStatus(Status.RUNNING, new AgentOutput().withAcceleration(1).withBoost(), this, true);
        } else { //GO TOWARD POINT WITH BOOST

        }

        return NodeStatus.DEFAULT_FAILURE; //TODO: Should be different
    }
}
