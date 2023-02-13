package com.example.kursinisjavafx;

import com.example.kursinisjavafx.Controller.MainMenuController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends javafx.application.Application {

    private static Stage stg;

    @Override
    public void start(Stage stage) throws IOException {
        stg = stage;
        stage.setResizable(true);
        stage.centerOnScreen();

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 342, 398);
        stage.setScene(scene);
        stage.show();
    }

    public void changeScene(String fxml, double width, double height) throws IOException {

        Parent pane = FXMLLoader.load(getClass().getResource(fxml));
        stg.setWidth(width);
        stg.setHeight(height);
        stg.centerOnScreen();
        stg.setResizable(true);
        stg.getScene().setRoot(pane);
    }

    public void newWindow(String fxml, double width, double height) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource(fxml));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}