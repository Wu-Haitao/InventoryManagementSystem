package com.example.InventoryManagementSystem;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.Desktop;
import java.io.File;

public class MyLogger {
    private static final Logger myLogger = LogManager.getLogger(MyLogger.class.getName());

    public static void logInfo(String info) {
        myLogger.info(info);
    }

    public static void logErr(String err) {
        myLogger.error(err);
    }

    public static void openLogDir() {
        try {
            Desktop.getDesktop().open(new File(System.getProperty("user.dir") + "/AppData/Log/"));
        }
        catch (Exception e) {
            logErr(String.format("Failed to open log directory - %s", e.getMessage()));
        }
    }
}
