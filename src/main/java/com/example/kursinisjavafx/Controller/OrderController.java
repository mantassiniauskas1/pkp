package com.example.kursinisjavafx.Controller;

import com.example.kursinisjavafx.Jdbc.JdbcDao;
import com.example.kursinisjavafx.Model.Order;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class OrderController {

    @FXML
    private ComboBox cargoIDComboBox;
    @FXML
    private ComboBox destinationIDComboBox;



    public void initialize() throws SQLException {

        Connection connection = JdbcDao.connectToDB();
        PreparedStatement ps = connection.prepareStatement("SELECT `cargoID` FROM `cargo` WHERE `inOrder`=?");
        ps.setString(1, "false");
        ResultSet rs = ps.executeQuery();   
        while (rs.next())
            cargoIDComboBox.getItems().add(rs.getString(1));

        ps = connection.prepareStatement("SELECT `destinationID` FROM `destination`");
        rs = ps.executeQuery();
        while (rs.next())
            destinationIDComboBox.getItems().add(rs.getString(1));
    }

    public void submitCreateOrderButtonOnAction(ActionEvent e) throws SQLException {
        if (!cargoIDComboBox.getValue().toString().isBlank() && !destinationIDComboBox.getValue().toString().isBlank())
            Order.createOrder(Integer.parseInt(cargoIDComboBox.getValue().toString()), Integer.parseInt(destinationIDComboBox.getValue().toString()));
    }

}
