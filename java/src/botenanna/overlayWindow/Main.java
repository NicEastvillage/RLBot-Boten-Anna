package botenanna.overlayWindow;

import botenanna.math.Vector3;
import rlbot.api.GameData;

import javax.swing.*;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {

        Window statusWindow = new Window();

        System.out.println("WHUT");

        Vector3 ventorsGamble = new Vector3(1.1232145096, 2.98519509, 3.1239812512);

        while(ventorsGamble.x < 1000000){
            ventorsGamble = ventorsGamble.scale(1000);
            statusWindow.updateLabelCarLocation(ventorsGamble);
            statusWindow.updateLabelCarVelocity(ventorsGamble);
            statusWindow.updateLabelCarRotation(ventorsGamble);
            statusWindow.updateLabelBallLocation(ventorsGamble);
            statusWindow.updateLabelBallVelocity(ventorsGamble);

            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            ventorsGamble = ventorsGamble.scale(0.001);

            statusWindow.updateLabelCarLocation(ventorsGamble);
            statusWindow.updateLabelCarVelocity(ventorsGamble);
            statusWindow.updateLabelCarRotation(ventorsGamble);
            statusWindow.updateLabelBallLocation(ventorsGamble);
            statusWindow.updateLabelBallVelocity(ventorsGamble);

            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }
}
