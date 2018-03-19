package botenanna.behaviortree.builder;

public class MissingBehaviourTreeException extends RuntimeException {

    public MissingBehaviourTreeException() {
    }

    public MissingBehaviourTreeException(String message) {
        super(message);
    }
}
