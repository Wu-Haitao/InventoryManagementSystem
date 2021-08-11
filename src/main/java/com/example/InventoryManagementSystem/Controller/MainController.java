package com.example.InventoryManagementSystem.Controller;

import com.example.InventoryManagementSystem.*;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {

    @FXML
    private AnchorPane root;

    @FXML
    private Label username_label;

    @FXML
    private Button btn_logout;

    @FXML
    private Button btn_update;

    @FXML
    private TextField sku_input, name_input, qty_input;

    @FXML
    private TableView<TableItem> table;

    @FXML
    Label item_count;

    @FXML
    Label sku_text, name_text, qty_text;

    @FXML
    WebView description_text;


    private void switchToLoginStage() throws IOException {
        Stage stage = StageManager.switchToStage(StageInfo.LOGIN_STAGE);
        Stage thisStage = (Stage) root.getScene().getWindow();
        stage.show();
        thisStage.close();
    }

    @FXML
    protected void setBtn_logout() throws IOException {
        switchToLoginStage();
    }

    @FXML
    protected void cancelFocus() {
        root.requestFocus();
    }

    /* Filter */
    private String radioBtnSelect = "More";
    @FXML
    protected void radioBtnSelect(Event event) {
        radioBtnSelect = ((RadioButton)event.getSource()).getText();
        handleFilterChange();
    }

    /*@FXML
    protected void clearRadioBtnSelect(Event event) {
        radioBtnSelect = "";
    }*/

    @FXML
    protected void handleFilterChange() {
        TableFilter.setFilter(sku_input.getText(), name_input.getText(), qty_input.getText(), radioBtnSelect);
        TableManager.refreshTable(table);
    }
    /*--------------------*/


    /* Functions and styles of close button and minimize button */
    @FXML
    protected void exitApplication() {
        Platform.exit();
    }

    @FXML
    protected void minimizeApplication(Event event) {
        Stage thisStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        thisStage.setIconified(true);
    }

    @FXML
    protected void buttonHoverEnter(MouseEvent event) {
        Button thisButton = (Button) event.getSource();
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(-0.1);
        thisButton.setEffect(colorAdjust);
    }

    @FXML
    protected void buttonHoverExit(MouseEvent event) {
        Button thisButton = (Button) event.getSource();
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(0);
        thisButton.setEffect(colorAdjust);
    }
    /*--------------------*/

    @FXML
    protected void setBtn_update() {
        for (int i=0; i<100; i++) {
            table.getItems().add(new TableItem("K00" + Integer.toString(i), "Nothing", i));
        }
        table.refresh();
    }


    /* Make the window draggable */
    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    protected void mousePressedHandle(MouseEvent event) {
        Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        xOffset = primaryStage.getX() - event.getScreenX();
        yOffset = primaryStage.getY() - event.getScreenY();
    }

    @FXML
    protected void mouseDraggedHandle(MouseEvent event) {
        Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        primaryStage.setX(event.getScreenX() + xOffset);
        primaryStage.setY(event.getScreenY() + yOffset);
    }
    /*--------------------*/

    /* Initialization */
    private void initUsername() {
        username_label.setText(Session.getUsername());
    }

    private void initTable() {
        TableManager.initTable(table);
        table.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                sku_text.setText(newValue.getSku());
                name_text.setText(newValue.getName());
                qty_text.setText(Integer.toString(newValue.getQty()));
                WebEngine webEngine = description_text.getEngine();
                webEngine.loadContent(newValue.getDescription());
            }
        });
        table.getItems().addListener((ListChangeListener<TableItem>) observable -> {
            item_count.setText(Integer.toString(observable.getList().size()));
        });
    }

    private void initQtyInput() {
        qty_input.setTextFormatter(new TextFormatter<String>(change -> {
            String text = change.getText();
            if (text.matches("[0-9]*")) {
                return change;
            }
            return null;
        }));
    }

    @FXML
    public void initialize() {
        initUsername();
        initTable();
        initQtyInput();
    }
    /*--------------------*/
}
