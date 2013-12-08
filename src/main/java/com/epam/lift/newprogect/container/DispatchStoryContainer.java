package com.epam.lift.newprogect.container;

import com.epam.lift.newprogect.Passenger;
import org.apache.log4j.Logger;
import java.util.ArrayList;
import java.util.List;

public class DispatchStoryContainer implements Container{
    private static Logger LOG = Logger.getLogger(DispatchStoryContainer.class);

    private List<Passenger> passengers;

    public DispatchStoryContainer() {
        this.passengers = new ArrayList<Passenger>();
    }

    @Override
    public void addPassenger(Passenger passenger) {
        passengers.add(passenger);
    }

    public void removePassenger(Passenger passenger){
        passengers.remove(passenger);
    }

    public List<Passenger> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<Passenger> passengers) {
        this.passengers = passengers;
    }
}
