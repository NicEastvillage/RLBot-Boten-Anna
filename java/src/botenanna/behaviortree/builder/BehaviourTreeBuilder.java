package botenanna.behaviortree.builder;

import botenanna.behaviortree.BehaviorTree;
import botenanna.behaviortree.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class BehaviourTreeBuilder {

    private static final File previousTreeSetupFile = new File(System.getProperty("user.home"), ".botenanna");

    private Stage parent;
    private File defaultFile;

    /** A BehaviourTreeBuilder is an assisting tool for building BehaviourTrees from files. The defaultFile can be specified
     * with a FileChooser. */
    public BehaviourTreeBuilder(Stage parent) {
        this.parent = parent;
    }

    /** Set the defaultFile used to generate a behaviour tree with the buildUsingDefault method. This method will open a defaultFile chooser
     * window, where the user can specify which defaultFile to open. */
    public void setupDefaultFile() throws MissingBehaviourTreeException {
        File previouslyUsedTree = getPreviouslyUsedTreeFile();
        if (previouslyUsedTree != null) {
            Alert defaultConfirmation = new Alert(Alert.AlertType.CONFIRMATION);
            defaultConfirmation.setTitle("Default Behaviour Tree");
            defaultConfirmation.setHeaderText("Would you like to use the same BT file as last time?");
            defaultConfirmation.setContentText("Previously used BT file was:\n" + previouslyUsedTree.toPath());

            ButtonType yesOption = new ButtonType("Yes");
            ButtonType chooseOption = new ButtonType("Choose another");
            ButtonType cancelOption = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            defaultConfirmation.getButtonTypes().setAll(yesOption, chooseOption, cancelOption);

            Optional<ButtonType> result = defaultConfirmation.showAndWait();
            if (result.get() == yesOption) {
                defaultFile = previouslyUsedTree;
            } else if (result.get() == chooseOption) {
                defaultFile = getFileWithFileChooser();
            }
        } else {
            defaultFile = getFileWithFileChooser();
        }

        if (defaultFile != null) {
            saveDefaultFilePath();
        }
    }

    /** Save the path of the default BT file in config file. */
    private void saveDefaultFilePath() {
        if (defaultFile != null) {
            try {
                // Create
                if (!previousTreeSetupFile.exists()) {
                    previousTreeSetupFile.createNewFile();
                }
                // Write
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(previousTreeSetupFile.getAbsoluteFile()))) {
                    writer.write(defaultFile.getAbsolutePath());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /** Check if a config file exists and get BT file from it. */
    private File getPreviouslyUsedTreeFile() {
        if (previousTreeSetupFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(previousTreeSetupFile))) {

                // The file only contains one line which is the path of the tree used last time
                String defaultTreePath = reader.readLine();
                if (defaultTreePath != null) {
                    return new File(defaultTreePath);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /** Choose a behaviour tree with a file chooser window*/
    private File getFileWithFileChooser() {
        FileChooser fc = new FileChooser();
        fc.setTitle("Open Behaviour Tree Source File");
        fc.setInitialDirectory(new File(System.getProperty("user.home"), "documents/"));
        return fc.showOpenDialog(parent);
    }

    /** Build a BehaviourTree from the defaultFile specified with the {@code setupDefaultFile} method. Throws a MissingBehaviourTreeException
     * if the defaultFile has not been specified.
     * @return a BehaviourTree. */
    public BehaviorTree buildUsingDefault() throws MissingBehaviourTreeException, FileNotFoundException, IOException {
        if (defaultFile == null) throw new MissingBehaviourTreeException("Default file not specified.");
        return build(defaultFile);
    }

    /** Build a BehaviourTree from a file chosen with a file chooser */
    public BehaviorTree buildFromFileChooser() throws FileNotFoundException, IOException {
        File file = getFileWithFileChooser();
        if (file == null) return null;
        return build(file);
    }

    /** Build a behaviour tree from a File. */
    public BehaviorTree build(File file) throws FileNotFoundException, IOException {
        return build(file, new HashSet<>());
    }

    /** Build a behaviour tree from a File and a Set of already visited files. The Set will prevent it from creating
     * infinite loops. The defaultFile cannot already be in the Set of visited defaultFile. A BehaviourTreeBuildingException is
     * thrown if it is. */
    public BehaviorTree build(File file, Set<File> visitedFiles) throws FileNotFoundException, IOException {

        // defaultFile cannot already be visited
        if (visitedFiles.contains(file)) {
            throw new BehaviourTreeBuildingException("Infinite loop detected when building behaviour tree. \"" +
                    file.getName() + "\" has already been visited.");
        }

        visitedFiles.add(file);

        // Read defaultFile and construct tree from lines
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            Queue<String> queue = fileToQueue(reader);
            BehaviorTree bt = new BehaviorTree();
            readTreeRecursive(bt, -1, queue, queue.size(), file, visitedFiles);
            return bt;
        }
    }

    /** Recursively read a Queue of lines and construct a tree of Nodes from it. The tree's structure is formed from
     * the indentation of the lines. Will crash the program, if a line is unreadable.
     * @param parent the Node created from previous line. Can be an external node. The tree will become children of this node.
     * @param level the current amount of indentation.
     * @param queue a queue of unread lines.
     * @param lineCount the original size of the queue. Used to determine in which line errors occur.
     * @param file the File being read. Used to find and buildUsingDefault subtrees and constructing error messages.
     * @param visitedFiles a Set of files that has already been visited. Will prevent it from doing infinite loops. */
    // Inspiration: https://stackoverflow.com/questions/6075974/python-file-parsing-build-tree-from-text-file?rq=1
    private void readTreeRecursive(Node parent, int level, Queue<String> queue, int lineCount, File file, Set<File> visitedFiles) throws IOException {
        while (queue.size() > 0) {
            String line = queue.peek();

            // Empty line?
            if (line.length() == 0) {
                throw new BehaviourTreeReadException("Empty line met when reading behaviour tree source defaultFile (" + file.getName() + ", line " + (lineCount - queue.size()) + ").");
            }

            // Break if this line belongs to another parent
            int indent = readIndent(line);
            if (indent <= level) {
                break;
            }

            // Is a child?
            if (indent == level + 1) {
                try {
                    // Node in this line is a child of the parent
                    Node node = translateLineToNode(queue.remove(), file, visitedFiles);
                    parent.addChild(node);
                    // Check if the node has children (recursion!)
                    readTreeRecursive(node, indent, queue, lineCount, file, visitedFiles);
                } catch (BehaviourTreeUnknownNodeException e) {
                    e.printStackTrace();
                } catch (BehaviourTreeChildException e) {
                    throw new BehaviourTreeReadException("Error in source defaultFile. Could not add node to parent (" + file.getName() + ", line " + (lineCount - queue.size()) + ").");
                } catch (BehaviourTreeBuildingException e) {
                    e.printStackTrace();
                }
            } else {
                // Error in indentation
                throw new BehaviourTreeReadException("Wrong indentation in behaviour tree source defaultFile (" + file.getName() + ", line " + (lineCount - queue.size()) + ", indent " + indent + ").");
            }
        }
    }

    /** Construct a behaviour tree subtree from a parent defaultFile and the name of the defaultFile of the subtree. This method
     * expects that the subtree is in the same directory or a subdirectory of the parent defaultFile's directory. If
     * {@code parentFile} is null or {@code subtreeName}'s length is zero, an IllegalArgument is thrown. An IOException
     * is thrown if the subtree defaultFile does not exist. */
    private Node buildSubtree(File parentFile, String subtreeName, Set<File> visitedFiles) throws IOException {
        if (parentFile == null) throw new IllegalArgumentException("Parent defaultFile cannot be null.");
        if (subtreeName.length() == 0) throw new IllegalArgumentException("Length of subtree's name cannot be zero.");

        File subtree = new File(defaultFile.toPath().getParent().toString(), subtreeName);
        if (!subtree.exists()) {
            throw new FileNotFoundException("Could not find subtree \"" + subtreeName + "\".");
        }
        // Infinite loop?
        if (visitedFiles.contains(subtree)) {
            throw new BehaviourTreeBuildingException("Executed building of tree to avoid infinite loop. \"" +
                    parentFile.getName() + "\" contains a tree, that has already been visited.");
        }
        return build(subtree, visitedFiles);
    }

    /** Break a line into parts and create a Node from it. Can create subtrees.
     * @param line a line from a BehaviourTree source defaultFile.
     * @param file the defaultFile being read. Used to find subtrees using relative path. */
    private Node translateLineToNode(String line, File file, Set<File> visitedFiles) throws IOException {

        List<String> parts = Arrays.stream(line.replace("\t", "").split(" ")).filter(s -> s.length() != 0).collect(Collectors.toList());
        List<String> args = parts.size() > 1 ? parts.subList(1, parts.size()) : new ArrayList<>();

        // Check if subtree
        if (parts.get(0).equals("Subtree")) {
            if (args.size() != 1) throw new BehaviourTreeReadException("Wrong number of arguments in behaviour tree source defaultFile. Line: \"" + line + "\".");
            try {
                return buildSubtree(file, args.get(0), visitedFiles);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        // Use NodeLibrary to construct node
        return NodeLibrary.nodeFromString(parts.get(0), args.toArray(new String[0]));
    }

    /** Transform a BufferedReader of Strings to a Queue of Strings. Throws an IOException if reading fails.
     * @param reader a BufferedReader.
     * @return a Queue of Strings with the first line as the first element. */
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

    /** Read the number of tabs (or 4x space) leading a String.
     * @param line a String
     * @return the number of tabs leading the String. */
    private int readIndent(String line) throws BehaviourTreeReadException {
        int TAB_SIZE = 4;
        // Count lines
        int indent = 0;
        for (int i = 0; i < line.length(); i++) {
            if (line.charAt(i) == '\t') {
                indent++;
            } else if (i + TAB_SIZE <= line.length() && line.substring(i, i + TAB_SIZE).equals("    ")) {
                indent++;
                i += TAB_SIZE - 1;
            } else {
                break;
            }
        }
        return indent;
    }
}