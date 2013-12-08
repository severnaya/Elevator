package com.epam.lift.newprogect.gui;

import com.epam.lift.newprogect.ElevatorManager;
import com.epam.lift.newprogect.Passenger;
import com.epam.lift.newprogect.container.DispatchStoryContainer;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class DrawingPanel extends JPanel {

    public static int currentFloor;
    private static final int PASSENGER_SIZE = 5;
    private int storyNumber;
    // position of passenger on the floor
    private int positionPassenger = 0;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int x;
        int y;
        int height;
        int width;
        String text;

        Graphics2D g2d = (Graphics2D) g;
        Font font = new Font(getFont().getName(), Font.BOLD, 20);
        g2d.setFont(font);
        g2d.setColor(Color.black);

        storyNumber = ElevatorManager.elevator.getConfig().getStoriesNumber();

        for (int i = 1; i <= storyNumber; i++) {
            currentFloor = (storyNumber - 2) + 1;
            /*draw floor's number*/
            x = 0;
            y = i * ContainerPanel.LIFT_HEIGHT;
            text = String.valueOf(storyNumber - i + 1);
            g2d.drawString(text, x, y);

            /*draw left line*/
            height = 3;
            width = ContainerPanel.lineWidth;
            g2d.fillRect(x, y, width, height);

            /*draw right line*/
            x = getWidth() - ContainerPanel.lineWidth;
            g2d.fillRect(x, y, width, height);
        }
        g2d.setColor(Color.blue);
        g2d.setStroke(new BasicStroke(3.0f));

        if (storyNumber != 0) {
            /*draw elevator*/
            x = ContainerPanel.lineWidth + ContainerPanel.INTERVAL;
            y = ContainerPanel.LIFT_HEIGHT * currentFloor - ContainerPanel.LIFT_HEIGHT;
            width = ContainerPanel.elevatorWidth;
            height = ContainerPanel.LIFT_HEIGHT;
            g2d.drawRect(x, y, width, height);

            /*draw text capacity*/
            x += ContainerPanel.INTERVAL;
            y += ContainerPanel.INTERVAL * 4;
            text = String.valueOf(ElevatorManager.elevator.getConfig().getElevatorCapacity());
            g2d.drawString(text, x, y);
        }

        /*draw passengers on the current floor*/
        if (ElevatorManager.elevator.getDispatchContainer() != null) {
            g2d.setColor(Color.blue);
            drawPassenger(g2d, "enter", ElevatorManager.elevator.getDispatchContainer());
        }
    }

    private void drawPassenger(Graphics2D g2d, String enter, List<DispatchStoryContainer> passengers) {
        int xPassenger = 0; //passenger's x-coordinate
        int yPassenger = 0; //passenger's y-coordinate
        int yCoordinatePassenger = 0;

        for (int i = 0; i < passengers.size(); i++) {
            for (Passenger p : passengers.get(i).getPassengers()) {
                if (enter.equals("enter")) {
                    /*calculate passenger's x-coordinate on the floor*/
                    xPassenger = getWidth() - ContainerPanel.lineWidth + positionPassenger * PASSENGER_SIZE;
                    /*calculate passenger's y-coordinate on the floor*/
                    yPassenger = (storyNumber - p.getLocationStory()) * ContainerPanel.LIFT_HEIGHT + 5 - yCoordinatePassenger;
                    positionPassenger++;
                    if (xPassenger == getWidth()){
                        positionPassenger = 0;
                        xPassenger = getWidth() - ContainerPanel.lineWidth + positionPassenger * PASSENGER_SIZE;
                        yCoordinatePassenger -= (PASSENGER_SIZE + 3);
                    }
                }
                /*draw passenger*/
                g2d.drawRect(xPassenger, yPassenger, PASSENGER_SIZE, PASSENGER_SIZE);
            }
            yCoordinatePassenger = 0;
            positionPassenger = 0;
        }
    }
}
