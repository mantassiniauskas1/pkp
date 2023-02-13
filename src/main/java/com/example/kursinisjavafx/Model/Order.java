package com.example.kursinisjavafx.Model;

import com.example.kursinisjavafx.Jdbc.JdbcDao;
import lombok.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Getter
@Setter
public class Order {

    private int id, distanceKm;
    private String locationFrom, locationTo, product;

    private static final int min = 10000;
    private static final int max = 99999;

    public Order(int id, int distanceKm, String locationFrom, String locationTo, String product) {
        this.id = id;
        this.distanceKm = distanceKm;
        this.locationFrom = locationFrom;
        this.locationTo = locationTo;
        this.product = product;
    }

        public static void createOrder ( int cargoID, int destinationID){
            int id = (int) ((Math.random() * (max - min + 1)) + min);
            try {
                Connection connection = JdbcDao.connectToDB();

                PreparedStatement ps = connection.prepareStatement("SELECT * FROM `cargo` WHERE `cargoID` = ?");
                ps.setInt(1, cargoID);
                ResultSet rs = ps.executeQuery();

                if(rs.next())
                    if (rs.getString("inOrder").equals("true")) {
                        return;
                    }

                ps = connection.prepareStatement("INSERT INTO `order` (`id`, `driverID`, `vehicleLicensePlate`, `cargoID`, `destinationID`, `status`) VALUES (?, ?, ?, ?, ?, ?);");
                ps.setInt(1, id);
                ps.setInt(2, 0);
                ps.setString(3, "");
                ps.setInt(4, cargoID);
                ps.setInt(5, destinationID);
                ps.setString(6, "In preparation");
                ps.execute();

                ps = connection.prepareStatement("UPDATE `cargo` SET inOrder = ? WHERE `cargoID` = ?");
                ps.setString(1, "true");
                ps.setInt(2, cargoID);
                ps.executeUpdate();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }

        public static void deleteOrder ( int orderID) throws SQLException {
            Connection connection = JdbcDao.connectToDB();
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM `order` WHERE `id` = ?");
            ps.setInt(1, orderID);
            ResultSet rs = ps.executeQuery();

            if(rs.next())
                if (!rs.getString("status").equals("In progress")) {
                    ps = connection.prepareStatement("DELETE FROM `order` WHERE `id` = ?");
                    ps.setInt(1, orderID);
                    ps.executeUpdate();

                    ps = connection.prepareStatement("UPDATE `cargo` SET `inOrder`=? WHERE cargoID=?");
                    ps.setString(1, "false");
                    ps.setInt(2, rs.getInt("cargoID"));
                    ps.executeUpdate();

                }
        }

        public static void assignDriver(int orderId, int driverId, String licensePlate) throws SQLException {

            Connection connection = JdbcDao.connectToDB();
            PreparedStatement ps = connection.prepareStatement("UPDATE `order` SET `driverID`=? WHERE `id`=?");
            ps.setInt(1, driverId);
            ps.setInt(2, orderId);
            ps.executeUpdate();

            ps = connection.prepareStatement("UPDATE `order` SET `vehicleLicensePlate`=? WHERE `id`=?");
            ps.setString(1, licensePlate);
            ps.setInt(2, orderId);
            ps.executeUpdate();

            ps = connection.prepareStatement("UPDATE `order` SET `status`=? WHERE `id`=?");
            ps.setString(1, "In progress");
            ps.setInt(2, orderId);
            ps.executeUpdate();

            ps = connection.prepareStatement("UPDATE `vehicle` SET `isTaken`=? WHERE `licensePlate`=?");
            ps.setString(1, "true");
            ps.setString(2, licensePlate);
            ps.executeUpdate();
        }

        public static int checkIfAssigned(int driverId) throws SQLException {

            Connection connection = JdbcDao.connectToDB();
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM `order` WHERE `driverID` = ? AND `status` = ?");
            ps.setInt(1, driverId);
            ps.setString(2, "In progress");
            ResultSet rs = ps.executeQuery();

            if (rs.next())
                return rs.getInt("id");
            return 0;
        }

        public static void completeOrder(int driverId) throws SQLException {

            Connection connection = JdbcDao.connectToDB();
            PreparedStatement ps = connection.prepareStatement("UPDATE `order` SET `status`=? WHERE driverID=?");
            ps.setString(1, "Completed");
            ps.setInt(2, driverId);
            ps.executeUpdate();

            ps = connection.prepareStatement("SELECT * FROM `order` WHERE `driverID` = ?");
            ps.setInt(1, driverId);
            ResultSet rs = ps.executeQuery();

            String vehicleLicensePlate = "";
            if (rs.next()) {
                vehicleLicensePlate = rs.getString("vehicleLicensePlate");
            }

            ps = connection.prepareStatement("UPDATE `vehicle` SET `isTaken`=? WHERE `licensePlate`=?");
            ps.setString(1, "false");
            ps.setString(2, vehicleLicensePlate);
            ps.executeUpdate();

        }

    }