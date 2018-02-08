package com.hcf.floatword;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        JPanel jPanel = new JPanel();
        JLabel wordLabel = new JLabel("rotary");
        wordLabel.setOpaque(true);
        jPanel.add(wordLabel);
        jPanel.setOpaque(false);

        JFrame jFrame = new JFrame();
        jFrame.setContentPane(jPanel);
        jFrame.setUndecorated(true);
        jFrame.setSize(600, 200);
        jFrame.setLocationRelativeTo(null);
        jFrame.setVisible(true);
    }

}
