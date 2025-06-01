module com.example.lab6_java {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;
    requires javafx.swing;
    requires java.desktop;


    opens com.example.lab6_java to javafx.fxml;
    exports com.example.lab6_java;
}