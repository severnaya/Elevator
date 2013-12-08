package com.epam.lift.newprogect;

import com.epam.lift.newprogect.container.ArrivalStoryContainer;
import com.epam.lift.newprogect.container.DispatchStoryContainer;
import org.apache.log4j.Logger;
import java.util.*;

public class Elevator {
    private static final Logger LOG = Logger.getLogger(Elevator.class);

    private Config config;
    private List<DispatchStoryContainer> dispatchContainer;
    private List<ArrivalStoryContainer> arrivalContainer;
    private List<Passenger> allPassengers;
    private Controller controller;

    public Elevator(Config config) {
        this.config = config;
    }

    public void initializationContainers(){
        dispatchContainer = initializerDispatchContainer();
        arrivalContainer = initializerArrivalContainer();
        controller = new Controller(config);
        allPassengers = getAllPassengers();
        dispatchContainer = distributePassengersOnFloor(allPassengers);
        controller.setDispatchContainers(dispatchContainer);
        controller.setArrivalContainers(arrivalContainer);
    }

    public void start(){
        runPassengers();
        controller.action();
    }

    public void stop(){
        for (Passenger passenger: allPassengers){
            passenger.stopTask();
        }
    }

    private List<DispatchStoryContainer> initializerDispatchContainer() {
        List<DispatchStoryContainer> containers = new ArrayList<DispatchStoryContainer>();
        for (int i = 0; i < config.getStoriesNumber(); i++) {
            containers.add(new DispatchStoryContainer());
        }
        return containers;
    }

    private List<ArrivalStoryContainer> initializerArrivalContainer() {
        List<ArrivalStoryContainer> containers = new ArrayList<ArrivalStoryContainer>();
        for (int i = 0; i < config.getStoriesNumber(); i++) {
            containers.add(new ArrivalStoryContainer());
        }
        return containers;
    }

    private List<Passenger> getAllPassengers() {
        List<Passenger> passengers = new ArrayList<Passenger>();
        for (int i = 0; i < config.getPassengersNumber(); i++) {
            int locationStory = new Random().nextInt(config.getStoriesNumber()) + 1;
            passengers.add(new Passenger(controller, locationStory, config.getStoriesNumber()));
        }
        return passengers;
    }

    private List<DispatchStoryContainer> distributePassengersOnFloor(List<Passenger> allPassengers) {
        List<DispatchStoryContainer> containers = dispatchContainer;
        for (int i = 0; i < config.getStoriesNumber(); i++){
            for (Passenger passenger: allPassengers){
                if (passenger.getLocationStory() == i + 1){
                    containers.get(i).addPassenger(passenger);
                }
            }
            LOG.info(containers.get(i).getPassengers().size() + " passengers on the " + i + " floor ");
        }
        return containers;
    }

    private void runPassengers() {
        for (Passenger passenger : allPassengers) {
            synchronized (controller) {
                passenger.runTask();
                try {
                    controller.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    public List<DispatchStoryContainer> getDispatchContainer() {
        return dispatchContainer;
    }

    public List<ArrivalStoryContainer> getArrivalContainer() {
        return arrivalContainer;
    }
}
