package botenanna.behaviortree.guards;

import botenanna.AgentInput;
import botenanna.ArgumentTranslator;
import botenanna.behaviortree.Leaf;
import botenanna.behaviortree.MissingNodeException;
import botenanna.behaviortree.Node;
import botenanna.behaviortree.NodeStatus;

import java.util.function.Function;

public class GuardIsDoubleLessThan extends Leaf {

    private Function<AgentInput, Object> valueFunc;
    private double size;
    private boolean valueIsAbsolute = false;

    /** The GuardIsDoubleLessThan checks if a double value is less than a given size. It can be used to test
     * both times and angles. The checked value can be marked as being absolute, which is commonly used with angles,
     * by setting the isAbsolute argument to true.
     *
     * It's signature is {@code GuardIsDoubleLessThan <value:double> <size:DOUBLE> [isAbsolute:BOOLEAN]}*/
    public GuardIsDoubleLessThan(String[] arguments) throws IllegalArgumentException {
        super(arguments);

        if (arguments.length < 2 || arguments.length > 3) {
            throw new IllegalArgumentException();
        }

        // Set variables
        valueFunc = ArgumentTranslator.get(arguments[0]);
        size = Double.parseDouble(arguments[1]);

        // Optional variables
        if (arguments.length == 3) {
            valueIsAbsolute = Boolean.parseBoolean(arguments[2]);
        }
    }

    @Override
    public void reset() {
        // Irrelevant
    }

    @Override
    public NodeStatus run(AgentInput input) throws MissingNodeException {
        double value = (double) valueFunc.apply(input);

        // Optionally absolute
        if (valueIsAbsolute) {
            value = Math.abs(value);
        }

        if (value < size) return NodeStatus.DEFAULT_SUCCESS;
        else return NodeStatus.DEFAULT_FAILURE;
    }
}
