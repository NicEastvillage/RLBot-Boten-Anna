package botenanna.behaviortree.builder;

/** Thrown when something went wrong while building the behaviour tree. */
public class BehaviourTreeBuildingException extends RuntimeException {

    public BehaviourTreeBuildingException() {
    }

    public BehaviourTreeBuildingException(String message) {
        super(message);
    }
}
