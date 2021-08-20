package com.example.InventoryManagementSystem;

import javafx.application.Application;
import javafx.concurrent.Task;
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
        Task<Void> connectDatabase = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                DatabaseHandler.checkDrivers();
                DatabaseHandler.connectDatabase(System.getProperty("user.dir").replace("\\", "/") + "/AppData");
                if (DatabaseHandler.init) DatabaseHandler.initDatabase();
                return null;
            }
        };
        new Thread(connectDatabase).start();
        launch();
        DatabaseHandler.closeDatabase();
    }
}