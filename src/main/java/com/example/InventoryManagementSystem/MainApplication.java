package com.example.InventoryManagementSystem;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {
    private void switchToLoginStage() throws IOException {
        Stage stage = StageManager.switchToStage(StageInfo.LOGIN_STAGE);
        stage.show();
    }

    @Override
    public void start(Stage stage) throws IOException {
        switchToLoginStage();
    }

    public static void main(String[] args) {
        DatabaseHandler.connectDatabase("inventory.db");
        if (DatabaseHandler.init) DatabaseHandler.initDatabase();
        launch();
        DatabaseHandler.closeDatabase();
    }
}