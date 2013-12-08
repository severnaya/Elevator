package com.epam.lift.newprogect.container;

import com.epam.lift.newprogect.Passenger;
import java.util.ArrayList;
import java.util.List;

public class ElevatorContainer implements Container {

    private List<Passenger> passengers;
    private int currentFloor;

    public ElevatorContainer() {
        passengers = new ArrayList<Passenger>();
    }

    @Override
    public void addPassenger(Passenger passenger) {
        passengers.add(passenger);
    }

    public boolean isFullElevator(int elevatorCapacity){
        boolean isFull = false;
        if (passengers.size() == elevatorCapacity){
            isFull = true;
        }
        return isFull;
    }

    public void setCurrentFloor(int currentFloor) {
        this.currentFloor = currentFloor;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public List<Passenger> getPassengers() {
        return passengers;
    }
}
