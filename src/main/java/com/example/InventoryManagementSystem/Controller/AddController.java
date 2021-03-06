package com.example.InventoryManagementSystem.Controller;

import com.example.InventoryManagementSystem.*;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.concurrent.Task;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AddController {
    public static Asset selectedAsset;
    public static Asset copiedAsset = null;

    @FXML
    AnchorPane root;

    @FXML
    Pane loadingPane;

    @FXML
    JFXTextArea tagInput, makeInput, modelInput, partNoInput, locationInput;

    @FXML
    JFXTextField rangeMinInput, rangeMaxInput, rangeUnitInput, qtyInput;

    @FXML
    Button saveBtn, editBtn;

    @FXML
    WebView remarkView;

    @FXML
    JFXCheckBox copyCheck;

    @FXML
    Label copyAssetLabel;

    @FXML
    protected void cancelFocus() {
        root.requestFocus();
    }

    @FXML
    protected void saveAddition() {
        Stage thisStage = (Stage)root.getScene().getWindow();
        thisStage.setOnCloseRequest(Event::consume); //Disable close button
        loadingPane.setVisible(true);
        Task<Void> save = new Task<Void>() {
            boolean result = true;
            @Override
            protected Void call() throws Exception {
                boolean applyCopy = (copyCheck.isSelected() && (copiedAsset != null));
                AssetDetails details = new AssetDetails(makeInput.getText(),
                        modelInput.getText(),
                        partNoInput.getText(),
                        rangeUnitInput.getText(),
                        locationInput.getText(),
                        remark,
                        (qtyInput.getText().equals(""))? 0:Integer.parseInt(qtyInput.getText()),
                        (rangeMinInput.getText().equals(""))? 0:Integer.parseInt(rangeMinInput.getText()),
                        (rangeMaxInput.getText().equals(""))? 0:Integer.parseInt(rangeMaxInput.getText()));
                String tagInputStrs = tagInput.getText();
                List<Asset> accessories = Arrays.stream(tagInputStrs.split("\n")).map(tagInputStr -> new Asset(tagInputStr, details)).collect(Collectors.toList());
                for (Asset accessory:
                     accessories) {
                    if ((selectedAsset.getTag().equals(accessory.getTag())) || DatabaseHandler.checkRelation(selectedAsset.getTag(), accessory.getTag())) {
                        //The accessory is already a child of current asset
                        result = false; //Addition canceled
                        return null;
                    }
                }
                for (Asset accessory:
                     accessories) {
                    DatabaseHandler.addAccessory(accessory);
                    DatabaseHandler.addAccessoriesRelation(selectedAsset.getTag(), accessory.getTag());
                    if (applyCopy) DatabaseHandler.copyAccessoriesRelation(copiedAsset.getTag(), accessory.getTag());
                }
                return null;
            }
            @Override
            protected void succeeded() {
                thisStage.setOnCloseRequest(null);
                if (result) thisStage.close();
                else {
                    tagInput.setText("Operation denied");
                    loadingPane.setVisible(false);
                }
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
    protected void editRemark() throws IOException  {
        Stage stage = StageManager.switchToStage(StageInfo.TEXT_INPUT_STAGE);
        stage.setOnHiding((event) -> {
            remark = TextInputController.text;
            setRemark();
        });
       stage.show();
    }

    private void initCopyAsset() {
        Task<Void> initCopyAsset = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                if (!DatabaseHandler.findInDatabase(copiedAsset.getTag())) copiedAsset = null;
                return null;
            }
            @Override
            protected void succeeded() {
                if (copiedAsset != null) {
                    copyAssetLabel.setText(copiedAsset.getTag());
                }
                else {
                    copyAssetLabel.setText("None");
                }
            }
        };
        new Thread(initCopyAsset).start();
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
        initCopyAsset();
        initNumericInput();
    }
}
