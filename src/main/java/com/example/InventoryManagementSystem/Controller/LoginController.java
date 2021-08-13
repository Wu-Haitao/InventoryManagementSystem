package com.example.InventoryManagementSystem.Controller;

import com.example.InventoryManagementSystem.*;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class LoginController {
    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

    @FXML
    private Label promptMsg;

    @FXML
    private AnchorPane root;

    private void switchToMainStage() throws IOException {
        Stage stage = StageManager.switchToStage(StageInfo.MAIN_STAGE);
        Stage thisStage = (Stage) root.getScene().getWindow();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    stage.show();
                    stage.setOnCloseRequest(evt -> Platform.exit());
                    thisStage.hide();
                });
                timer.cancel();
            }
        }, 500);
    }

    @FXML
    protected void login() throws IOException {
        Task<Void> login = new Task<Void>() {
            boolean check;
            @Override
            protected Void call() throws Exception {
                check = LoginVerifier.verify(username.getText(), password.getText());
                promptMsg.getStyleClass().removeAll("lbl", "lbl-danger", "lbl-success");
                return null;
            }
            @Override
            protected void succeeded() {
                if (check) {
                    promptMsg.getStyleClass().addAll("lbl", "lbl-success");
                    promptMsg.setText(" - Login successful! - ");
                    Session.setUsername(username.getText());
                    try {
                        switchToMainStage();
                    }
                    catch (IOException e) {
                        System.err.println("No way");
                    }
                }
                else {
                    promptMsg.getStyleClass().addAll("lbl", "lbl-danger");
                    promptMsg.setText(" - Login failed! - ");
                }
            }
        };
        new Thread(login).start();
    }

    @FXML
    protected void kbdLogin(KeyEvent event) throws IOException{
        if (event.getCode() == KeyCode.ENTER) {
            login();
        }
    }
}