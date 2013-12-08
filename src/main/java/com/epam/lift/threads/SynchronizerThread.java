package com.epam.lift.threads;

import javax.swing.*;

public class SynchronizerThread extends Thread{

    public static boolean stop;
    public static int animationSpeed;

    private JPanel jPanel;

    public SynchronizerThread(JPanel jPanel) {
        this.jPanel = jPanel;
    }

    @Override
    public void run() {

    }

    public void start(){
        this.run();
    }
}
