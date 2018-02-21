package botenanna.overlayWindow;

//https://docs.oracle.com/javase/tutorial/uiswing/examples/layout/FlowLayoutDemoProject/src/layout/FlowLayoutDemo.java

import botenanna.math.Vector3;
import rlbot.api.GameData;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;

public class Window extends JFrame{

    private static DecimalFormat df2 = new DecimalFormat(".##");
    private static Font font = new Font("Courier New", Font.PLAIN , 12);

    private JLabel carLocation;
    private JLabel carVelocity;
    private JLabel carRotation;
    private JLabel ballLocation;
    private JLabel ballVelocity;
    private JLabel angleToBall;

    public Window(){
        //Creating window frame
        JFrame frame = new JFrame("Status Window"); //Creating the frame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setAlwaysOnTop(true);

        frame.setResizable(false);

        //Setting up content pane (adding stuff)
        addComponentsToPane(frame.getContentPane());

        //Display window

        frame.pack();
        frame.setSize(440,190);
        frame.setVisible(true);

    }

    //Adds components to the pane
    private void addComponentsToPane(final Container pane){
        JPanel line1 = new JPanel();
        JPanel line2 = new JPanel();
        JPanel line3 = new JPanel();
        JPanel line4 = new JPanel();
        JPanel line5 = new JPanel();
        JPanel line6 = new JPanel();

        line1.setLayout(new FlowLayout(FlowLayout.LEFT));
        line2.setLayout(new FlowLayout(FlowLayout.LEFT));
        line3.setLayout(new FlowLayout(FlowLayout.LEFT));
        line4.setLayout(new FlowLayout(FlowLayout.LEFT));
        line5.setLayout(new FlowLayout(FlowLayout.LEFT));
        line6.setLayout(new FlowLayout(FlowLayout.LEFT));

        carLocation = new JLabel("Car Location(x, y, z)");
        carVelocity = new JLabel("Car Velocity(x, y, z)");
        carRotation = new JLabel("Car Rotation(Pitch, Yaw, Roll)");
        ballLocation = new JLabel("Ball Location(x, y, z)");
        ballVelocity = new JLabel("Ball Velocity(x, y, z)");
        angleToBall = new JLabel("Angle to ball()");

        carLocation.setFont(font);
        carVelocity.setFont(font);
        carRotation.setFont(font);
        ballLocation.setFont(font);
        ballVelocity.setFont(font);
        angleToBall.setFont(font);

        line1.add(carLocation);
        line2.add(carVelocity);
        line3.add(carRotation);
        line4.add(ballLocation);
        line5.add(ballVelocity);
        line6.add(angleToBall);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS)); //Stack on Y_axis, aka stack
        mainPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(line1);
        mainPanel.add(line2);
        mainPanel.add(line3);
        mainPanel.add(line4);
        mainPanel.add(line5);
        mainPanel.add(line6);

        pane.add(mainPanel);
    }

    //Updates the labels with data from gamePacket
    public void updateData(GameData.GameTickPacket gameDataPacket){
        //Player info:

        GameData.PlayerInfo player = gameDataPacket.getPlayers(0); //Gets the player info

        //Find player: //TODO: not a good solution
        if(player.getIsBot())
            player = gameDataPacket.getPlayers(1);


        Vector3 playerLocationVector = Vector3.convert(player.getLocation()); //Gets player location vector
        updateLabelCarLocation(playerLocationVector);//Updating car location label
        Vector3 playerVelocityVector = Vector3.convert(player.getVelocity()); //Gets player velocity vector
        updateLabelCarVelocity(playerVelocityVector); //Update car velocity label
        Vector3 playerRotationVector = Vector3.convert(player.getVelocity());
        updateLabelCarRotation(playerRotationVector); //Update car rotation label

        //Ball info:
        GameData.BallInfo ball = gameDataPacket.getBall(); //Gets ball info
        Vector3 ballLocationVector = Vector3.convert(ball.getLocation()); //Gets ball location vector
        updateLabelBallLocation(ballLocationVector); //Updating ball location label
        Vector3 ballVelocityVector = Vector3.convert(ball.getVelocity()); //Gets ball velocity vector
        updateLabelBallVelocity(ballVelocityVector); //Update ball velocity label



    }

    public void updateLabelCarLocation(Vector3 playerVecLocation){
        carLocation.setText(formatLabelVectorXYZ("Car Location", playerVecLocation));
    }

    public void updateLabelCarVelocity(Vector3 playerVecVelocity){
        carVelocity.setText(formatLabelVectorXYZ("Car Velocity", playerVecVelocity));

    }

    public void updateLabelCarRotation(Vector3 playerRotationVector) {
        carRotation.setText(formatLabelVectorRotation("Car Rotation", playerRotationVector));
    }

    public void updateLabelBallLocation(Vector3 ballVecLocation){
        ballLocation.setText(formatLabelVectorXYZ("Ball Location", ballVecLocation));
    }

    public void updateLabelBallVelocity(Vector3 ballVecVelocity){
        ballVelocity.setText(formatLabelVectorXYZ("Ball Velocity", ballVecVelocity));
    }

    public void updateLabelAngleToBall(){

    }

    private String formatLabelVectorXYZ(String labelName, Vector3 vector){
        return labelName + String.format(" (x:% 8.2f, y:% 8.2f, z:% 8.2f)", vector.x, vector.y, vector.z);
    }

    private String formatLabelVectorRotation(String labelName, Vector3 vector){
        return labelName + String.format(" (Pitch:% 8.2f, Yaw:% 8.2f, Roll:% 8.2f)", vector.x, vector.y, vector.z);
    }
}
