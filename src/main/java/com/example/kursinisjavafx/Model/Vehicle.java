package com.example.kursinisjavafx.Model;

import com.example.kursinisjavafx.Jdbc.JdbcDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Vehicle {

    private String licensePlate, brand, model, isTaken;
    private int yearOfManifacture, mileage, weightCapacityKg, fuelCapacityL;

    public static void createVehicle(String licensePlate, String brand, String model, int yearOfManifacture, int mileage, int weightCapacityKg, int fuelCapacityL) throws SQLException {
        Connection connection = JdbcDao.connectToDB();
        PreparedStatement ps = connection.prepareStatement("INSERT INTO vehicle(licensePlate, brand, model, yearOfManifacture, mileage, weightCapacityKg, fuelCapacityL, isTaken) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
        ps.setString(1, licensePlate);
        ps.setString(2, brand);
        ps.setString(3, model);
        ps.setInt(4, yearOfManifacture);
        ps.setInt(5, mileage);
        ps.setInt(6, weightCapacityKg);
        ps.setInt(7, fuelCapacityL);
        ps.setString(8, "false");
        ps.execute();
    }

    public static void updateVehicle(String licensePlateInput, String selectedColumn, String changedValue) throws SQLException {
        Connection connection = JdbcDao.connectToDB();
        PreparedStatement ps = connection.prepareStatement("UPDATE vehicle SET " + selectedColumn + "=? WHERE licensePlate=?");
        ps.setString(1, changedValue);
        ps.setString(2, licensePlateInput);
        ps.executeUpdate();
    }

    public static void deleteVehicle(String licensePlateInput) throws SQLException {
        Connection connection = JdbcDao.connectToDB();
        PreparedStatement ps = connection.prepareStatement("DELETE FROM `vehicle` WHERE `licensePlate` = ?");
        ps.setString(1, licensePlateInput);
        ps.executeUpdate();
    }

}
