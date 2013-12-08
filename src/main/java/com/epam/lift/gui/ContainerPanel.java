package com.epam.lift.gui;

import com.epam.lift.lift.LiftManager;
import com.epam.lift.newprogect.ElevatorManager;
import com.epam.lift.newprogect.Passenger;
import com.epam.lift.newprogect.container.DispatchStoryContainer;
import com.epam.lift.threads.SynchronizerThread;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.List;

public class ContainerPanel extends JPanel implements ComponentListener {

    /* lift height */
    private final int LIFT_HEIGHT = 60;
    /* interval between floor and lift */
    private final int INTERVAL = 5;
    /* width and height of person */
    private final int PASSENGER_SIZE = 5;

    /* area for drawing */
    private Dimension area;
    /* panel for drawing */
    private JPanel drawingPane;
    private JScrollPane scrollPanel;
    /* number of floors */
    private int floorNumber;
    /* y-coordinate of the current floor(line) */
    public static int currentFloorYPos;
    /* previous frame's width */
    private int width;
    /* floor(line) width */
    private int lineWidth;
    /* lift width */
    private int elevatorWidth = 260;

    public ContainerPanel() {
        super(new BorderLayout());
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        floorNumber = ElevatorManager.elevator.getConfig().getStoriesNumber();
        area = new Dimension(0, 0);
        lineWidth = 260;

        /*create panel for drawing*/
        drawingPane = new DrawingPanel();
        drawingPane.setBackground(Color.white);
        drawingPane.addComponentListener(this);
        /* create scrollPanel */
        scrollPanel = new JScrollPane(drawingPane);
        add(scrollPanel, BorderLayout.CENTER);
    }

    private class DrawingPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            int x, y, height, width;
            String text;

            Graphics2D g2d = (Graphics2D) g;
            Font font = new Font(getFont().getName(), Font.BOLD, 20);
            g2d.setFont(font);
            g2d.setColor(Color.BLACK);

            for (int i = 1; i <= floorNumber; i++) {
                 currentFloorYPos = 2;
				/* draw floor's number */
                x = 0;
                y = i * LIFT_HEIGHT;
                text = String.valueOf(floorNumber - i + 1);
                g2d.drawString(text, x, y);

				/* draw left line */
                height = 3;
                width = lineWidth;
                g2d.fillRect(x, y, width, height);

				/* draw right line */
                x = getWidth() - lineWidth;
                g2d.fillRect(x, y, width, height);
            }
            g2d.setColor(Color.BLUE);
            g2d.setStroke(new BasicStroke(3.0f));

            if (floorNumber != 0) {
                /* draw lift */
                x = lineWidth + INTERVAL;
                y = LIFT_HEIGHT * currentFloorYPos - LIFT_HEIGHT;
                width = elevatorWidth;
                height = LIFT_HEIGHT;
                g2d.drawRect(x, y, width, height);

				/* draw capacity */
                x = lineWidth + INTERVAL + 10;
                y = LIFT_HEIGHT * currentFloorYPos - LIFT_HEIGHT + 35;
                text = String.valueOf(ElevatorManager.elevator.getConfig().getElevatorCapacity());
                g2d.drawString(text, x, y);
            }
            font = new Font(getFont().getName(), Font.ITALIC, 10);
            g.setFont(font);

            /* draw passengers on the current floor */
            if (ElevatorManager.elevator.getDispatchContainer() != null) {
                g2d.setColor(Color.BLUE);
                drawPersons(g2d, "enter", ElevatorManager.elevator.getDispatchContainer());
            }
            /* draw exiting persons on the target floor */
            if (LiftManager.personsExit != null) {
                g2d.setColor(Color.RED);
                drawPersons(g2d, "exit", ElevatorManager.elevator.getDispatchContainer());
            }
        }

        /**
         * method for drawing persons on the floors
         */
        private void drawPersons(Graphics2D g2d, String enter, List<DispatchStoryContainer> passengers) {
            int pos = 0; // person position on the floor
            int x = 0; // person's x-coordinate
            int yPerson; // person's y-coordinate
            //int yFloor; // y-coordinate of the floor
            //String text; // contains current floor and target floor

            for (int currentFloor = 0; currentFloor < passengers.size(); currentFloor++) {
                for (Passenger p : passengers.get(currentFloor).getPassengers()){
                    if (enter.equals("enter")) {
					/* calculate person's x-coordinate on the current floor */
                        x = getWidth() - lineWidth + pos * PASSENGER_SIZE;
					/* calculate person's y-coordinate on the current floor */
                        yPerson = (floorNumber - p.getLocationStory()) * LIFT_HEIGHT + 10;
					/* calculate y-coordinate of the current floor */
                       // yFloor = (floorNumber - p.getLocationStory()) * LIFT_HEIGHT + 20;
                        pos++;
                     } else {
					/* calculate person's x-coordinate on the target floor */
                        x = lineWidth - pos * PASSENGER_SIZE - PASSENGER_SIZE;
					/* calculate person's y-coordinate on the target floor */
                        yPerson = (floorNumber - p.getDestinationStory()) * LIFT_HEIGHT + 10;
					/* calculate y-coordinate of the target floor */
                       // yFloor = (floorNumber - p.getDestinationStory()) * LIFT_HEIGHT + 20;
                        pos++;
                    }
				/* draw person */
                    g2d.drawRect(x, yPerson, PASSENGER_SIZE, PASSENGER_SIZE);
                }
                pos = 0;
            }
        }
    }

    /**
     * start drawing
     */
    public void draw() {
		/* number of floors */
        floorNumber = ElevatorManager.elevator.getConfig().getStoriesNumber();
		/* set height of drawing area */
        area.height = floorNumber * LIFT_HEIGHT;
		/* set size of panel for drawing */
        drawingPane.setPreferredSize(area);

        drawingPane.revalidate();
        drawingPane.repaint();

        if (floorNumber != 0) {
			/* set flag */
            SynchronizerThread.stop = false;
			/* start thread */
            ElevatorManager.start();
            /*LiftManager.syncThread = new SynchronizerThread(this);
            LiftManager.syncThread.start();*/
        } else {
            SynchronizerThread.stop = true;
        }
    }

    @Override
    public void componentResized(ComponentEvent e) {
        if (width != 0) {
            lineWidth += (getWidth() - width) / 2;
            elevatorWidth = getWidth() - 2 * lineWidth - 2 * INTERVAL - 25;
        }
        width = getWidth();
    }

    @Override
    public void componentMoved(ComponentEvent e) {
    }

    @Override
    public void componentShown(ComponentEvent e) {
    }

    @Override
    public void componentHidden(ComponentEvent e) {
    }
}
