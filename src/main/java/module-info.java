module com.example.kursinisjavafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires lombok;

    opens com.example.kursinisjavafx to javafx.fxml;
    exports com.example.kursinisjavafx;
    exports com.example.kursinisjavafx.Controller;
    opens com.example.kursinisjavafx.Controller to javafx.fxml;
    exports com.example.kursinisjavafx.Jdbc;
    opens com.example.kursinisjavafx.Jdbc to javafx.fxml;
    exports com.example.kursinisjavafx.Model;
    opens com.example.kursinisjavafx.Model to javafx.fxml;
}