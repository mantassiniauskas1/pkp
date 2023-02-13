package com.example.kursinisjavafx.Controller;

import com.example.kursinisjavafx.Jdbc.JdbcDao;
import com.example.kursinisjavafx.Main;
import com.example.kursinisjavafx.Model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;

public class LoginController {

    @FXML
    private Button loginButton;
    @FXML
    private BorderPane rootPane;
    @FXML
    private Label loginMessageLabel;
    @FXML
    private TextField usernameTextField;
    @FXML
    private TextField passwordTextField;

    public void initialize() throws SQLException, IOException {
        quickLogin();
    }

    public void quickLogin() throws SQLException, IOException {
        usernameTextField.setText("admin");
        passwordTextField.setText("admin123");
        loginButtonOnAction();
    }


    public void loginButtonOnAction() throws IOException, SQLException {

        if (usernameTextField.getText().isBlank()) {
            loginMessageLabel.setText("Username is empty");
        }
        else if (passwordTextField.getText().isBlank()) {
            loginMessageLabel.setText("Password is empty");
        }
        else {
            if (User.login(usernameTextField.getText(), passwordTextField.getText(), loginMessageLabel)) {

                Main m = new Main();

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/kursinisjavafx/mainWindow.fxml"));
                Parent root = loader.load();
                MainMenuController mmc = loader.getController();
                mmc.createUserObject(usernameTextField.getText());

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.show();

            }
        }
    }

    public void signupLabelOnMouseClicked(MouseEvent mouseEvent) throws IOException {
        Main m = new Main();
        m.changeScene("registration.fxml", 433, 550);
    }

}