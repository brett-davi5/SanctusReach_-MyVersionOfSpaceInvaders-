package com.company;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.EventQueue;
import javax.swing.JFrame;

/**
 * Created by Family on 5/25/2017.
 */
public class SanctusReachMain extends JFrame implements Commons {

    public SanctusReachMain(){

        initUI();
    }

    private void initUI(){

        add(new Board());
        setTitle("Battle for Sanctus Reach");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(BOARD_WIDTH, BOARD_HEIGHT);
        setLocationRelativeTo(null);
        setResizable(false);

    }

    public static void main(String[] args){

        EventQueue.invokeLater(() -> {
           SanctusReachMain ex = new SanctusReachMain();
           ex.setVisible(true);
        });

    }
}
