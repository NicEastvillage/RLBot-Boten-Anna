package botenanna.behaviortree.builder;

import botenanna.behaviortree.Node;

public class NodeLibrary {
    public static Node nodeFromString(String nodeName, String[] arguments) {
        if (nodeName.substring(0, 4).equals("Task")) {
            // Tasks
            switch (nodeName) {

            }
        } else if (nodeName.substring(0, 5).equals("Guard")) {
            // Guards
            switch (nodeName) {

            }
        } else {
            // Other nodes
            switch (nodeName) {

            }
        }
        throw new BehaviourTreeReadException("Could not create node from \"" + nodeName + "\" to a node.");
    }
}
