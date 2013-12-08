package com.epam.lift.newprogect;

public class ElevatorManager {

    public static Elevator elevator;

    public static void initialization(Config config) {
        if (elevator == null) {
            elevator = new Elevator(config);
        } else {
            elevator.setConfig(config);
            elevator.initializationContainers();
        }
    }

    public static void start() {
    //    elevator.start();
    }

    public static void stop() {

    }
}
