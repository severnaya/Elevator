package com.epam.lift;

import org.apache.log4j.Logger;

public class Actions {
    private static Logger LOG = Logger.getLogger(Actions.class);

    public static void startTransportation() {
        LOG.info("STARTING TRANSPORTATION");
    }

    public static void completionTransportation() {
        LOG.info("COMPLETION TRANSPORTATION");
    }

    public static void abortingTransportation() {
        LOG.info("ABORTING TRANSPORTATION");
    }

    public static void movingElevator(int storiesNumber, int currentFloor, boolean direction) {
        if (direction){
            if (storiesNumber > currentFloor){
                LOG.info("MOVING ELEVATOR FROM " + currentFloor + " TO " + (currentFloor + 1));
            }
        }else {
            if (currentFloor > 0){
                LOG.info("MOVING ELEVATOR FROM " + currentFloor + " TO " + (currentFloor - 1));
            }
        }

    }

    public static void currentFloor(int currentFloor){
        LOG.info("Current floor " + currentFloor);
    }

    public static void boardingOfPassenger(int passengerId, int story) {
        LOG.info("BOARDING OF PASSENGER: passengerID = " + passengerId + " story = " + story);
    }

    public static void deboadingOfPassenger(int passengerId, int story) {
        LOG.info("DEBOARDING OF PASSENGER: passengerId = " + passengerId + " story = " + story);
    }

    public static void errorOfValidation(){
        LOG.info("Incorrect entered value!");
    }
}
