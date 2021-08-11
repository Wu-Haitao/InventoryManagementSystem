package com.example.InventoryManagementSystem;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;

import java.io.IOException;

public class StageManager {
    public static Stage switchToStage(StageInfo stageName) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource(StageInfo.getFXML(stageName)));
        Scene scene = new Scene(fxmlLoader.load(), StageInfo.getHeightAndWidth(stageName)[0], StageInfo.getHeightAndWidth(stageName)[1]);
        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
        Stage stage = new Stage();
        stage.setTitle(StageInfo.getTitle(stageName));
        stage.setScene(scene);
        stage.initStyle(StageInfo.getStyle(stageName));
        stage.setResizable(StageInfo.getResizable(stageName));
        return stage;
    }
}
