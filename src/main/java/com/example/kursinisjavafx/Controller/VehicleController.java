package com.example.kursinisjavafx.Controller;

import com.example.kursinisjavafx.Jdbc.JdbcDao;
import com.example.kursinisjavafx.Model.Destination;
import com.example.kursinisjavafx.Model.Vehicle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.SQLException;

public class VehicleController {

    @FXML
    private TextField licensePlateTextField;
    @FXML
    private TextField brandTextField;
    @FXML
    private TextField modelTextField;
    @FXML
    private TextField yearOfManifactureTextField;
    @FXML
    private TextField mileageTextField;
    @FXML
    private TextField weightCapacityKgTextField;
    @FXML
    private TextField fuelCapacityLTextField;
    @FXML
    private Button submitCreateVehicleButton;

    public void submitCreateVehicleButtonOnAction(ActionEvent e) throws SQLException {
        Vehicle.createVehicle(licensePlateTextField.getText(), brandTextField.getText(), modelTextField.getText(), Integer.parseInt(yearOfManifactureTextField.getText()), Integer.parseInt(mileageTextField.getText()), Integer.parseInt(weightCapacityKgTextField.getText()), Integer.parseInt(fuelCapacityLTextField.getText()));
    }
}