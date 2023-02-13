package com.example.kursinisjavafx.Controller;

import com.example.kursinisjavafx.Jdbc.JdbcDao;
import com.example.kursinisjavafx.Model.Destination;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.SQLException;

public class DestinationController {
    private static final int min = 10000;
    private static final int max = 99999;

    @FXML
    private TextField destinationIDTextField;
    @FXML
    private TextField destinationFromTextField;
    @FXML
    private TextField destinationToTextField;
    @FXML
    private TextField additionalStopsTextField;
    @FXML
    private TextField distanceKmTextField;
    @FXML
    private Button submitCreateDestinationButton;

    public void initialize() {
        int id = (int) ((Math.random() * (max - min + 1)) + min);
        destinationIDTextField.setText(String.valueOf(id));
    }

    public void submitCreateDestinationButtonOnAction(ActionEvent e) throws SQLException {
        Destination.createDestination(Integer.parseInt(destinationIDTextField.getText()), (destinationFromTextField.getText()), destinationToTextField.getText(), additionalStopsTextField.getText(), Integer.parseInt(distanceKmTextField.getText()));
    }
}