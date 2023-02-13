package com.example.kursinisjavafx.Jdbc;

import java.sql.*;

public class JdbcDao {

    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/cargo_transpo";
    private static final String DATABASE_USERNAME = "root";
    private static final String DATABASE_PASSWORD = "";
    private static Connection connection;

    public static Connection connectToDB() throws SQLException {
        connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
        return connection;
    }

    public static void disconnectFromDB(Connection connection) throws SQLException {
        connection.close();
    }

    public static ResultSet fetchTable(String tableName) throws SQLException {
        PreparedStatement ps;
        ps = connection.prepareStatement("SELECT * FROM " + tableName);
        return ps.executeQuery();
    }
}




