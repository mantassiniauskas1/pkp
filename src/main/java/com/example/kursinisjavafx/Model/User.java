package com.example.kursinisjavafx.Model;

import com.example.kursinisjavafx.Jdbc.JdbcDao;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import lombok.*;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


@Getter
@Setter
public abstract class User implements Serializable {


    private int id;
    private String username, password, fullname, surname, email, phoneNo;
    private String dateOfBirth;
    private String role;

    private static final int min = 10000;
    private static final int max = 99999;
    private static final String defaultRole = "undefined";

    public User(int id, String username, String password, String fullname, String surname, String email, String phoneNo, String dateOfBirth, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.fullname = fullname;
        this.surname = surname;
        this.email = email;
        this.phoneNo = phoneNo;
        this.dateOfBirth = dateOfBirth;
        this.role = role;
    }

    public static boolean login(String username, String password, Label loginMessageLabel) throws SQLException {

        Connection connection = JdbcDao.connectToDB();
        PreparedStatement ps = connection.prepareStatement("SELECT `username`, `password` FROM `user` WHERE `username` = ? AND `password` = ?");
        ps.setString(1, username);
        ps.setString(2, password);
        ResultSet rs = ps.executeQuery();


        if (rs.next()) {
            loginMessageLabel.setText("Login successfull");
            loginMessageLabel.setTextFill(Color.GREEN);
            return true;
        }
        else {
            loginMessageLabel.setText("Login failed");
            return false;
        }
    }

    public static void createUser(String username, String password, String fullname, String surname, String email, String phoneNo, String dateOfBirth) {
        int id = (int) ((Math.random() * (max - min + 1)) + min);
        try {
            Connection connection = JdbcDao.connectToDB();
            PreparedStatement ps = connection.prepareStatement("INSERT INTO user(id, username, password, fullname, surname, email, phoneNo, dateOfBirth, role) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
            ps.setString(1, String.valueOf(id));
            ps.setString(2, username);
            ps.setString(3, password);
            ps.setString(4, fullname);
            ps.setString(5, surname);
            ps.setString(6, email);
            ps.setString(7, phoneNo);
            ps.setString(8, dateOfBirth);
            ps.setString(9, defaultRole);
            ps.execute();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static boolean checkIfUsernameExists(String username) throws SQLException {
        Connection connection = JdbcDao.connectToDB();
        PreparedStatement ps = connection.prepareStatement("SELECT `username` FROM `user` WHERE `username` = ?");
        ps.setString(1, username);
        ResultSet rs = ps.executeQuery();

        if (rs.next())
            return true;
        else
            return false;
    }

    public static void changePassword(int id, String password) throws SQLException {
        Connection connection = JdbcDao.connectToDB();
        PreparedStatement ps = connection.prepareStatement("UPDATE `user` SET `password` = ? WHERE `id` = ?");
        ps.setString(1, password);
        ps.setInt(2, id);
        ps.executeUpdate();
    }


}
