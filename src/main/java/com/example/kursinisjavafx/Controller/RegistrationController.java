package com.example.kursinisjavafx.Controller;

import java.io.File;

import com.example.kursinisjavafx.Jdbc.JdbcDao;
import com.example.kursinisjavafx.Main;
import com.example.kursinisjavafx.Model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;

import java.io.IOException;
import java.sql.SQLException;

public class RegistrationController {

    @FXML
    private BorderPane rootPane;
    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField passwordTextField;
    @FXML
    private PasswordField confirmPasswordTextField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField fullnameTextField;
    @FXML
    private TextField surnameTextField;
    @FXML
    private TextField phoneNoTextField;
    @FXML
    private DatePicker datePickerField;
    @FXML
    private Button registerButton;
    @FXML
    private Label goBackLabel;
    @FXML
    private Label errorLabel;
    @FXML
    private TextField fileTextField;

    public void goBackLabelOnMouseClicked(MouseEvent mouseEvent) throws IOException {
        Main m = new Main();
        m.changeScene("login.fxml", 342, 425);
    }

    public void registerButtonOnAction(ActionEvent e) throws IOException, SQLException {
        if (usernameTextField.getText().isBlank() || passwordTextField.getText().isBlank() || emailField.getText().isBlank() || confirmPasswordTextField.getText().isBlank() || fullnameTextField.getText().isBlank() || surnameTextField.getText().isBlank() || phoneNoTextField.getText().isBlank() || datePickerField.getValue() == null)
            errorLabel.setText("Some fields are empty.");
        else if (!passwordTextField.getText().equals(confirmPasswordTextField.getText()))
            errorLabel.setText("Passwords do not match");
        else if (User.checkIfUsernameExists(usernameTextField.getText()))
            errorLabel.setText("User with such username already exists.");
        else {
            User.createUser(usernameTextField.getText(), passwordTextField.getText(), fullnameTextField.getText(), surnameTextField.getText(), emailField.getText(), phoneNoTextField.getText(), datePickerField.getValue().toString());
            Main m = new Main();
            m.changeScene("login.fxml", 342, 425);
        }
    }

    public void FileTextFieldOnMouseClicked(MouseEvent mouseEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open File");
        File file = fileChooser.showOpenDialog(fileTextField.getScene().getWindow());
        fileTextField.setText(file.getName());
    }



}
