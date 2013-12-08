package com.epam.lift.newprogect;

import org.apache.log4j.Logger;

import java.util.Random;

public class Passenger {
    private static Logger LOG = Logger.getLogger(Passenger.class);

    public static final int NOT_STARTED = 0;
    public static final int IN_PROGRESS = 1;
    public static final int COMPLETED = 2;
    public static final int ABORTED = 3;
    private static final int SIZE_PASSENGERS_ZERO = 0;

    private int locationStory;
    private int storiesNumber;
    private int destinationStory;
    private int passengerId = this.hashCode();
    private int transportationState = NOT_STARTED;
    protected Controller controller;
    private TransportationTask task;

    public Passenger(Controller controller, int locationStory, int storiesNumber) {
        this.controller = controller;
        this.locationStory = locationStory;
        this.storiesNumber = storiesNumber;

        do {
            destinationStory = (new Random().nextInt(storiesNumber)) + 1;
        } while (destinationStory == locationStory);
    }

    public void runTask(){
        task = new TransportationTask(this);
    }

    public void stopTask(){
        task.stopThread();
        LOG.info("Stop passenger " + getPassengerId());
    }

    public synchronized void extend(){
        this.notify();
    }

    public void setController(Controller controller) {
        this.controller = controller;
        task.controller = controller;
    }

    public int getLocationStory() {
        return locationStory;
    }

    public int getDestinationStory() {
        return destinationStory;
    }

    public int getTransportationState() {
        return transportationState;
    }

    public void setTransportationState(int transportationState) {
        this.transportationState = transportationState;
    }

    public int getPassengerId() {
        return passengerId;
    }

    public TransportationTask getTask() {
        return task;
    }

    public String toString(){
        return "passengerId = " + passengerId + "\t destinationStory = " + destinationStory + "\t locationStory = " + locationStory;
    }
}
