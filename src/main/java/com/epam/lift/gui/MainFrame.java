package com.epam.lift.gui;

import com.epam.lift.lift.Constants;
import com.epam.lift.newprogect.Config;
import com.epam.lift.newprogect.ElevatorManager;
import com.epam.lift.threads.SynchronizerThread;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame {
    private JLabel lbPersonNumber = new JLabel(Constants.PASSENGER_NUMBER);
    private JLabel lbCapacity = new JLabel(Constants.ELEVATOR_CAPACITY);
    private JLabel lbFloorNumber = new JLabel(Constants.STORY_NUMBER);
    private JTextField txtPersonNumber = new JTextField(5);
    private JTextField txtCapacity = new JTextField(5);
    private JTextField txtFloorNumber = new JTextField(5);
    private JToggleButton btnStartStop = new JToggleButton(Constants.START_BUTTON_TEXT);
    private JSlider slider = new JSlider(JSlider.HORIZONTAL, 10, 60, 10);
    private JPanel panComponents = new JPanel();
    private ContainerPanel drawingPanel;

    public MainFrame() {
        drawingPanel = new ContainerPanel();
        setTitle(Constants.FRAME_TITLE);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int sizeWidth = Constants.FRAME_WIDTH;
        int sizeHeight = Constants.FRAME_HEIGHT;
        int locationX = (screenSize.width - sizeWidth) / 2;
        int locationY = (screenSize.height - sizeHeight) / 2;
        setBounds(locationX, locationY, sizeWidth, sizeHeight);
        setMinimumSize(new Dimension(sizeWidth, sizeHeight));

        Container c = getContentPane();
        c.setLayout(new BorderLayout());

        Config config = ElevatorManager.elevator.getConfig();
        txtPersonNumber.setText("" + config.getPassengersNumber());
        txtCapacity.setText("" + config.getElevatorCapacity());
        txtFloorNumber.setText("" + config.getStoriesNumber());

        panComponents.setLayout(new FlowLayout());
        panComponents.add(lbPersonNumber);
        panComponents.add(txtPersonNumber);
        panComponents.add(lbCapacity);
        panComponents.add(txtCapacity);
        panComponents.add(lbFloorNumber);
        panComponents.add(txtFloorNumber);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setMajorTickSpacing(10);
        slider.setMinorTickSpacing(20);
        panComponents.add(slider);
        panComponents.add(btnStartStop);
        c.add(panComponents, BorderLayout.NORTH);
        c.add(drawingPanel);

        slider.addChangeListener(new SliderListener());
        btnStartStop.addActionListener(new StartButtonListener());

        /* set animation speed */
        SynchronizerThread.animationSpeed = slider.getValue();
    }

    private class StartButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            StringBuilder errorFields = new StringBuilder();
            /* get number of persons */
            int passengerNumber = getTextFieldValue(txtPersonNumber,
                    Constants.PASSENGER_NUMBER, Constants.MIN_PASSENGERS_NUMBER_VALUE, errorFields);
            /* get lift capacity */
            int elevatorCapacity = getTextFieldValue(txtCapacity, Constants.ELEVATOR_CAPACITY, Constants.MIN_ELEVATOR_CAPACITY_VALUE,
                    errorFields);
            /* get number of floors */
            int storyNumber = getTextFieldValue(txtFloorNumber, Constants.STORY_NUMBER, Constants.MIN_STORIES_NUMBER_VALUE,
                    errorFields);

            if (!errorFields.toString().isEmpty()) {
                errorFields.deleteCharAt(errorFields.length() - 2);
                JOptionPane.showMessageDialog(null, Constants.ERROR_MESSAGE + errorFields, Constants.ERROR_MESSAGE_TITLE,
                        JOptionPane.WARNING_MESSAGE);
                btnStartStop.setSelected(false);
            }

            /*set value in config*/
            Config config = ElevatorManager.elevator.getConfig();
            config.setElevatorCapacity(elevatorCapacity);
            config.setPassengersNumber(passengerNumber);
            config.setStoriesNumber(storyNumber);

            if (btnStartStop.isSelected()) {
                btnStartStop.setText(Constants.STOP_BUTTON_TEXT);
                txtPersonNumber.setEditable(false);
                txtCapacity.setEditable(false);
                txtFloorNumber.setEditable(false);
				/* start lift */
                /*Lift.initialize(capacity, floorNumber);
                LiftManager.initialize(personNumber, floorNumber);
                LiftManager.start();*/
                ElevatorManager.elevator.setConfig(config);
                ElevatorManager.start();
                drawingPanel.draw();
            } else {
                btnStartStop.setText(Constants.START_BUTTON_TEXT);
                txtPersonNumber.setEditable(true);
                txtCapacity.setEditable(true);
                txtFloorNumber.setEditable(true);
				/* stop lift */
                //elevator.stop();
                //LiftManager.stop();
                ElevatorManager.stop();
                drawingPanel.draw();
            }
        }

        /**
         * check for correct value in the text field
         */
        private int getTextFieldValue(JTextField txtField, String fieldName, int border, StringBuilder error) {
            int value = 0;
            try {
                value = Integer.parseInt(txtField.getText());
                if (value < border)
                    throw new NumberFormatException();
            } catch (NumberFormatException e) {
                error.append(fieldName + ", ");
            }
            return value;
        }
    }

    private class SliderListener implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent e) {
            /*set animation speed*/
            SynchronizerThread.animationSpeed = ((JSlider) (e.getSource())).getValue();
        }
    }
}
