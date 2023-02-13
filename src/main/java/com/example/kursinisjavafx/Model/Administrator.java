package com.example.kursinisjavafx.Model;

import com.example.kursinisjavafx.Jdbc.JdbcDao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class Administrator extends User implements Serializable {

    public Administrator(int id, String username, String password, String fullname, String surname, String email, String phoneNo, String dateOfBirth, String role) {
        super(id, username, password, fullname, surname, email, phoneNo, dateOfBirth, role);
    }

    public static void setRole(String username, String role) throws SQLException {
        Connection connection = JdbcDao.connectToDB();
        PreparedStatement ps = connection.prepareStatement("UPDATE `user` SET role = ? WHERE `username` = ?");
        ps.setString(1, role);
        ps.setString(2, username);
        ps.executeUpdate();
    }

    public static void deleteUser(String username) throws SQLException {
        Connection connection = JdbcDao.connectToDB();
        PreparedStatement ps = connection.prepareStatement("DELETE FROM `user` WHERE `username` = ?");
        ps.setString(1, username);
        ps.executeUpdate();
    }


}