package ui;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Bovink on 2016/5/15 0015.
 */
public class OsuMasterUI {

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                OsuMasterFrame frame = new OsuMasterFrame();

                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.pack();
                frame.setVisible(true);

            }
        });

    }
}
