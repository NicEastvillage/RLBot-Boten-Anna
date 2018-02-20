package botenanna.overlayWindow;

//https://docs.oracle.com/javase/tutorial/uiswing/examples/layout/FlowLayoutDemoProject/src/layout/FlowLayoutDemo.java

import botenanna.math.Vector3;
import rlbot.api.GameData;

import javax.swing.*;
import java.awt.*;

public class Window extends JFrame{

    private JLabel carLocation;
    private JLabel carVelocity;
    private JLabel ballLocation;
    private JLabel ballVelocity;

    //FlowLayout windowLayout = new FlowLayout();
    final String LtoR = "Left to right";

    public Window(){
        //Creating window frame
        JFrame frame = new JFrame("Status Window"); //Creating the frame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        //Setting up content pane (adding stuff)
        addComponentsToPane(frame.getContentPane());

        //Display window
        frame.pack();
        frame.setVisible(true);

    }

    //Adds components to the pane
    private void addComponentsToPane(final Container pane){
        JPanel line1 = new JPanel();
        JPanel line2 = new JPanel();
        JPanel line3 = new JPanel();
        JPanel line4 = new JPanel();

        line1.setLayout(new FlowLayout(FlowLayout.LEFT));
        line2.setLayout(new FlowLayout(FlowLayout.LEFT));
        line3.setLayout(new FlowLayout(FlowLayout.LEFT));
        line4.setLayout(new FlowLayout(FlowLayout.LEFT));

        carLocation = new JLabel("Car Location(x, y, z)");
        carVelocity = new JLabel("Car Velocity(x, y, z)");
        ballLocation = new JLabel("Ball Location(x, y, z)");
        ballVelocity = new JLabel("Ball Velocity(x, y, z)");

        line1.add(carLocation);
        line2.add(carVelocity);
        line3.add(ballLocation);
        line4.add(ballVelocity);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS)); //Stack on Y_axis, aka stack
        mainPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(line1);
        mainPanel.add(line2);
        mainPanel.add(line3);
        mainPanel.add(line4);

        pane.add(mainPanel);
    }

    //Updates the labels with data from gamePacket
    public void updateData(GameData.GameTickPacket gameDataPacket){
        //Player info:
        GameData.PlayerInfo player = gameDataPacket.getPlayers(0); //Gets the player info
        Vector3 playerLocationVector = Vector3.convert(player.getLocation()); //Gets player location vector
        updateLabelCarLocation(playerLocationVector);//Updating car location label
        Vector3 playerVelocityVector = Vector3.convert(player.getVelocity()); //Gets player velocity vector
        updateLabelCarVelocity(playerVelocityVector); //Update car velocity label

        //Ball info:
        GameData.BallInfo ball = gameDataPacket.getBall(); //Gets ball info
        Vector3 ballLocationVector = Vector3.convert(ball.getLocation()); //Gets ball location vector
        updateLabelBallLocation(ballLocationVector); //Updating ball location label
        Vector3 ballVelocityVector = Vector3.convert(ball.getVelocity()); //Gets ball velocity vector
        updateLabelBallVelocity(ballVelocityVector); //Update ball velocity label

    }

    private void updateLabelCarLocation(Vector3 playerVecLocation){
        double playerX, playerY, playerZ;
        playerX = playerVecLocation.x;
        playerY = playerVecLocation.y;
        playerZ = playerVecLocation.z;

        carLocation.setText("Car Location (x: " + playerX + ", y: " + playerY + ", z: " + playerZ + ")");
    }

    private void updateLabelCarVelocity(Vector3 playerVecVelocity){
        double playVelX, playVelY, playVelZ;
        playVelX = playerVecVelocity.x;
        playVelY = playerVecVelocity.y;
        playVelZ = playerVecVelocity.z;

        carVelocity.setText("Car Velocity (x: " + playVelX + ", y: " + playVelY + ", z: " + playVelZ + ")");
    }

    private void updateLabelBallLocation(Vector3 ballVecLocation){
        double ballX, ballY, ballZ;
        ballX = ballVecLocation.x;
        ballY = ballVecLocation.y;
        ballZ = ballVecLocation.z;

        ballLocation.setText("Ball Location (x: " + ballX + ", y: " + ballY + ", z: " + ballZ + ")");
    }

    private void updateLabelBallVelocity(Vector3 ballVecVelocity){
        double ballVelX, ballVelY, ballVelZ;
        ballVelX = ballVecVelocity.x;
        ballVelY = ballVecVelocity.y;
        ballVelZ = ballVecVelocity.z;

        ballVelocity.setText("Ball Velocity (x: " + ballVelX + ", y: " + ballVelY + ", z: " + ballVelZ + ")");
    }
}
