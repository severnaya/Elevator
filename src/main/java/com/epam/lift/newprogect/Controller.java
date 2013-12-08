package com.epam.lift.newprogect;

import com.epam.lift.Actions;
import com.epam.lift.gui.ContainerPanel;
import com.epam.lift.newprogect.container.ArrivalStoryContainer;
import com.epam.lift.newprogect.container.DispatchStoryContainer;
import org.apache.log4j.Logger;
import java.util.ArrayList;
import java.util.List;

public class Controller {
    private static final Logger LOG = Logger.getLogger(Controller.class);

    private Config config;
    private List<DispatchStoryContainer> dispatchContainers;
    private List<ArrivalStoryContainer> arrivalContainers;
    private List<Passenger> elevatorContainer;

    private int arrivedPassengers = 0;
    private int dispatchCapacity = 0;
    private int currentStory;
    private int counter = 0;
    /*if elevator goes up so direction's true else direction's false*/
    private boolean direction;
    private boolean activeElevator;

    public Controller(Config config) {
        this.config = config;
        elevatorContainer = new ArrayList<Passenger>();
    }

    public synchronized boolean exitElevator(Passenger passenger) throws InterruptedException {
        if (passenger.getDestinationStory() == currentStory) {
            LOG.info("Go out passenger " + passenger.getPassengerId() + " from elevator");
            arrivalContainers.get(currentStory - 1).addPassenger(passenger);
            elevatorContainer.remove(passenger);
            arrivedPassengers++;
            return true;
        }
        return false;
    }

    public synchronized boolean entryElevator(Passenger passenger) throws InterruptedException {
        if (passenger.getTask().isDirection() == direction && elevatorContainer.size() < config.getElevatorCapacity()) {
            LOG.info("Enter passenger " + passenger.getPassengerId() + " to elevator");
            elevatorContainer.add(passenger);
            dispatchContainers.get(currentStory - 1).removePassenger(passenger);
            return true;
        }
        return false;
    }

    public synchronized boolean isLastPassenger() {
        counter++;
        if (counter == dispatchCapacity) {
            activeElevator = false;
            counter = 0;
            return true;
        } else {
            return false;
        }
    }

    public synchronized void action() {
        try {
            while (config.getPassengersNumber() > arrivedPassengers) {
                direction = true;
                for (currentStory = 1; currentStory < config.getStoriesNumber(); currentStory++) {
                    Actions.currentFloor(currentStory);
                    ContainerPanel.currentFloorYPos = currentStory;
                    operationWithPassengers();
                    dispatchCapacity = 0;
                    Actions.movingElevator(config.getStoriesNumber(), currentStory, direction);
                }
                direction = false;
                //move down elevator
                for (currentStory = config.getStoriesNumber(); currentStory > 1; currentStory--) {
                    Actions.currentFloor(currentStory);
                    ContainerPanel.currentFloorYPos = currentStory;
                    operationWithPassengers();
                    dispatchCapacity = 0;
                    Actions.movingElevator(config.getStoriesNumber(), currentStory, direction);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void operationWithPassengers() throws InterruptedException{
        wakePassengersInElevator();
        stopElevator();
        wakePassengersInFloor();
        stopElevator();
    }

    private void wakePassengersInElevator(){
        synchronized (elevatorContainer) {
            dispatchCapacity = elevatorContainer.size();
            if (dispatchCapacity != 0) {
                activeElevator = true;
                LOG.info("Wake up all passengers in the Elevator");
                elevatorContainer.notifyAll();
            }
        }
    }

    private void wakePassengersInFloor(){
        dispatchCapacity = 0;
        synchronized (dispatchContainers.get(currentStory - 1).getPassengers()) {
            dispatchCapacity = dispatchContainers.get(currentStory - 1).getPassengers().size();
            if (dispatchCapacity != 0) {
                activeElevator = true;
                LOG.info("Wake up all passengers on the " + currentStory + " story");
                dispatchContainers.get(currentStory - 1).getPassengers().notifyAll();
            }
        }
    }

    private void stopElevator()throws InterruptedException{
        while (activeElevator){
            LOG.info("Stop elevator");
            wait();
        }
    }

    public boolean isDirection() {
        return direction;
    }

    public int getArrivedPassengers() {
        return arrivedPassengers;
    }

    public int getCurrentStory() {
        return currentStory;
    }

    public List<DispatchStoryContainer> getDispatchContainers() {
        return dispatchContainers;
    }

    public List<Passenger> getElevatorContainer() {
        return elevatorContainer;
    }

    public void setDispatchContainers(List<DispatchStoryContainer> dispatchContainers) {
        this.dispatchContainers = dispatchContainers;
    }

    public void setArrivalContainers(List<ArrivalStoryContainer> arrivalContainers) {
        this.arrivalContainers = arrivalContainers;
    }
}
