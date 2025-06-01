package com.example.lab6_java;

import javafx.animation.PauseTransition;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Popup;
import javafx.stage.Window;
import javafx.util.Duration;

public class ToastUtil {

    public static void show(Window ownerWindow, String message) {
        Popup popup = new Popup();
        Label label = new Label(message);
        label.setStyle("-fx-background-color: black; -fx-text-fill: white; -fx-padding: 10px;");

        StackPane container = new StackPane(label);
        container.setStyle("-fx-background-color: transparent;");
        popup.getContent().add(container);

        popup.setAutoHide(true);
        popup.show(ownerWindow);

        PauseTransition delay = new PauseTransition(Duration.seconds(2));
        delay.setOnFinished(e -> popup.hide());
        delay.play();
    }
}
