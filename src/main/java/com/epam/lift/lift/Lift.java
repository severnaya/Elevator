package com.epam.lift.lift;

public class Lift {

    public static int countPersons;
    public static int floorNumber;

    public static void initialize(int capacity, int floorNumb){
        countPersons = capacity;
        floorNumber = floorNumb;
    }
}
