package botenanna.behaviortree.absolute;

import botenanna.Situation;
import botenanna.behaviortree.MissingNodeException;
import botenanna.behaviortree.NodeStatus;
import botenanna.behaviortree.Status;

/** The IfThenElse node is an Absolute Node with 3 children. The children are CONDITION, THEN, ELSE. The IfThenElse
 * node will run the CONDITION child first. If the CONDITION child returns a SUCCESS, the IfThenElse will return
 * the value of the THEN child and the ELSE child won't be called. If the CONDITION child returns FAILURE, the
 * IfThenElse will return the value of the ELSE child instead and the THEN child won't be called. If any of the
 * children returns RUNNING, the running status will be returned. */
public class IfThenElse extends Absolute {

    private static final int IF = 0;
    private static final int THEN = 1;
    private static final int ELSE = 2;

    /** The IfThenElse node is an Absolute Node with 3 children. The children are CONDITION, THEN, ELSE. The IfThenElse
     * node will run the CONDITION child first. If the CONDITION child returns a SUCCESS, the IfThenElse will return
     * the value of the THEN child and the ELSE child won't be called. If the CONDITION child returns FAILURE, the
     * IfThenElse will return the value of the ELSE child instead and the THEN child won't be called. If any of the
     * children returns RUNNING, the running status will be returned. */
    public IfThenElse() {
        super(3);
    }

    @Override
    public void reset() {
        // Irrelevant
    }

    @Override
    public NodeStatus run(Situation input) throws MissingNodeException {

        // Run condition-branch
        NodeStatus condition = child(IF).run(input);

        // If condition status was RUNNING return then-branch else return else-branch
        if (condition.status == Status.RUNNING) {
            return condition;
        } else if (condition.status == Status.SUCCESS){
            return child(THEN).run(input);
        } else {
            return child(ELSE).run(input);
        }
    }
}
