package com.example.kursinisjavafx.Controller;

import com.example.kursinisjavafx.Jdbc.JdbcDao;
import com.example.kursinisjavafx.Model.Cargo;
import com.example.kursinisjavafx.Model.Order;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.SQLException;
public class CargoController {
        private static final int min = 10000;
        private static final int max = 99999;

        @FXML
        private TextField cargoIdTextField;
        @FXML
        private TextField cargoMassKgTextField;
        @FXML
        private TextField descriptionTextField;
        @FXML
        private Button submitCreateCargoButton;


        public void initialize() {
                int id = (int) ((Math.random() * (max - min + 1)) + min);
                cargoIdTextField.setText(String.valueOf(id));
        }
        public void submitCreateCargoButtonOnAction(ActionEvent e) throws SQLException {
                Cargo.createCargo(Integer.parseInt(cargoIdTextField.getText()), Integer.parseInt(cargoMassKgTextField.getText()), descriptionTextField.getText());
        }
}