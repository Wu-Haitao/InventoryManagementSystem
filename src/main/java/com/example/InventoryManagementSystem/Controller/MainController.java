package com.example.InventoryManagementSystem.Controller;

import com.example.InventoryManagementSystem.*;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {
    private Asset parentAsset;

    @FXML
    private AnchorPane root;

    @FXML
    private Pane loadingPane;

    @FXML
    private Pane maskPane;

    @FXML
    private Label username_label;

    @FXML
    private JFXTextField tag_input, qty_input;

    @FXML
    private Button btn_add, btn_delete, btn_edit;

    @FXML
    private Label tagLabel, makeLabel, modelLabel, partNoLabel, rangeLabel, qtyLabel, locationLabel;

    @FXML
    private WebView remarkLabel;

    @FXML
    private TableView<Asset> table;

    @FXML
    private TableColumn<Asset, String> colTag, colMake, colModel, colPartNo, colRange, colLocation, colAction;

    @FXML
    private TableColumn<Asset, Integer> colQty;

    @FXML
    Label item_count;

    @FXML
    JFXToggleButton modeToggle;

    private static MainController controller;

    public static void setController(MainController controller) {
        MainController.controller = controller;
    }

    public static MainController getController() {
        return controller;
    }

    /* Filter */
    private String radioBtnSelect = "More";
    @FXML
    protected void radioBtnSelect(Event event) {
        radioBtnSelect = ((RadioButton)event.getSource()).getText();
        handleFilterChange();
    }

    @FXML
    protected void handleFilterChange() {
        TableFilter.setFilter(tag_input.getText(), qty_input.getText(), radioBtnSelect, parentAsset);
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
        ColorAdjust originAdjust = (ColorAdjust) thisButton.getEffect();
        ColorAdjust colorAdjust = new ColorAdjust();
        if (originAdjust == null) {
            colorAdjust.setBrightness(-0.2);
        }
        else {
            colorAdjust.setBrightness(originAdjust.getBrightness() - 0.2);
        }
        thisButton.setEffect(colorAdjust);
    }

    @FXML
    protected void buttonHoverExit(MouseEvent event) {
        Button thisButton = (Button) event.getSource();
        ColorAdjust originAdjust = (ColorAdjust) thisButton.getEffect();
        ColorAdjust colorAdjust = new ColorAdjust();
        if (originAdjust == null) {
            colorAdjust.setBrightness(0);
        }
        else {
            colorAdjust.setBrightness(originAdjust.getBrightness() + 0.2);
        }
        thisButton.setEffect(colorAdjust);
    }
    /*--------------------*/

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

    private void switchToLoginStage() throws IOException {
        Stage stage = StageManager.switchToStage(StageInfo.LOGIN_STAGE);
        Stage thisStage = (Stage) root.getScene().getWindow();
        stage.show();
        thisStage.close();
    }

    @FXML
    private void switchToAddStage() throws IOException {
        AddController.selectedAsset = parentAsset;
        Stage stage = StageManager.switchToStage(StageInfo.ADD_STAGE);
        stage.setOnHiding((event) -> {
            TableManager.refreshTable(table);
        });
        stage.show();
    }

    @FXML
    private void switchToEditStage() throws IOException {
        EditController.selectedAsset = parentAsset;
        Stage stage = StageManager.switchToStage(StageInfo.EDIT_STAGE);
        stage.setOnHiding((event) -> {
            TableManager.refreshTable(table);
            refreshParentAsset();
        });
        stage.show();
    }

    @FXML
    protected void logout() throws IOException {
        switchToLoginStage();
    }

    @FXML
    protected void cancelFocus() {
        root.requestFocus();
    }

    @FXML
    protected void backToParent() {
        Task<Void> goBack = new Task<Void>() {
            Asset newParentAsset;
            @Override
            protected Void call() throws Exception {
                newParentAsset = DatabaseHandler.getAssetWithTag(History.goBackward());
                return null;
            }
            @Override
            protected void succeeded() {
                reloadWindow(newParentAsset);
            }
        };
        new Thread(goBack).start();
    }

    @FXML
    protected void changeShowMode() {
        TableManager.setShowAllMode(modeToggle.isSelected());
        TableManager.refreshTable(table);
    }

    public void reloadWindow(Asset newParentAsset) {
        parentAsset = newParentAsset;
        setDescriptionPanel(parentAsset);
        tag_input.setText("");
        qty_input.setText("");
        handleFilterChange();
    }

    @FXML
    protected void deleteAccessory() {
        ObservableList<Asset> selectAssets = table.getSelectionModel().getSelectedItems();
        if (selectAssets.isEmpty()) {
            Alert selectAlert = new Alert(Alert.AlertType.INFORMATION);
            selectAlert.setHeaderText("Select at least one accessory to continue");
            selectAlert.showAndWait();
            return;
        }
        Alert deleteAlert = new Alert(Alert.AlertType.CONFIRMATION);
        deleteAlert.setHeaderText("Are you sure?");
        deleteAlert.setContentText("Child accessories may also be deleted");
        deleteAlert.showAndWait();
        if (deleteAlert.getResult() == ButtonType.OK) {
            Task<Void> delete = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    for (Asset selectAsset:
                         selectAssets) {
                        if (modeToggle.isSelected()) {
                            DatabaseHandler.deleteAsset(selectAsset.getTag());
                        }
                        else {
                            DatabaseHandler.deleteAccessory(parentAsset.getTag(), selectAsset.getTag());
                        }
                    }
                    return null;
                }
                @Override
                protected void succeeded() {
                    TableManager.refreshTable(table);
                    loadingPane.setVisible(false);
                }
            };
            loadingPane.setVisible(true);
            new Thread(delete).start();
        }
    }

    @FXML
    protected void copyAccessoriesList() {
        AddController.copiedAsset = parentAsset;
    }

    @FXML
    protected void switchToDescriptionStage() throws IOException {
        AssetDescriptionController.selectedAsset = parentAsset;
        Stage stage = StageManager.switchToStage(StageInfo.ASSET_DESCRIPTION_STAGE);
        stage.show();
    }

    private void refreshParentAsset() { //This is called when current asset is modified
        Task<Void> refresh = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                parentAsset = DatabaseHandler.getAssetWithTag(parentAsset.getTag());
                return null;
            }
            @Override
            protected void succeeded() {
                setDescriptionPanel(parentAsset);
            }
        };
        new Thread(refresh).start();
    }

    private void setDescriptionPanel(Asset asset) {
        tagLabel.setText(asset.getTag());
        makeLabel.setText(asset.getManufacturerName());
        modelLabel.setText(asset.getModel());
        partNoLabel.setText(asset.getPartNo());
        rangeLabel.setText(asset.getRange());
        qtyLabel.setText(Integer.toString(asset.getQty()));
        locationLabel.setText(asset.getLocation());
        WebEngine webEngine = remarkLabel.getEngine();
        webEngine.setUserStyleSheetLocation(MainApplication.class.getResource("webview-noscroll.css").toExternalForm());
        webEngine.loadContent(asset.getRemark());
        maskPane.setVisible(parentAsset.getTag().equals("root"));
        btn_edit.setDisable(parentAsset.getTag().equals("root"));
    }

    /* More functions */
    @FXML
    protected void openLogDir() {
        MyLogger.openLogDir();
    }

    @FXML
    protected void backupDatabase() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        DatabaseHandler.backupDatabase(directoryChooser.showDialog(((Node) root).getScene().getWindow()));
    }

    /* Initialization */
    private void initDescriptionPanel() {
        setDescriptionPanel(parentAsset);
    }

    private void initUsername() {
        username_label.setText(Session.getUsername());
    }

    private void initTable() {
        TableManager.initTable(table, colTag, colMake, colModel, colPartNo, colRange, colQty, colLocation, colAction);
        item_count.setText(Integer.toString(table.getItems().size()));
        table.getItems().addListener((ListChangeListener<Asset>) observable -> {
            item_count.setText(Integer.toString(observable.getList().size()));
        });
    }

    private void initFilter() {
        tag_input.setText("");
        qty_input.setText("");
        TableFilter.setFilter(tag_input.getText(), qty_input.getText(), "More", parentAsset);
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

    private void initHistory() {
        History.goForward(parentAsset.getTag());
    }

    private void initRoot() {
        Task<Void> init = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                parentAsset = DatabaseHandler.getRoot();
                return null;
            }
            @Override
            protected void succeeded() {
                initFilter();
                initTable();
                initDescriptionPanel();
                initHistory();
            }
        };
        new Thread(init).start();
    }

    @FXML
    public void initialize() {
        initUsername();
        initQtyInput();
        initRoot();
    }
    /*--------------------*/
}
