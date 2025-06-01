
package com.example.lab6_java;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.scene.layout.VBox;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.application.Platform;



import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public class MainController {
    @FXML
    private ImageView originalImageView;
    @FXML
    private ImageView processedImageView;
    @FXML
    private ComboBox<String> operationComboBox;
    @FXML
    private Button executeButton;
    @FXML
    private Button openImageButton;
    @FXML
    private Button saveImageButton;
    @FXML
    private Label footerLabel;
    @FXML private Button rotateLeftButton;
    @FXML private Button rotateRightButton;
    @FXML private Button scaleButton;


    private Image originalImage;
    private Image processedImage;
    private final Logger logger = LoggerManager.getLogger();
    private final ExecutorService executor = Executors.newFixedThreadPool(4);

    @FXML
    public void initialize() {
        footerLabel.setText("Autor: Filip Zioło, PWr 2025");
        executeButton.setDisable(true);
        saveImageButton.setDisable(true);
        operationComboBox.getItems().addAll("Negatyw", "Progowanie", "Konturowanie");
        operationComboBox.setValue(null);
        scaleButton.setDisable(true);
        rotateLeftButton.setDisable(true);
        rotateRightButton.setDisable(true);
    }

    @FXML
    private void handleOpenImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Pliki JPG", "*.jpg"));
        File file = fileChooser.showOpenDialog(openImageButton.getScene().getWindow());

        if (file != null) {
            try {
                originalImage = new Image(file.toURI().toString());
                originalImageView.setImage(originalImage);
                processedImageView.setImage(null);
                executeButton.setDisable(false);
                saveImageButton.setDisable(false);
                scaleButton.setDisable(false);
                rotateLeftButton.setDisable(false);
                rotateRightButton.setDisable(false);


                ToastUtil.show(openImageButton.getScene().getWindow(), "Pomyślnie załadowano plik");

                logger.info("Załadowano plik: " + file.getName());
            } catch (Exception e) {
                ToastUtil.show(openImageButton.getScene().getWindow(), "Nie udało się załadować pliku");
                logger.severe("Błąd ładowania pliku: " + e.getMessage());
            }
        } else {
            ToastUtil.show(openImageButton.getScene().getWindow(), "Niedozwolony format pliku");
        }
    }

    @FXML
    private void handleExecute() {
        String operation = operationComboBox.getValue();
        if (operation == null) {
            ToastUtil.show(executeButton.getScene().getWindow(), "Nie wybrano operacji do wykonania");
            return;
        }
        if (originalImage == null) return;

        logger.info("Rozpoczęto operację: " + operation);

        switch (operation) {
            case "Negatyw":
                executor.submit(() -> {
                    try {
                        Image result = ImageProcessor.negative(originalImage);
                        Platform.runLater(() -> {
                            processedImage = result;
                            processedImageView.setImage(processedImage);
                            ToastUtil.show(executeButton.getScene().getWindow(), "Negatyw został wygenerowany pomyślnie!");
                            logger.info("Wykonano operację: Negatyw");
                        });
                    } catch (Exception e) {
                        Platform.runLater(() ->
                                ToastUtil.show(executeButton.getScene().getWindow(), "Nie udało się wykonać konturowania.")
                        );
                        logger.severe("Błąd operacji Negatyw: " + e.getMessage());
                    }
                });
                break;

            case "Progowanie":
                executor.submit(() -> {
                    try {
                        Image result = ImageProcessor.threshold(originalImage, 128);
                        Platform.runLater(() -> {
                            processedImage = result;
                            processedImageView.setImage(processedImage);
                            ToastUtil.show(executeButton.getScene().getWindow(), "Progowanie zostało przeprowadzone pomyślnie!");
                            logger.info("Wykonano operację: Progowanie");
                        });
                    } catch (Exception e) {
                        Platform.runLater(() ->
                                ToastUtil.show(executeButton.getScene().getWindow(), "Nie udało się wykonać prog.")
                        );

                        logger.severe("Błąd operacji Progowanie: " + e.getMessage());
                    }
                });
                break;

            case "Konturowanie":
                executor.submit(() -> {
                    try {
                        Image result = ImageProcessor.edgeDetect(originalImage);
                        Platform.runLater(() -> {
                            processedImage = result;
                            processedImageView.setImage(processedImage);
                            ToastUtil.show(executeButton.getScene().getWindow(), "Konturowanie zostało przeprowadzone pomyślnie!");
                            logger.info("Wykonano operację: Konturowanie");
                        });
                    } catch (Exception e) {
                        Platform.runLater(() ->
                                ToastUtil.show(executeButton.getScene().getWindow(), "Nie udało się wykonać konturowania.")
                        );
                        logger.severe("Błąd operacji Konturowanie: " + e.getMessage());
                    }
                });
                break;
        }
    }


    @FXML
    private void handleRotateLeft() {
        if (processedImage == null) return;
        processedImage = ImageProcessor.rotate(processedImage, -90);
        processedImageView.setImage(processedImage);
        logger.info("Obrócono obraz o -90 stopni");
    }

    @FXML
    private void handleRotateRight() {
        if (processedImage == null) return;
        processedImage = ImageProcessor.rotate(processedImage, 90);
        processedImageView.setImage(processedImage);
        logger.info("Obrócono obraz o 90 stopni");
    }


    @FXML
    private void handleScale() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Skalowanie obrazu");

        VBox content = new VBox(10);
        TextField widthField = new TextField();
        TextField heightField = new TextField();
        widthField.setPromptText("Szerokość (1-3000)");
        heightField.setPromptText("Wysokość (1-3000)");

        Button resetSize = new Button("Przywróć oryginalny rozmiar");
        resetSize.setOnAction(e -> {
            widthField.setText(String.valueOf((int) originalImage.getWidth()));
            heightField.setText(String.valueOf((int) originalImage.getHeight()));
        });

        content.getChildren().addAll(new Label("Nowa szerokość:"), widthField, new Label("Nowa wysokość:"), heightField, resetSize);
        dialog.getDialogPane().setContent(content);

        ButtonType okButton = new ButtonType("Zmień rozmiar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButton, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(response -> {
            if (response == okButton) {
                try {
                    int newWidth = Integer.parseInt(widthField.getText());
                    int newHeight = Integer.parseInt(heightField.getText());

                    if (newWidth <= 0 || newWidth > 3000 || newHeight <= 0 || newHeight > 3000) {
                        ToastUtil.show(scaleButton.getScene().getWindow(), "Podano nieprawidłowe wymiary!");
                        return;
                    }

                    processedImage = ImageProcessor.scale(originalImage, newWidth, newHeight);
                    processedImageView.setImage(processedImage);
                    ToastUtil.show(scaleButton.getScene().getWindow(), "Skalowanie zakończone!");
                } catch (NumberFormatException e) {
                    ToastUtil.show(scaleButton.getScene().getWindow(), "Pole jest wymagane");
                }
            }
        });
    }

    @FXML
    private void handleSaveImage() {
        if (processedImage == null) {
            ToastUtil.show(saveImageButton.getScene().getWindow(), "Na pliku nie zostały wykonane żadne operacje!");
            return;
        }

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Zapis obrazu");
        dialog.setHeaderText("Podaj nazwę pliku (bez rozszerzenia):");
        dialog.showAndWait().ifPresent(name -> {
            if (name.length() < 3) {
                ToastUtil.show(saveImageButton.getScene().getWindow(), "Wpisz co najmniej 3 znaki");
            } else {
                try {
                    ImageProcessor.saveImage(processedImage, name);
                    ToastUtil.show(saveImageButton.getScene().getWindow(), "Zapisano obraz w pliku " + name + ".jpg");
                    logger.info("Zapisano obraz jako: " + name + ".jpg");
                } catch (IOException e) {
                    ToastUtil.show(saveImageButton.getScene().getWindow(), "Nie udało się zapisać pliku " + name + ".jpg");
                    logger.severe("Błąd zapisu pliku: " + e.getMessage());
                }
            }
        });
    }
}
