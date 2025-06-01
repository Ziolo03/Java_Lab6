package com.example.lab6_java;

import java.io.IOException;
import java.util.logging.*;


public class LoggerManager {
    private static final Logger logger = Logger.getLogger("AppLogger");

    static {
        try {
            FileHandler fileHandler = new FileHandler("aplikacja_log.txt", true);
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
            logger.setLevel(Level.ALL);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Logger getLogger() {
        return logger;
    }
}
