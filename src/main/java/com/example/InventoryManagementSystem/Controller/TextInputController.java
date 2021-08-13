package com.example.InventoryManagementSystem.Controller;

import javafx.fxml.FXML;
import javafx.scene.web.HTMLEditor;
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
}
