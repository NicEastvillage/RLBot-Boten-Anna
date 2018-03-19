package botenanna.overlayWindow;

import botenanna.behaviortree.builder.BehaviourTreeBuilder;
import botenanna.math.*;
import rlbot.api.GameData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;

/** Debug/Status window. Displays a window that shows the stats of a car and the ball. */
public class StatusWindow extends JFrame {

    private BehaviourTreeBuilder btBuilder;

    private JLabel carLocation;
    private JLabel carVelocity;
    private JLabel carRotation;
    private JLabel ballLocation;
    private JLabel ballVelocity;
    private JLabel angleToBall;
    private JButton buttonPlay0;
    private JButton buttonPlay1;
    private JButton buttonPlay2;
    private JButton buttonPlay3;

    private int selectedPlayer = 0;

    //Finding the screen size and converting  the doubles to int.
    private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private Double h = screenSize.getHeight();
    private Double w = screenSize.getWidth();
    private int fontsize = (h.intValue()/5)/15;
    private int width = w.intValue()/4;
    //Setting font size based on screen size.
    private Font font = new Font("Courier New", Font.PLAIN , fontsize);

    /** Creating the window adding content/layout */
    public StatusWindow(){

        // Pick a file for building behaviour trees
        btBuilder = new BehaviourTreeBuilder(this);
        btBuilder.setFileWithChooser();

        //Creating window frame
        JFrame frame = new JFrame("Status StatusWindow"); //Creating the frame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setAlwaysOnTop(true);
        frame.setResizable(false);

        //Setting up content pane (adding stuff)
        int numberOfLines = addComponentsToPane(frame.getContentPane());

        //Display the window
        frame.pack();
        frame.setSize(width,fontsize*2*numberOfLines);
        frame.setVisible(true);
     }

    /** Adds components/content to to the frame */
    private int addComponentsToPane(final Container pane){
        //Creating panels, one for each label (needed for pretty layout)
        int lineCounter = 2;
        JPanel topBar = new JPanel(); lineCounter++;
        JPanel line1 = new JPanel();  lineCounter++;
        JPanel line2 = new JPanel();  lineCounter++;
        JPanel line3 = new JPanel();  lineCounter++;
        JPanel line4 = new JPanel();  lineCounter++;
        JPanel line5 = new JPanel();  lineCounter++;
        JPanel line6 = new JPanel();  lineCounter++;

        //Setting layout and alignment to left
        topBar.setLayout(new FlowLayout(FlowLayout.LEFT));
        line1.setLayout(new FlowLayout(FlowLayout.LEFT));
        line2.setLayout(new FlowLayout(FlowLayout.LEFT));
        line3.setLayout(new FlowLayout(FlowLayout.LEFT));
        line4.setLayout(new FlowLayout(FlowLayout.LEFT));
        line5.setLayout(new FlowLayout(FlowLayout.LEFT));
        line6.setLayout(new FlowLayout(FlowLayout.LEFT));

        //Adding default content to labels
        carLocation = new JLabel("Car Location(x, y, z)");
        carVelocity = new JLabel("Car Velocity(x, y, z)");
        carRotation = new JLabel("Car Rotation(P, Y, R)");
        ballLocation = new JLabel("Ball Location(x, y, z)");
        ballVelocity = new JLabel("Ball Velocity(x, y, z)");
        angleToBall = new JLabel("Angle to ball()");

        //Creating buttons
        buttonPlay0 = new JButton("Player 1");
        buttonPlay1 = new JButton("Player 2");
        buttonPlay2 = new JButton("Player 3");
        buttonPlay3 = new JButton("Player 4");

        //Adding functions to the buttons
        buttonPlay0.addActionListener(e -> StatusWindow.this.selectedPlayer = 0);
        buttonPlay1.addActionListener(e -> StatusWindow.this.selectedPlayer = 1);
        buttonPlay2.addActionListener(e -> StatusWindow.this.selectedPlayer = 2);
        buttonPlay3.addActionListener(e -> StatusWindow.this.selectedPlayer = 3);

        //Setting the font (monospaced)
        buttonPlay0.setFont(font);
        buttonPlay1.setFont(font);
        buttonPlay2.setFont(font);
        buttonPlay3.setFont(font);
        carLocation.setFont(font);
        carVelocity.setFont(font);
        carRotation.setFont(font);
        ballLocation.setFont(font);
        ballVelocity.setFont(font);
        angleToBall.setFont(font);
        //Adding the labels and buttons to the panels
        topBar.add(buttonPlay0);
        topBar.add(buttonPlay1);
        topBar.add(buttonPlay2);
        topBar.add(buttonPlay3);
        line1.add(carLocation);
        line2.add(carVelocity);
        line3.add(carRotation);
        line4.add(ballLocation);
        line5.add(ballVelocity);
        line6.add(angleToBall);

        //Creating main panel and adding label panels (stack on y-axis)
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS)); //Stack on Y_axis, aka stack
        mainPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(topBar);
        mainPanel.add(line1);
        mainPanel.add(line2);
        mainPanel.add(line3);
        mainPanel.add(line4);
        mainPanel.add(line5);
        mainPanel.add(line6);

        //Adding main panel to frame
        pane.add(mainPanel);
        return lineCounter;
    }

    /** Updates the labels with the given game data.
     * @param gameDataPacket a packet with information about the current output of the game. */
    public void updateData(GameData.GameTickPacket gameDataPacket){

        if(selectedPlayer < gameDataPacket.getPlayersCount()){
            //Car info:
            GameData.PlayerInfo car = gameDataPacket.getPlayers(selectedPlayer); //Gets the car info
            Vector3 carLocationVector = Vector3.convert(car.getLocation()); //Gets car location vector
            updateLabelCarLocation(carLocationVector);//Updating car location label
            Vector3 carVelocityVector = Vector3.convert(car.getVelocity()); //Gets car velocity vector
            updateLabelCarVelocity(carVelocityVector); //Update car velocity label
            Vector3 carRotationVector = Vector3.convert(car.getRotation()); //Gets car rotation vector
            updateLabelCarRotation(carRotationVector); //Update car rotation label

            //Ball info:
            GameData.BallInfo ball = gameDataPacket.getBall(); //Gets ball info
            Vector3 ballLocationVector = Vector3.convert(ball.getLocation()); //Gets ball location vector
            updateLabelBallLocation(ballLocationVector); //Updating ball location label
            Vector3 ballVelocityVector = Vector3.convert(ball.getVelocity()); //Gets ball velocity vector
            updateLabelBallVelocity(ballVelocityVector); //Update ball velocity label

            //Angle between car and ball:
            updateLabelAngleToBall(RLMath.carsAngleToPoint(new Vector2(carLocationVector), carRotationVector.yaw, new Vector2(ballLocationVector)));
        }else{
            updateLabelCarLocation(new Vector3(0,0,0));
            updateLabelCarVelocity(new Vector3(0,0,0));
            updateLabelCarRotation(new Vector3(0,0,0));
            updateLabelBallLocation(new Vector3(0,0,0));
            updateLabelBallVelocity(new Vector3(0,0,0));
            updateLabelAngleToBall(0.0);
        }
    }

    /** Updates the car location label with a formatted string.
     * @param playerVecLocation a 3D-vector containing coordinates for the players location. */
    public void updateLabelCarLocation(Vector3 playerVecLocation){
        carLocation.setText(formatLabelVectorXYZ("Car Location", playerVecLocation));
    }

    /** Updates the car velocity label with formatted string.
     * @param playerVecVelocity a 3D-vector containing the velocity of the player. */
    public void updateLabelCarVelocity(Vector3 playerVecVelocity){
        carVelocity.setText(formatLabelVectorXYZ("Car Velocity", playerVecVelocity));

    }

    /** Updates the car rotation label with formatted string.
     * @param playerRotationVector a 3D-vector containing the rotation of the player. */
    public void updateLabelCarRotation(Vector3 playerRotationVector) {
        carRotation.setText(formatLabelVectorRotation("Car Rotation", playerRotationVector));
    }

    /** Updates the ball location label with a formatted string.
     * @param ballVecLocation a 3D-vector containing the coordinates of the ball. */
    public void updateLabelBallLocation(Vector3 ballVecLocation){
        ballLocation.setText(formatLabelVectorXYZ("Ball Location", ballVecLocation));
    }

    /** Updates the ball velocity label with a formatted string.
     * @param ballVecVelocity a 3D-vector containing the velocity of the ball. */
    public void updateLabelBallVelocity(Vector3 ballVecVelocity){
        ballVelocity.setText(formatLabelVectorXYZ("Ball Velocity", ballVecVelocity));
    }

    /** Updates the angle to ball label with a formatted string.
     * @param angle the angle between the ball and car. */
    public void updateLabelAngleToBall(double angle){
        angleToBall.setText(formatLabelSingleValue("Angle", angle));
    }

    /** Formats a string to be used in a label.
     * @param labelName a string used to describe the value.
     * @param value a value to be used in the string.
     * @return a formatted string used in a label. */
    private String formatLabelSingleValue(String labelName, double value){
        return labelName + String.format(" (% 4.2f)", value);
    }

    /** Formats a string to be used in a label.
     * @param labelName a string used to describe the vector.
     * @param vector a 3D-vector.
     * @return a formatted string used in a label. */
    private String formatLabelVectorXYZ(String labelName, Vector3 vector){
        return labelName + String.format(" (x:% 8.2f, y:% 8.2f, z:% 8.2f)", vector.x, vector.y, vector.z);
    }

    /** Formats a string used in a label to describe rotation (pitch, yaw and roll).
     * @param labelName a string used to describe the vector.
     * @param vector a 3D-vector vector describing pitch, yaw and roll.
     * @return a formatted string used in labels describing rotation. */
    private String formatLabelVectorRotation(String labelName, Vector3 vector){
        return labelName + String.format(" (P:% 8.2f, Y:% 8.2f, R:% 8.2f)", vector.pitch, vector.yaw, vector.roll);
    }

    /** Get the behaviour tree builder. */
    public BehaviourTreeBuilder getBtBuilder() {
        return btBuilder;
    }
}
