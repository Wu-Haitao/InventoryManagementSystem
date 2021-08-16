package com.example.InventoryManagementSystem.Controller;

import com.example.InventoryManagementSystem.MainApplication;
import javafx.fxml.FXML;
import javafx.scene.web.HTMLEditor;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class TextInputController {
    public static String text;

    @FXML
    private HTMLEditor editor;

    @FXML
    protected void saveAndExit() {
        text = editor.getHtmlText();
        Stage thisStage = (Stage)editor.getScene().getWindow();
        thisStage.close();
    }

    @FXML
    public void initialize() {
        WebView webView = (WebView) (editor.lookup("WebView"));
        WebEngine webEngine = webView.getEngine();
        webEngine.setUserStyleSheetLocation(MainApplication.class.getResource("webview.css").toExternalForm());
    }
}
