package botenanna.behaviortree.builder;

import botenanna.behaviortree.BehaviorTree;
import botenanna.behaviortree.Node;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

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
            Queue<String> queue = fileToQueue(reader);
            BehaviorTree bt = new BehaviorTree();
            readTreeRecursive(bt, -1, queue, queue.size());
            return bt;
        }
    }

    // Inspiration: https://stackoverflow.com/questions/6075974/python-file-parsing-build-tree-from-text-file?rq=1
    private void readTreeRecursive(Node parent, int level, Queue<String> queue, int lineCount) throws IOException {
        while (queue.size() > 0) {
            String line = queue.peek();
            int indent = readIndent(line);

            // Break if this line belongs to another parent
            if (indent <= level) {
                break;
            }

            if (indent == level + 1) {
                try {
                    // Node in this line is a child of the parent
                    Node node = translateLineToNode(queue.remove());
                    parent.addChild(node);
                    // Check if the node has children (recursion!)
                    readTreeRecursive(node, indent, queue, lineCount);
                } catch (BehaviourTreeUnknownNodeException e) {
                    e.printStackTrace();
                } catch (BehaviourTreeBuildingException e) {
                    throw new BehaviourTreeReadException("Error occurred in line " + (lineCount - queue.size() + "."));
                }
            } else {
                // Error in indentation
                throw new BehaviourTreeReadException("Wrong indentation in behaviour tree source file.");
            }
        }
    }

    private Node translateLineToNode(String line) {
        List<String> parts = Arrays.stream(line.replace("\t", "").split(" ")).filter(s -> s.length() != 0).collect(Collectors.toList());
        List<String> args = parts.size() > 1 ? parts.subList(1, parts.size()) : new ArrayList<>();
        return NodeLibrary.nodeFromString(parts.get(0), args.toArray(new String[0]));
    }

    private Queue<String> fileToQueue(BufferedReader reader) throws IOException {
        Queue<String> queue = new LinkedList<>();

        // Add lines to queue
        while (true) {
            String line = reader.readLine();
            if (line != null) {
                queue.add(line);
            } else {
                break;
            }
        }

        return queue;
    }

    private int readIndent(String line) throws BehaviourTreeReadException {
        // Validate
        if (line.length() == 0) {
            throw new BehaviourTreeReadException("Empty line met when reading behaviour tree source file.");
        }

        // Count lines
        int indent = 0;
        for (int i = 0; i < line.length(); i++) {
            if (line.charAt(i) == '\t') {
                indent++;
            } else {
                break;
            }
        }
        return indent;
    }
}