package com.example.InventoryManagementSystem.Controller;

import com.example.InventoryManagementSystem.*;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.io.IOException;

public class AddController {
    public static Asset selectedAsset;

    @FXML
    AnchorPane root;

    @FXML
    JFXTextArea tagInput, makeInput, modelInput, partNoInput, locationInput;

    @FXML
    JFXTextField rangeMinInput, rangeMaxInput, rangeUnitInput, qtyInput;

    @FXML
    Button saveBtn, editBtn;

    @FXML
    WebView remarkView;

    private Asset accessory;

    @FXML
    protected void saveAddition() {
        Stage thisStage = (Stage)root.getScene().getWindow();
        Task<Void> save = new Task<Void>() {
            boolean result;
            @Override
            protected Void call() throws Exception {
                accessory = new Asset(tagInput.getText(),
                        new AssetDetails(makeInput.getText(),
                                modelInput.getText(),
                                partNoInput.getText(),
                                rangeUnitInput.getText(),
                                locationInput.getText(),
                                remark,
                                (qtyInput.getText().equals(""))? 0:Integer.parseInt(qtyInput.getText()),
                                (rangeMinInput.getText().equals(""))? 0:Integer.parseInt(rangeMinInput.getText()),
                                (rangeMaxInput.getText().equals(""))? 0:Integer.parseInt(rangeMaxInput.getText())),
                        selectedAsset.getTag());
                result = DatabaseHandler.addAsset(accessory);
                return null;
            }
            @Override
            protected void succeeded() {
                if (result) thisStage.close();
                else tagInput.setText("Repeated asset tags not allowed");
            }
        };
        new Thread(save).start();
    }

    private String remark = "";

    private void setRemark() {
        WebEngine webEngine = remarkView.getEngine();
        webEngine.loadContent(remark);
    }

    @FXML
    protected void editRemark() throws IOException  {
        Stage stage = StageManager.switchToStage(StageInfo.TEXT_INPUT_STAGE);
        stage.setOnHiding((event) -> {
            remark = TextInputController.text;
            setRemark();
        });
       stage.show();
    }
}
