package botenanna.behaviortree.builder;

public class BehaviourTreeUnknownNodeException extends RuntimeException {

    public BehaviourTreeUnknownNodeException() {
    }

    public BehaviourTreeUnknownNodeException(String message) {
        super(message);
    }
}
