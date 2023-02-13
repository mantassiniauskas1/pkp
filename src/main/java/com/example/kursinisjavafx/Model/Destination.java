package com.example.kursinisjavafx.Model;

import com.example.kursinisjavafx.Jdbc.JdbcDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Destination {

    private int destinationID, distanceKm;
    private String destinationFrom, destinationTo, additionalStops;

    public static void createDestination(int destinationID, String destinationFrom, String destinationTo, String additionalStops, int distanceKm) throws SQLException {
        Connection connection = JdbcDao.connectToDB();
        PreparedStatement ps = connection.prepareStatement("INSERT INTO destination(destinationID, destinationFrom, destinationTo, additionalStops, distanceKm) VALUES (?, ?, ?, ?, ?)");
        ps.setInt(1, destinationID);
        ps.setString(2, destinationFrom);
        ps.setString(3, destinationTo);
        ps.setString(4, additionalStops);
        ps.setInt(5, distanceKm);
        ps.execute();
    }

    public static void modifyDestination(int id, String selectedColumn, String changedValue) throws SQLException {
        Connection connection = JdbcDao.connectToDB();
        PreparedStatement ps = connection.prepareStatement("UPDATE destination SET " + selectedColumn + "=? WHERE destinationID=?");
        ps.setString(1, changedValue);
        ps.setInt(2, id);
        ps.executeUpdate();
    }

    public static void deleteDestination(int id) throws SQLException {
        Connection connection = JdbcDao.connectToDB();
        PreparedStatement ps = connection.prepareStatement("DELETE FROM `destination` WHERE `destinationID` = ?");
        ps.setInt(1, id);
        ps.executeUpdate();
    }

}
