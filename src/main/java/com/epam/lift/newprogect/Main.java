package com.epam.lift.newprogect;

import com.epam.lift.Actions;
import com.epam.lift.newprogect.gui.MainFrame;
import org.apache.log4j.Logger;

public class Main {

    private static final int NOT_SPEED_OF_ANIMATION = 0;

    private static Logger LOG = Logger.getLogger(Main.class);

    public static void main(String[] args) {
        Config config = Config.getInstance();
        ElevatorManager.initialization(config);
        if (config.validationConfig()) {
            if (config.getAnimationBoots() == NOT_SPEED_OF_ANIMATION) {
                ElevatorManager.start();
            } else {
                MainFrame mainFrame = new MainFrame();
                mainFrame.setVisible(true);
            }
        } else {
            Actions.errorOfValidation();
        }
    }
}
