package com.example.kursinisjavafx.Model;

import com.example.kursinisjavafx.Jdbc.JdbcDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Cargo {

    private int cargoID, cargoMassKg;
    private String description;

    public static void createCargo(int cargoId, int cargoMassKg, String description) throws SQLException {
        Connection connection = JdbcDao.connectToDB();
        PreparedStatement ps = connection.prepareStatement("INSERT INTO cargo(cargoId, cargoMassKg, description, inOrder) VALUES (?, ?, ?, ?)");
        ps.setInt(1, cargoId);
        ps.setInt(2, cargoMassKg);
        ps.setString(3, description);
        ps.setString(4, "false");
        ps.execute();
    }

    public static void modifyCargo(int id, String selectedColumn, String changedValue) throws SQLException {
        Connection connection = JdbcDao.connectToDB();
        PreparedStatement ps = connection.prepareStatement("UPDATE cargo SET " + selectedColumn + "=? WHERE cargoID=?");
        ps.setString(1, changedValue);
        ps.setInt(2, id);
        ps.executeUpdate();
    }

    public static void deleteCargo(int id) throws SQLException {
        Connection connection = JdbcDao.connectToDB();
        PreparedStatement ps = connection.prepareStatement("DELETE FROM `cargo` WHERE `cargoID` = ?");
        ps.setInt(1, id);
        ps.executeUpdate();
    }

}
