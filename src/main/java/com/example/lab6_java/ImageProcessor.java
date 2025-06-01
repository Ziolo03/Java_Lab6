

package com.example.lab6_java;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.*;
import javafx.scene.paint.Color;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageProcessor {
    public static Image negative(Image image) {
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();
        WritableImage output = new WritableImage(width, height);
        PixelReader reader = image.getPixelReader();
        PixelWriter writer = output.getPixelWriter();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                javafx.scene.paint.Color color = reader.getColor(x, y);
                writer.setColor(x, y, javafx.scene.paint.Color.color(1 - color.getRed(), 1 - color.getGreen(), 1 - color.getBlue()));
            }
        }
        return output;
    }

    public static Image rotate(Image image, int angle) {
        int w = (int) image.getWidth();
        int h = (int) image.getHeight();
        WritableImage rotated = new WritableImage(h, w);
        PixelReader reader = image.getPixelReader();
        PixelWriter writer = rotated.getPixelWriter();

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                javafx.scene.paint.Color color = reader.getColor(x, y);
                if (angle == 90) {
                    writer.setColor(h - y - 1, x, color);
                } else if (angle == -90) {
                    writer.setColor(y, w - x - 1, color);
                }
            }
        }
        return rotated;
    }


    public static Image threshold(Image image, int threshold) {
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();
        WritableImage output = new WritableImage(width, height);
        PixelReader reader = image.getPixelReader();
        PixelWriter writer = output.getPixelWriter();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                javafx.scene.paint.Color color = reader.getColor(x, y);
                double gray = (color.getRed() + color.getGreen() + color.getBlue()) / 3;
                writer.setColor(x, y, gray * 255 > threshold ? javafx.scene.paint.Color.WHITE : javafx.scene.paint.Color.BLACK);
            }
        }
        return output;
    }

    public static Image edgeDetect(Image image) {
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();
        WritableImage output = new WritableImage(width, height);
        PixelReader reader = image.getPixelReader();
        PixelWriter writer = output.getPixelWriter();

        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {
                double center = reader.getColor(x, y).getBrightness();
                double right = reader.getColor(x + 1, y).getBrightness();
                double bottom = reader.getColor(x, y + 1).getBrightness();
                double diff = Math.abs(center - right) + Math.abs(center - bottom);
                diff = Math.min(diff, 1.0);
                Color edge = Color.gray(diff);
                writer.setColor(x, y, edge);
            }
        }
        return output;
    }

    public static Image scale(Image image, int newWidth, int newHeight) {
        return new WritableImage(image.getPixelReader(), newWidth, newHeight);
    }

    public static void saveImage(Image image, String name) throws IOException {
        BufferedImage fxImage = SwingFXUtils.fromFXImage(image, null);
        BufferedImage bufferedImage = new BufferedImage(fxImage.getWidth(), fxImage.getHeight(), BufferedImage.TYPE_INT_RGB);
        bufferedImage.getGraphics().drawImage(fxImage, 0, 0, null);
        File outputFile = new File(System.getProperty("user.home") + "/Pictures/" + name + ".jpg");
        outputFile.getParentFile().mkdirs();
        System.out.println("Zapisuję do: " + outputFile.getAbsolutePath());
        if (outputFile.exists()) {
            throw new IOException("Plik już istnieje");
        }

        boolean result = ImageIO.write(bufferedImage, "jpg", outputFile);
        System.out.println("Wynik zapisu: " + result);
    }

}