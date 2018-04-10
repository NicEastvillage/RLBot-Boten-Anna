package botenanna.behaviortree.builder;

import botenanna.behaviortree.Node;
import botenanna.behaviortree.guards.*;
import botenanna.behaviortree.tasks.*;
import botenanna.behaviortree.composites.*;
import botenanna.behaviortree.decorators.*;

public class NodeLibrary {
    public static Node nodeFromString(String nodeName, String[] arguments) {
        if (nodeName.length() >= 4 && nodeName.substring(0, 4).equals("Task")) {
            // Tasks
            switch (nodeName) {
                case "TaskGoForwards": return new TaskGoForwards(arguments);
                case "TaskGoTowardsPoint": return new TaskGoTowardsPoint(arguments);
                case "TaskDashForward": return new TaskDashForward(arguments);
                case "TaskAdjustAirRotation": return new TaskAdjustAirRotation(arguments);
                case "TaskHitTowardsPoint": return new TaskHitTowardsPoint(arguments);
                case "TaskBallTowardsGoal": return new TaskBallTowardsGoal(arguments);
            }
        } else if (nodeName.length() >= 5 && nodeName.substring(0, 5).equals("Guard")) {
            // Guards
            switch (nodeName) {
                case "GuardHasBoost": return new GuardHasBoost(arguments);
                case "GuardIsBallOnMyHalf": return new GuardIsBallOnMyHalf(arguments);
                case "GuardIsDistanceLessThan": return new GuardIsDistanceLessThan(arguments);
                case "GuardIsDoubleLessThan": return new GuardIsDoubleLessThan(arguments);
                case "GuardIsKickoff": return new GuardIsKickoff(arguments);
                case "GuardIsMidAir": return new GuardIsMidAir(arguments);
                case "GuardIntercept": return new GuardIntercept(arguments);
                case "GuardCloserThan": return  new GuardCloserThan(arguments);
                case "GuardHasGoalOpportunity": return new GuardHasGoalOpportunity(arguments);
                case "GuardIsPointBehind": return new GuardIsPointBehind(arguments);
                case "GuardHasBallPossession": return new GuardHasBallPossession(arguments);
            }
        } else {
            // Other nodes
            switch (nodeName) {
                case "Selector": return new Selector();
                case "Sequencer": return new Sequencer();
                case "Inverter": return new Inverter();
            }
        }
        throw new BehaviourTreeUnknownNodeException("Could not recognize the node \"" + nodeName + "\".");
    }
}
