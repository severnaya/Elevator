package com.epam.lift.newprogect;

import org.apache.log4j.Logger;

public class TransportationTask implements Runnable {
    private static Logger LOG = Logger.getLogger(TransportationTask.class);

    private Passenger passenger;
    protected Controller controller;
    private int locationStory;
    private boolean direction;
    public Thread thread;

    public TransportationTask(Passenger passenger) {
        thread = new Thread(this, String.valueOf(passenger.getPassengerId()));
        this.passenger = passenger;
        controller = passenger.controller;
        passenger.setTransportationState(Passenger.IN_PROGRESS);
        locationStory = passenger.getLocationStory();
        if (locationStory < passenger.getDestinationStory()) {
            direction = true;
        } else {
            direction = false;
        }
        thread.start();
        LOG.info("Passenger = " + passenger.getPassengerId() + " STARTED");
    }

    @Override
    public void run() {
        synchronized (controller.getDispatchContainers().get(locationStory - 1).getPassengers()) {
            synchronized (controller) {
                controller.notify();
            }
            try {
                LOG.info("Wait passengers on locationStory = " + locationStory);
                controller.getDispatchContainers().get(locationStory - 1).getPassengers().wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            while (!controller.entryElevator(passenger)) {
                synchronized (controller.getDispatchContainers().get(locationStory - 1).getPassengers()) {
                    wakeControllerAfterLastPassenger();
                    controller.getDispatchContainers().get(locationStory - 1).getPassengers().wait();
                }
            }
            synchronized (controller.getElevatorContainer()) {
                wakeControllerAfterLastPassenger();
                controller.getElevatorContainer().wait();
            }
            while (!controller.exitElevator(passenger)) {
                synchronized (controller.getElevatorContainer()) {
                    wakeControllerAfterLastPassenger();
                    controller.getElevatorContainer().wait();
                }
            }
            wakeControllerAfterLastPassenger();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        passenger.setTransportationState(Passenger.COMPLETED);
        LOG.info("Passenger = " + passenger.getPassengerId() + " FINISHED");
    }

    private void wakeControllerAfterLastPassenger() {
        synchronized (controller) {
            if (controller.isLastPassenger()) {
                controller.notify();
            }
        }
    }

    public void stopThread(){
        if (!thread.isInterrupted()){
            thread.interrupt();
        }
    }

    public boolean isDirection() {
        return direction;
    }

    public int getPassengerId() {
        return passenger.getPassengerId();
    }
}
