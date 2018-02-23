package botenanna.behaviortree;

import botenanna.AgentOutput;
import rlbot.api.GameData;

public class BehaviorTree implements Task {

    private Task topNode;
    private Action lastAction;

    @Override
    public Status getStatus() {
        return Status.RUNNING;
    }

    @Override
    public void reset() {
        lastAction = null;
    }

    @Override
    public Action run() {
        Action newAction = topNode.run();

        if (lastAction != null && (newAction == null || newAction.creator != lastAction.creator)) {
            lastAction.creator.reset();
        }

        if (newAction == null) {
            lastAction = new Action(new AgentOutput(), this);
        } else {
            lastAction = newAction;
        }

        return lastAction;
    }

    @Override
    public void addChild(Task child) throws BehaviourTreeBuildingException {
        if (topNode != null) {
            throw new BehaviourTreeBuildingException();
        }

        topNode = child;
    }
}
