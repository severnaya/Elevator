package com.epam.lift.newprogect.gui;

import com.epam.lift.newprogect.ElevatorManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class ContainerPanel extends JPanel{

    public static final int LIFT_HEIGHT = 60;
    public static final int INTERVAL = 5;
    public static int elevatorWidth = 250;

    /*width of line for floor*/
    public static int lineWidth = 100;
    /*area for driving*/
    private Dimension areaPanel;
    private JPanel drawingPanel;
    private JScrollPane scroll;
    /*previous frame's width*/
    private int width;

    private ComponentListener componentListener = new ComponentListener() {
        @Override
        public void componentResized(ComponentEvent e) {
            if (width != 0){
                lineWidth += (getWidth() - width)/2;
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
    };

    public ContainerPanel(){
        super(new BorderLayout());
        width = getWidth();
        setBorder(BorderFactory.createLineBorder(Color.black, 3));
        areaPanel = new Dimension(0,0);

        /*create panel for drawing*/
        drawingPanel = new DrawingPanel();
        drawingPanel.setBackground(Color.white);
        drawingPanel.addComponentListener(componentListener);
        add(drawingPanel);
        drawingPanel.setVisible(false);
        /*add scroll*/
        scroll = new JScrollPane(drawingPanel);
        add(scroll, BorderLayout.CENTER);
    }

    public void draw(int storyNumber){
        /*set height of drawing areaPanel*/
        areaPanel.height = storyNumber * LIFT_HEIGHT;
        /*set size of drawingPanel*/
        drawingPanel.setPreferredSize(areaPanel);
        drawingPanel.revalidate();
        drawingPanel.repaint();

        if (storyNumber != 0){
            drawingPanel.setVisible(true);
            ElevatorManager.start();
        }else {
            ElevatorManager.stop();
            drawingPanel.setVisible(false);
        }
    }
}
