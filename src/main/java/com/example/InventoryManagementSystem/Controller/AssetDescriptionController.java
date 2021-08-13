package com.example.InventoryManagementSystem.Controller;

import com.example.InventoryManagementSystem.Asset;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class AssetDescriptionController {
    public static Asset selectedAsset;

    @FXML
    public Label tagLabel, makeLabel, modelLabel, partNoLabel, rangeLabel, qtyLabel, locationLabel;

    @FXML
    private WebView remarkLabel;

    private void setDescriptionScene() {
        tagLabel.setText(selectedAsset.getTag());
        makeLabel.setText(selectedAsset.getManufacturerName());
        modelLabel.setText(selectedAsset.getModel());
        partNoLabel.setText(selectedAsset.getPartNo());
        rangeLabel.setText(selectedAsset.getRange());
        qtyLabel.setText(Integer.toString(selectedAsset.getQty()));
        locationLabel.setText(selectedAsset.getLocation());
        WebEngine webEngine = remarkLabel.getEngine();
        webEngine.loadContent(selectedAsset.getRemark());
    }

    @FXML
    public void initialize() {
        setDescriptionScene();
    }
}
