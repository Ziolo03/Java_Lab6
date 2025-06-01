package com.example.lab6_java;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.logging.Logger;

public class App extends Application {
    private static final Logger logger = Logger.getLogger(App.class.getName());

    @Override
    public void start(Stage stage) throws IOException {
        logger.info("Aplikacja została uruchomiona");
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/view/MainView.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Edytor Obrazów");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() {
        logger.info("Aplikacja została zamknięta");
        LoggerManager.getLogger().info("Aplikacja została zamknięta");
    }

    public static void main(String[] args) {
        launch();
    }
}
