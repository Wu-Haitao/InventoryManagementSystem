package com.example.InventoryManagementSystem.Controller;

import com.example.InventoryManagementSystem.*;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.io.IOException;

public class EditController {
    public static Asset selectedAsset;

    @FXML
    AnchorPane root;

    @FXML
    Label tagLabel;

    @FXML
    JFXTextArea makeInput, modelInput, partNoInput, locationInput;

    @FXML
    JFXTextField rangeMinInput, rangeMaxInput, rangeUnitInput, qtyInput;

    @FXML
    Button saveBtn, editBtn;

    @FXML
    WebView remarkView;

    private Asset accessory;

    @FXML
    protected void saveEdition() {
        Stage thisStage = (Stage)root.getScene().getWindow();
        Task<Void> save = new Task<Void>() {
            boolean result;
            @Override
            protected Void call() throws Exception {
                accessory = new Asset(selectedAsset.getTag(),
                        new AssetDetails(makeInput.getText(),
                                modelInput.getText(),
                                partNoInput.getText(),
                                rangeUnitInput.getText(),
                                locationInput.getText(),
                                remark,
                                (qtyInput.getText().equals(""))? 0:Integer.parseInt(qtyInput.getText()),
                                (rangeMinInput.getText().equals(""))? 0:Integer.parseInt(rangeMinInput.getText()),
                                (rangeMaxInput.getText().equals(""))? 0:Integer.parseInt(rangeMaxInput.getText())),
                        selectedAsset.getAccessories());
                result = DatabaseHandler.updateAsset(accessory);
                return null;
            }
            @Override
            protected void succeeded() {
                if (result) thisStage.close();
            }
        };
        new Thread(save).start();
    }

    private String remark = "";

    private void setRemark() {
        WebEngine webEngine = remarkView.getEngine();
        webEngine.setUserStyleSheetLocation(MainApplication.class.getResource("webview.css").toExternalForm());
        webEngine.loadContent(remark);
    }

    @FXML
    protected void editRemark() throws IOException {
        Stage stage = StageManager.switchToStage(StageInfo.TEXT_INPUT_STAGE);
        stage.setOnHiding((event) -> {
            remark = TextInputController.text;
            setRemark();
        });
        stage.show();
    }

    private void fillAssetDetails() {
        tagLabel.setText(selectedAsset.getTag());
        makeInput.setText(selectedAsset.getManufacturerName());
        modelInput.setText(selectedAsset.getModel());
        partNoInput.setText(selectedAsset.getPartNo());
        rangeMinInput.setText(Integer.toString(selectedAsset.getRangeMin()));
        rangeMaxInput.setText(Integer.toString(selectedAsset.getRangeMax()));
        rangeUnitInput.setText(selectedAsset.getRangeUnit());
        qtyInput.setText(Integer.toString(selectedAsset.getQty()));
        locationInput.setText(selectedAsset.getLocation());
        remark = selectedAsset.getRemark();
        setRemark();
    }

    private void initNumericInput() {
        qtyInput.setTextFormatter(new TextFormatter<String>(change -> {
            String text = change.getText();
            if (text.matches("[0-9]*")) {
                return change;
            }
            return null;
        }));
        rangeMinInput.setTextFormatter(new TextFormatter<String>(change -> {
            String text = change.getText();
            if (text.matches("[0-9]*")) {
                return change;
            }
            return null;
        }));
        rangeMaxInput.setTextFormatter(new TextFormatter<String>(change -> {
            String text = change.getText();
            if (text.matches("[0-9]*")) {
                return change;
            }
            return null;
        }));
    }

    @FXML
    private void initialize() {
        initNumericInput();
        fillAssetDetails();
    }
}
