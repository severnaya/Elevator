package com.epam.lift.newprogect.container;

import com.epam.lift.newprogect.Passenger;

import java.util.ArrayList;
import java.util.List;

public class ArrivalStoryContainer implements Container{

    private List<Passenger> passengers;

    public ArrivalStoryContainer() {
        this.passengers = new ArrayList<Passenger>();
    }

    @Override
    public void addPassenger(Passenger passenger) {
        passengers.add(passenger);
    }

    public void setPassengers(List<Passenger> passengers) {
        this.passengers = passengers;
    }

    public List<Passenger> getPassengers() {
        return passengers;
    }
}
