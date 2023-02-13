package com.example.kursinisjavafx.Controller;

import com.example.kursinisjavafx.Jdbc.JdbcDao;
import com.example.kursinisjavafx.Main;
import com.example.kursinisjavafx.Model.*;
import com.example.kursinisjavafx.Model.Driver;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.scene.control.TableColumn.CellDataFeatures;


import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import lombok.SneakyThrows;

public class MainMenuController implements Initializable {

    @FXML
    Label welcomeLabel;
    @FXML
    Button completeCurrentOrderButton;
    @FXML
    ComboBox generalDriverIdComboBox;
    @FXML
    ComboBox generalOrderIdComboBox;
    @FXML
    ComboBox generalVehicleIdComboBox;
    @FXML
    Button assignOrderButton;
    @FXML
    Label currentOrderIdLabel;
    @FXML
    Button generalRefreshButton;

    @FXML
    TableView userTableView;
    @FXML
    TextField usernameTextField;
    @FXML
    ComboBox roleComboBox;
    @FXML
    Button deleteUserButton;
    @FXML
    Button setRoleButton;
    @FXML
    Button refreshUserButton;


    @FXML
    TableView orderTableView;
    @FXML
    Button createOrderButton;
    @FXML
    Button updateOrderButton;
    @FXML
    Button deleteOrderButton;
    @FXML
    TextField orderTextField;


    @FXML
    TableView cargoTableView;
    @FXML
    Button createCargoButton;
    @FXML
    TextField cargoTextField;
    @FXML
    ComboBox cargoComboBox;
    @FXML
    TextField cargoUpdateTextField;
    @FXML
    Button deleteCargoButton;
    @FXML
    Button refreshCargoButton;


    @FXML
    TableView destinationTableView;
    @FXML
    Button createDestinationButton;
    @FXML
    TextField destinationTextField;
    @FXML
    ComboBox destinationComboBox;
    @FXML
    TextField destinationUpdateTextField;
    @FXML
    Button updateDestinationButton;
    @FXML
    Button deleteDestinationButton;
    @FXML
    Button refreshDestinationButton;


    @FXML
    TableView vehicleTableView;
    @FXML
    Button createVehicleButton;
    @FXML
    TextField vehicleTextField;
    @FXML
    ComboBox vehicleComboBox;
    @FXML
    TextField vehicleUpdateTextField;
    @FXML
    Button updateVehicleButton;
    @FXML
    Button deleteVehicleButton;
    @FXML
    Button refreshVehicleButton;


    @FXML
    TextField accountCurrentPasswordTextField;
    @FXML
    TextField accountNewPasswordTextField;
    @FXML
    TextField accountRepeatNewPasswordTextField;
    @FXML
    Button changePasswordButton;
    @FXML
    Label changePasswordLabel;


    @FXML
    ListView<String> postListView;
    @FXML
    Button forumCreatePostButton;
    @FXML
    TextField forumTitleTextField;
    @FXML
    TextArea forumContentTextField;
    @FXML
    Text forumTitleErrorText;
    @FXML
    Button forumDeletePostButton;
    @FXML
    Label assignAnOrderText;
    @FXML
    Tab usersTab;
    @FXML
    Tab vehicleTab;
    @FXML
    Tab destinationTab;
    @FXML
    Tab cargoTab;
    @FXML
    BarChart ordersBarChart;
    User currUser;

    @Override
    public void initialize(URL location, ResourceBundle resources) {


        roleComboBox.getItems().addAll("undefined", "administrator", "manager", "driver");
        cargoComboBox.getItems().addAll("cargoID", "cargoMassKg", "description", "inOrder");
        destinationComboBox.getItems().addAll("destinationID", "destinationFrom", "destinationTo", "additionalStops", "distanceKm");
        vehicleComboBox.getItems().addAll("licensePlate", "brand", "model", "yearOfManifacture", "mileage", "weightCapacityKg", "fuelCapacityL", "isTaken");
        try {
            updateGeneralOrder();
            postListViewMouseClickListener();
            updateForumPost();
            tableLoad("user", userTableView);
            tableLoad("`order`", orderTableView);
            tableLoad("cargo", cargoTableView);
            tableLoad("destination", destinationTableView);
            tableLoad("vehicle", vehicleTableView);
            updateOrdersBarChart();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    public void setPermissions() {

        if (!currUser.getRole().equals("administrator")) {
            usernameTextField.setVisible(false);
            roleComboBox.setVisible(false);
            setRoleButton.setVisible(false);
            deleteUserButton.setVisible(false);
        }

        switch(currUser.getRole()) {
            case "driver":
                assignAnOrderText.setVisible(false);
                generalDriverIdComboBox.setVisible(false);
                generalOrderIdComboBox.setVisible(false);
                generalVehicleIdComboBox.setVisible(false);
                assignOrderButton.setVisible(false);
                generalRefreshButton.setVisible(false);
                createOrderButton.setVisible(false);
                orderTextField.setVisible(false);
                deleteOrderButton.setVisible(false);
                usersTab.setDisable(true);
                vehicleTab.setDisable(true);
                destinationTab.setDisable(true);
                cargoTab.setDisable(true);
                break;
            case "manager":
                completeCurrentOrderButton.setVisible(false);
                currentOrderIdLabel.setVisible(false);
                break;
        }
    }

    public void updateOrdersBarChart() throws SQLException {
        Connection connection = JdbcDao.connectToDB();
        PreparedStatement ps = connection.prepareStatement("SELECT COUNT(*) AS total FROM `order` WHERE `status` = ?");
        ps.setString(1, "Completed");
        ResultSet rs = ps.executeQuery();
        rs.next();
        Integer completedCount = rs.getInt("total");

        ps.setString(1, "In progress");
        rs = ps.executeQuery();
        rs.next();
        Integer inProgressCount = rs.getInt("total");

        ps.setString(1, "In preparation");
        rs = ps.executeQuery();
        rs.next();
        Integer inPreparationCount = rs.getInt("total");

        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        final BarChart<String,Number> bc =
                new BarChart<String,Number>(xAxis,yAxis);
        bc.setTitle("Orders status Summary");
        xAxis.setLabel("Status");
        yAxis.setLabel("Value");

        XYChart.Series series1 = new XYChart.Series();
        series1.setName("2022");
        series1.getData().add(new XYChart.Data("Completed", completedCount));
        series1.getData().add(new XYChart.Data("In progress", inProgressCount));
        series1.getData().add(new XYChart.Data("In preparation", inPreparationCount));

        ordersBarChart.getData().clear();
        ordersBarChart.getData().addAll(series1);
    }

    public void postListViewMouseClickListener()  {
        postListView.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @SneakyThrows
            @Override
            public void handle(MouseEvent click) {

                if (click.getClickCount() == 2) {

                    Main m = new Main();

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/kursinisjavafx/forumPost.fxml"));
                    Parent root = loader.load();
                    ForumPostController fpc = loader.getController();
                    fpc.init(currUser, getIdFromSelectedPost(), postListView.getSelectionModel().getSelectedItem());

                    Stage stage = new Stage();
                    stage.setScene(new Scene(root));
                    stage.show();
                }
            }
        });
    }

    public String getIdFromSelectedPost() throws SQLException {

        String tempArr[] = (postListView.getSelectionModel().getSelectedItem().split("\n\n"));
        String title = tempArr[0];
        Connection connection = JdbcDao.connectToDB();
        PreparedStatement ps = connection.prepareStatement("SELECT `id` FROM `forum` WHERE `title` = ?");
        ps.setString(1, title);
        ResultSet rs = ps.executeQuery();
        if(rs.next()) {
            return rs.getString("id");
        }
       return title;
    }

    public void updateForumPost() throws SQLException {

        List<String> results = new ArrayList<>();

        Connection connection = JdbcDao.connectToDB();
        PreparedStatement ps = connection.prepareStatement("SELECT * FROM `forum` WHERE `type` = ?");
        ps.setString(1, "post");
        ResultSet rs = ps.executeQuery();
        while(rs.next()) {
            String strPost = "";
            strPost += rs.getString("title") + "\n\n" + rs.getString("content") + "\n\n" + "Created by: " + rs.getString("author") + "\n" + "Creation date: " + rs.getString("postingDate");
            results.add(strPost);
        }

        ObservableList<String> post = FXCollections.observableArrayList(results);
        postListView.setItems(post);

    }

    public boolean checkIfForumPostTitleExists(String title) throws SQLException {

        Connection connection = JdbcDao.connectToDB();

        PreparedStatement ps = connection.prepareStatement("SELECT `title` FROM `forum` WHERE `title` = ?");
        ps.setString(1, title);
        ResultSet rs = ps.executeQuery();

        return rs.next();
    }
    public void forumCreatePostButtonOnAction() throws SQLException {
        if (forumTitleTextField.getText().isBlank() || forumContentTextField.getText().isBlank())
            return;

        if (checkIfForumPostTitleExists(forumTitleTextField.getText())) {
            forumTitleErrorText.setVisible(true);
            return;
        }
        forumTitleErrorText.setVisible(false);

        Connection connection = JdbcDao.connectToDB();
        PreparedStatement ps = connection.prepareStatement("INSERT INTO forum(type, author, title, content, postingDate) VALUES (?, ?, ?, ?, ?)");
        ps.setString(1, "post");
        ps.setString(2, currUser.getUsername());
        ps.setString(3, forumTitleTextField.getText());
        ps.setString(4, forumContentTextField.getText());
        ps.setString(5, String.valueOf(LocalDate.now()));
        ps.execute();
        updateForumPost();
    }

    public void forumDeletePostButtonOnAction() throws SQLException {

        String postId = getIdFromSelectedPost();

        if (!Objects.equals(getAuthorFromPost(), currUser.getUsername()) && !Objects.equals(currUser.getRole(), "administrator"))
            return;

        Connection connection = JdbcDao.connectToDB();
        PreparedStatement ps = connection.prepareStatement("DELETE FROM `forum` WHERE `id` = ?");
        ps.setString(1, postId);
        ps.executeUpdate();

        ps = connection.prepareStatement("DELETE FROM `forum` WHERE `belongsToPostId` = ?");
        ps.setString(1, postId);
        ps.executeUpdate();
        updateForumPost();
    }

    public String getAuthorFromPost() {

        String author = postListView.getSelectionModel().getSelectedItem();
        String[] str = author.split("\\R");
        for (String line: str) {
            if (line.contains("by:"))
                return line.substring(12);
        }
        return null;
    }

    public void updateGeneralOrder() throws SQLException {

        generalOrderIdComboBox.getItems().clear();
        generalVehicleIdComboBox.getItems().clear();
        generalDriverIdComboBox.getItems().clear();

        Connection connection = JdbcDao.connectToDB();
        PreparedStatement ps = connection.prepareStatement("SELECT * FROM `order` WHERE `status` = ?");
        ps.setString(1, "In preparation");
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            generalOrderIdComboBox.getItems().add(rs.getString("id"));
        }

        ps = connection.prepareStatement("SELECT * FROM `vehicle` WHERE `isTaken` = ?");
        ps.setString(1, "false");
        rs = ps.executeQuery();
        while (rs.next()) {
            generalVehicleIdComboBox.getItems().add(rs.getString("licensePlate"));
        }

        ps = connection.prepareStatement("SELECT * FROM `user` WHERE `role` = ?");
        ps.setString(1, "driver");
        rs = ps.executeQuery();
        while (rs.next()) {
            generalDriverIdComboBox.getItems().add(rs.getString("id"));
        }
    }
    public void generalRefreshButtonOnAction(ActionEvent e) throws SQLException {
        updateGeneralOrder();
    }

    public void createUserObject(String username) throws SQLException {
        User user;
        Connection connection = JdbcDao.connectToDB();

        PreparedStatement ps = connection.prepareStatement("SELECT * FROM `user` WHERE `username` = ?");
        ps.setString(1, username);
        ResultSet rs = ps.executeQuery();

        if(rs.next()) {
            switch(rs.getString("role")) {
                case "administrator":
                    user = new Administrator(rs.getInt("id"), rs.getString("username"), rs.getString("password"), rs.getString("fullname"), rs.getString("surname"), rs.getString("email"), rs.getString("phoneNo"), rs.getString("dateOfBirth"), rs.getString("role"));
                    currUser = user;
                    break;
                case "manager":
                    user = new Manager(rs.getInt("id"), rs.getString("username"), rs.getString("password"), rs.getString("fullname"), rs.getString("surname"), rs.getString("email"), rs.getString("phoneNo"), rs.getString("dateOfBirth"), rs.getString("role"));
                    currUser = user;
                    break;
                case "driver":
                    user = new Driver(rs.getInt("id"), rs.getString("username"), rs.getString("password"), rs.getString("fullname"), rs.getString("surname"), rs.getString("email"), rs.getString("phoneNo"), rs.getString("dateOfBirth"), rs.getString("role"));
                    currUser = user;
                    break;
                }
            }
        updateCurrUser();
        setPermissions();
    }

    public void updateCurrUser() throws SQLException {
        welcomeLabel.setText("Welcome " + currUser.getFullname());
        if (Order.checkIfAssigned(currUser.getId()) == 0)
            currentOrderIdLabel.setText("Your current order ID is: None");
        else
            currentOrderIdLabel.setText("Your current order ID is: " + Order.checkIfAssigned(currUser.getId()));
    }

    public void completeCurrentOrderButtonOnAction(ActionEvent e) throws SQLException {
        Order.completeOrder(currUser.getId());
        updateCurrUser();
        tableLoad("`order`", orderTableView);
        tableLoad("vehicle", vehicleTableView);
    }

    public void assignOrderButtonOnAction(ActionEvent e) throws SQLException {
        if (generalDriverIdComboBox.getValue().toString().isBlank() || generalOrderIdComboBox.getValue().toString().isBlank() || generalVehicleIdComboBox.getValue().toString().isBlank())
            return;
        Order.assignDriver(Integer.parseInt(generalOrderIdComboBox.getValue().toString()), Integer.parseInt(generalDriverIdComboBox.getValue().toString()), generalVehicleIdComboBox.getValue().toString());
        updateCurrUser();
        tableLoad("`order`", orderTableView);
        tableLoad("vehicle", vehicleTableView);
    }

    public void deleteUserButtonOnAction(ActionEvent e) throws SQLException {
        if (!usernameTextField.getText().isBlank())
            Administrator.deleteUser(usernameTextField.getText());
            tableLoad("user", userTableView);
    }
    public void setRoleButtonOnAction(ActionEvent e) throws SQLException {
        if (!usernameTextField.getText().isBlank() && !roleComboBox.toString().isBlank()) {
            Administrator.setRole(usernameTextField.getText(), roleComboBox.getValue().toString());
            tableLoad("user", userTableView);
        }
    }
    public void refreshUserButtonOnAction(ActionEvent e) throws SQLException {
        tableLoad("user", userTableView);
    }


    public void createOrderButtonOnAction(ActionEvent e) throws IOException {
        Main m = new Main();
        m.newWindow("createOrder.fxml", 0, 0);
    }
    public void deleteOrderButtonOnAction(ActionEvent e) throws SQLException {
        if (!orderTextField.getText().isBlank())
            Order.deleteOrder(Integer.parseInt(orderTextField.getText()));
        tableLoad("`order`", orderTableView);
        tableLoad("`cargo`", cargoTableView);
    }
    public void updateOrderButtonOnAction(ActionEvent e) throws SQLException {
        tableLoad("`order`", orderTableView);
        tableLoad("cargo", cargoTableView);
        updateOrdersBarChart();

    }


    public void createCargoButtonOnAction(ActionEvent e) throws IOException {
        Main m = new Main();
        m.newWindow("createCargo.fxml", 0, 0);
    }

    public void updateCargoButtonOnAction(ActionEvent e) throws IOException, SQLException {
        if (!cargoTextField.getText().isBlank() && !cargoUpdateTextField.getText().isBlank()) {
            Cargo.modifyCargo(Integer.parseInt(cargoTextField.getText()), cargoComboBox.getValue().toString(), cargoUpdateTextField.getText());
            tableLoad("cargo", cargoTableView);
        }

    }
    public void deleteCargoButtonOnAction(ActionEvent e) throws IOException, SQLException {
        if (!cargoTextField.getText().isBlank())
            Cargo.deleteCargo(Integer.parseInt(cargoTextField.getText()));
            tableLoad("cargo", cargoTableView);
    }
    public void refreshCargoButtonOnAction(ActionEvent e) throws SQLException {
        tableLoad("cargo", cargoTableView);
    }


    public void createDestinationButtonOnAction(ActionEvent e) throws IOException {
        Main m = new Main();
        m.newWindow("createDestination.fxml", 0, 0);
    }
    public void updateDestinationButtonOnAction(ActionEvent e) throws IOException, SQLException {
        Destination.modifyDestination(Integer.parseInt(destinationTextField.getText()), destinationComboBox.getValue().toString(), destinationUpdateTextField.getText());
        tableLoad("destination", destinationTableView);
    }

    public void deleteDestinationButtonOnAction(ActionEvent e) throws IOException, SQLException {
        Destination.deleteDestination(Integer.parseInt(destinationTextField.getText()));
        tableLoad("destination", destinationTableView);
    }
    public void refreshDestinationButtonOnAction(ActionEvent e) throws SQLException {
        tableLoad("destination", destinationTableView);
    }


    public void createVehicleButtonOnAction(ActionEvent e) throws IOException {
        Main m = new Main();
        m.newWindow("createVehicle.fxml", 0, 0);
    }

    public void updateVehicleButtonOnAction(ActionEvent e) throws IOException, SQLException {
        Vehicle.updateVehicle(vehicleTextField.getText(), vehicleComboBox.getValue().toString(), vehicleUpdateTextField.getText());
        tableLoad("vehicle", vehicleTableView);
    }

    public void deleteVehicleButtonOnAction(ActionEvent e) throws IOException, SQLException {
        Vehicle.deleteVehicle(vehicleTextField.getText());
        tableLoad("vehicle", vehicleTableView);
    }
    public void refreshVehicleButtonOnAction(ActionEvent e) throws SQLException {
        tableLoad("vehicle", vehicleTableView);
    }


    public void changePasswordButtonOnAction(ActionEvent e) throws SQLException {
        changePasswordLabel.setTextFill(Color.color(1, 0, 0));
        if(accountCurrentPasswordTextField.getText().isBlank() || accountNewPasswordTextField.getText().isBlank() || accountRepeatNewPasswordTextField.getText().isBlank()) {
            changePasswordLabel.setText("Please fill in all the fields.");
            return;
        }

        if(!accountCurrentPasswordTextField.getText().equals(currUser.getPassword())) {
            changePasswordLabel.setText("Incorrect current password.");
            return;
        }

        if(!accountNewPasswordTextField.getText().equals(accountRepeatNewPasswordTextField.getText())) {
            changePasswordLabel.setText("New passwords do not match.");
            return;
        }

        currUser.setPassword(accountNewPasswordTextField.getText());
        User.changePassword(currUser.getId(), currUser.getPassword());
        changePasswordLabel.setTextFill(Color.color(0, 1, 0));
        changePasswordLabel.setText("Password successfully changed.");
    }

    public void tableLoad(String tableName, TableView table) throws SQLException {
        ObservableList<ObservableList> data;
        ResultSet rs = JdbcDao.fetchTable(tableName);
        data = FXCollections.observableArrayList();
        table.getColumns().clear();

        for(int i=0 ; i<rs.getMetaData().getColumnCount(); i++){
            final int j = i;
            TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i+1));
            if (col.getText().equals("password"))
                continue;

            col.setCellValueFactory((Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(j).toString()));

            table.getColumns().addAll(col);
        }

        while(rs.next()) {
            ObservableList<String> row = FXCollections.observableArrayList();
            for(int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                row.add(rs.getString(i));
            }
            data.add(row);
        }

        table.setItems(data);
    }

}
