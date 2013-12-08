package com.epam.lift.newprogect;

import com.epam.lift.lift.Constants;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class Config {

    public static final String NAME_FILE = "config.properties";
    public static final String ANIMATION_BOOTS = "animationBoots";
    public static final String PASSENGER_NUMBER = "passengersNumber";
    public static final String STORIES_NUMBER = "storiesNumber";
    public static final String ELEVATOR_CAPACITY = "elevatorCapacity";

    private static Config uniqueInstance;
    private int animationBoots;
    private int passengersNumber;
    private int storiesNumber;
    private int elevatorCapacity;

    private Config() {
        readPropertyFile();
    }

    public static Config getInstance() {
        if (uniqueInstance == null) {
            synchronized (Config.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new Config();
                }
            }
        }
        return uniqueInstance;
    }

    public boolean validationConfig(){
        if (animationBoots < Constants.MIN_ANIMATION_BOOTS_VALUE){
            return false;
        }
        if (passengersNumber < Constants.MIN_PASSENGERS_NUMBER_VALUE){
            return false;
        }
        if (storiesNumber < Constants.MIN_STORIES_NUMBER_VALUE){
            return false;
        }
        if (elevatorCapacity < Constants.MIN_ELEVATOR_CAPACITY_VALUE){
            return false;
        }
        return true;
    }

    private void readPropertyFile() {
        Properties prop = new Properties();
        try {
            prop.load(new FileInputStream(NAME_FILE));
            animationBoots = convert(prop.getProperty(ANIMATION_BOOTS));
            passengersNumber = convert(prop.getProperty(PASSENGER_NUMBER));
            storiesNumber = convert(prop.getProperty(STORIES_NUMBER));
            elevatorCapacity = convert(prop.getProperty(ELEVATOR_CAPACITY));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
   * @number the string to be parsed
   * @return an Integer object holding the value represented by the string argument.
   * */
    private Integer convert(String number) {
        return Integer.valueOf(number);
    }


    public int getElevatorCapacity() {
        return elevatorCapacity;
    }

    public int getStoriesNumber() {
        return storiesNumber;
    }

    public int getPassengersNumber() {
        return passengersNumber;
    }

    public int getAnimationBoots() {
        return animationBoots;
    }

    public void setPassengersNumber(int passengersNumber) {
        this.passengersNumber = passengersNumber;
    }

    public void setStoriesNumber(int storiesNumber) {
        this.storiesNumber = storiesNumber;
    }

    public void setElevatorCapacity(int elevatorCapacity) {
        this.elevatorCapacity = elevatorCapacity;
    }
}
