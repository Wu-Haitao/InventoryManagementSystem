package com.example.InventoryManagementSystem;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MyLogger {
    private static final Logger myLogger = LogManager.getLogger(MyLogger.class.getName());

    public static void logInfo(String info) {
        myLogger.info(info);
    }

    public static void logErr(String err) {
        myLogger.error(err);
    }
}
