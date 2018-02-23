package botenanna.behaviortree;

public interface Task {
    Status getStatus();
    void reset();
    Action run();
    void addChild(Task child) throws BehaviourTreeBuildingException ;
}
