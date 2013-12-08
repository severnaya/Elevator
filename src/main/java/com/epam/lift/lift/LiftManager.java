package com.epam.lift.lift;

import com.epam.lift.threads.PersonThread;
import com.epam.lift.threads.SynchronizerThread;
import java.util.concurrent.CopyOnWriteArrayList;

public class LiftManager {

    public static SynchronizerThread syncThread;
    public static CopyOnWriteArrayList<PersonThread> personsExit;
    public static CopyOnWriteArrayList<PersonThread> personsEnter;

    private static int personNumber;
    private static int floorNumber;

    public static void initialize(int personNumb, int floorNumb){
        personNumber = personNumb;
        floorNumber = floorNumb;
        personsExit = new CopyOnWriteArrayList<PersonThread>();
    }

    public static void start(){

    }

    public static void stop(){

    }

    public static int getPersonEnterPosition(PersonThread personThread){
        return 0;
    }

    public static int getPersonExitPosition(PersonThread personThread){
        return 0;
    }


    private static int currentFloorPerson(int destinationStory){
        int currentFloor = (int) (Math.random() * LiftManager.floorNumber);
        if (currentFloor == destinationStory){
            currentFloor = currentFloorPerson(destinationStory);
        }
        return currentFloor;
    }
}
