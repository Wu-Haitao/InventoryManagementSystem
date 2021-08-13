package com.example.InventoryManagementSystem;

import com.example.InventoryManagementSystem.Controller.AssetDescriptionController;
import com.example.InventoryManagementSystem.Controller.MainController;
import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class TableManager {
    private static ObservableList<Asset> tableItems;

    private static void retrieveTableItemsData() {
        ObservableList<Asset> items = FXCollections.observableArrayList();
        List<Asset> assets = DatabaseHandler.getChildAssets(TableFilter.getParentAsset());
        items.addAll(assets);
        tableItems = items;
    }

    public static void refreshTable(TableView<Asset> table) {
        table.getItems().clear();
        retrieveTableItemsData();
        table.getItems().addAll(TableFilter.getFilteredResult(tableItems));
        table.refresh();
    }

    public static void initTable(TableView<Asset> table,
                                 TableColumn<Asset, String> colTag,
                                 TableColumn<Asset, String> colMake,
                                 TableColumn<Asset, String> colModel,
                                 TableColumn<Asset, String> colPartNo,
                                 TableColumn<Asset, String> colRange,
                                 TableColumn<Asset, Integer> colQty,
                                 TableColumn<Asset, String> colLocation,
                                 TableColumn<Asset, String> colAction) {
        colTag.setCellValueFactory(new PropertyValueFactory<>("tag"));
        colMake.setCellValueFactory(new PropertyValueFactory<>("manufacturerName"));
        colModel.setCellValueFactory(new PropertyValueFactory<>("model"));
        colPartNo.setCellValueFactory(new PropertyValueFactory<>("partNo"));
        colRange.setCellValueFactory(new PropertyValueFactory<>("range"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
        colAction.setCellFactory((col)->{
            TableCell<Asset, String> cell = new TableCell<>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);

                    HBox hbox = new HBox();
                    hbox.setAlignment(Pos.BASELINE_CENTER);
                    hbox.setSpacing(5);

                    JFXButton button1 = new JFXButton();
                    button1.setFocusTraversable(false);
                    FontAwesomeIconView iconView = new FontAwesomeIconView(FontAwesomeIcon.EYE);
                    iconView.setFill(Color.WHITE);
                    button1.setGraphic(iconView);
                    button1.getStyleClass().addAll("btn-xs", "btn-info");
                    button1.setOnAction(actionEvent -> {
                        AssetDescriptionController.selectedAsset = this.getTableRow().getItem();
                        try {
                            Stage stage = StageManager.switchToStage(StageInfo.ASSET_DESCRIPTION_VIEW);
                            stage.show();
                        }
                        catch (IOException e) {
                            System.err.println("No way");
                        }
                    });

                    JFXButton button2 = new JFXButton();
                    button2.setFocusTraversable(false);
                    iconView = new FontAwesomeIconView(FontAwesomeIcon.ANGLE_DOUBLE_RIGHT);
                    iconView.setFill(Color.WHITE);
                    button2.setGraphic(iconView);
                    button2.getStyleClass().addAll("btn-xs", "btn-info");
                    button2.setOnAction(actionEvent -> MainController.getController().reloadWindow(this.getTableRow().getItem()));

                    hbox.getChildren().addAll(button1, button2);

                    if (empty) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        this.setGraphic(hbox);
                    }
                }
            };
            return cell;
        });
        refreshTable(table);
    }
}
