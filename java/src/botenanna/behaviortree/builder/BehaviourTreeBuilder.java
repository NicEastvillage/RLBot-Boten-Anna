package botenanna.behaviortree.builder;

import botenanna.behaviortree.BehaviorTree;
import botenanna.behaviortree.Node;
import botenanna.behaviortree.tasks.TaskGoForwards;

import javax.swing.*;
import java.awt.*;
import java.io.*;

public class BehaviourTreeBuilder {

    private Component parent;
    private File file;

    public BehaviourTreeBuilder(Component parent) {
        this.parent = parent;
    }

    /** Set the file used to generate a behaviour tree with the build method. This method will open a file chooser
     * window, where the user can specify which file to open. */
    public void setFileWithChooser() throws MissingBehaviourTreeException {
        // Open file window
        JFileChooser fc = new JFileChooser();
        int status = fc.showOpenDialog(parent);
        if (status == JFileChooser.APPROVE_OPTION) {
            // Build from file
            file = fc.getSelectedFile();
        } else {
            // User did not choose a file or error occurred
            throw new MissingBehaviourTreeException("User did not choose a file or error occurred.");
        }
    }

    /** Build a BehaviourTree from the file specified with the {@code setFileWithChooser} method. Throws a MissingBehaviourTreeException
     * if the file has not been specified.
     * @return a BehaviourTree. */
    public BehaviorTree build() throws MissingBehaviourTreeException, FileNotFoundException, IOException {
        if (file == null) throw new MissingBehaviourTreeException("File not specified yet.");
        return build(file);
    }

    /** Build a BehaviourTree from a filepath. */
    public BehaviorTree build(String path) throws FileNotFoundException, IOException {
        File file = new File(path);
        return build(file);
    }

    /** Build a behaviour tree from a File. */
    public BehaviorTree build(File file) throws FileNotFoundException, IOException {

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            BehaviorTree bt = new BehaviorTree();
            readTreeRecursive(bt, 0, reader);
            return bt;
        }
    }

    // Inspiration: https://stackoverflow.com/questions/6075974/python-file-parsing-build-tree-from-text-file?rq=1
    private Node readTreeRecursive(Node parent, int level, BufferedReader reader) {
        TaskGoForwards tgf = new TaskGoForwards(new String[0]);
        parent.addChild(tgf);
        return tgf;
    }
}