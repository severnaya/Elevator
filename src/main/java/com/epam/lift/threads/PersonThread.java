package com.epam.lift.threads;

public class PersonThread extends Thread{

    public static final int NOT_STARTED = 0;
    public static final int IN_PROGRESS = 1;
    public static final int COMPLETED = 2;
    public static final int ABORTED = 3;

    private int idPerson;
    private int currentFloor;
    private int targetFloor;
    private int transportationTask;


    public PersonThread(int idPerson, int currentFloor, int targetFloor){
        this.idPerson = idPerson;
        this.currentFloor = currentFloor;
        this.targetFloor = targetFloor;
        transportationTask = NOT_STARTED;
    }

    @Override
    public void run() {

    }

    public int getCurrentFloor(){
        return currentFloor;
    }

    public int getTargetFloor(){
        return targetFloor;
    }

    public int getIdPerson() {
        return idPerson;
    }

    public int getTransportationTask() {
        return transportationTask;
    }
}
