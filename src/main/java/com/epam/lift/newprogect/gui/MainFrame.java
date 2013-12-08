package com.epam.lift.newprogect.gui;

import com.epam.lift.lift.Constants;
import com.epam.lift.newprogect.Config;
import com.epam.lift.newprogect.ElevatorManager;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame {
    private JLabel passengerNumberLabel;
    private JLabel elevatorCapacityLabel;
    private JLabel storyNumberLabel;
    private JTextField passengerNumberText;
    private JTextField elevatorCapacityText;
    private JTextField storyNumberText;
    private JButton buttonStartStop;
    private JSlider slider;
    private JPanel panelComponents;
    private ContainerPanel containerPanel;
    private Config config;

    private ActionListener buttonListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (buttonStartStop.getText().equals(Constants.START_BUTTON_TEXT)) {
                StringBuilder error = new StringBuilder();
                int passengerNumber = getValueFromText(passengerNumberText, Constants.PASSENGER_NUMBER,
                        Constants.MIN_PASSENGERS_NUMBER_VALUE, error);
                int elevatorCapacity = getValueFromText(elevatorCapacityText, Constants.ELEVATOR_CAPACITY,
                        Constants.MIN_ELEVATOR_CAPACITY_VALUE, error);
                int storyNumber = getValueFromText(storyNumberText, Constants.STORY_NUMBER,
                        Constants.MIN_STORIES_NUMBER_VALUE, error);

                /*show message when incorrect validation of fields*/
                if (!error.toString().isEmpty()) {
                    error.deleteCharAt(error.length() - 2);
                    JOptionPane.showMessageDialog(null, Constants.ERROR_MESSAGE + error, Constants.ERROR_MESSAGE_TITLE,
                            JOptionPane.WARNING_MESSAGE);
                    buttonStartStop.setText(Constants.START_BUTTON_TEXT);
                    return;
                }
                config.setPassengersNumber(passengerNumber);
                config.setElevatorCapacity(elevatorCapacity);
                config.setStoriesNumber(storyNumber);
                ElevatorManager.initialization(config);

                buttonStartStop.setText(Constants.STOP_BUTTON_TEXT);
                unlockField(false);
                containerPanel.draw(config.getStoriesNumber());
            } else {
                buttonStartStop.setText(Constants.START_BUTTON_TEXT);
                unlockField(true);
                containerPanel.draw(0);
            }
        }

        private void unlockField(boolean value) {
            passengerNumberText.setEditable(value);
            elevatorCapacityText.setEditable(value);
            storyNumberText.setEditable(value);
        }
    };

    private ChangeListener sliderListener = new ChangeListener() {

        @Override
        public void stateChanged(ChangeEvent e) {
            System.out.println(((JSlider) e.getSource()).getValue());
        }
    };

    public MainFrame() {
        initialization();
        setTitle(Constants.FRAME_TITLE);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int sizeWidth = Constants.FRAME_WIDTH;
        int sizeHeight = Constants.FRAME_HEIGHT;
        int locationX = (screenSize.width - sizeWidth) / 2;
        int locationY = (screenSize.height = sizeHeight) / 2;
        setBounds(locationX, locationY, sizeWidth, sizeHeight);
        setMinimumSize(new Dimension(sizeWidth, sizeHeight));

        Container container = getContentPane();
        container.setLayout(new BorderLayout());

        panelComponents.setLayout(new FlowLayout());
        panelComponents.add(passengerNumberLabel);
        panelComponents.add(passengerNumberText);
        panelComponents.add(elevatorCapacityLabel);
        panelComponents.add(elevatorCapacityText);
        panelComponents.add(storyNumberLabel);
        panelComponents.add(storyNumberText);
        slider.setMajorTickSpacing(10);
        slider.setMinorTickSpacing(5);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        panelComponents.add(slider);
        panelComponents.add(buttonStartStop);

        container.add(panelComponents, BorderLayout.NORTH);
        container.add(containerPanel);

        buttonStartStop.addActionListener(buttonListener);

        slider.addChangeListener(sliderListener);
    }

    private void initialization() {
        passengerNumberLabel = new JLabel(Constants.PASSENGER_NUMBER);
        elevatorCapacityLabel = new JLabel(Constants.ELEVATOR_CAPACITY);
        storyNumberLabel = new JLabel(Constants.STORY_NUMBER);
        passengerNumberText = new JTextField(5);
        elevatorCapacityText = new JTextField(5);
        storyNumberText = new JTextField(5);
        slider = new JSlider(JSlider.HORIZONTAL, Constants.MIN_SLIDER, Constants.MAX_SLIDER, Constants.INIT_SLIDER);
        buttonStartStop = new JButton(Constants.START_BUTTON_TEXT);
        panelComponents = new JPanel();
        containerPanel = new ContainerPanel();

        /*add value from config in TextPanels*/
        config = ElevatorManager.elevator.getConfig();
        passengerNumberText.setText("" + config.getPassengersNumber());
        elevatorCapacityText.setText("" + config.getElevatorCapacity());
        storyNumberText.setText("" + config.getStoriesNumber());
    }

    /*validation of field*/
    private int getValueFromText(JTextField textField, String fieldName, int border, StringBuilder error) {
        int value = 0;
        try {
            value = Integer.valueOf(textField.getText());
            if (value < border) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            error.append(fieldName + ", ");
        }
        return value;
    }
}
