package com.example.InventoryManagementSystem;

import com.example.InventoryManagementSystem.Controller.AssetDescriptionController;
import com.example.InventoryManagementSystem.Controller.EditController;
import com.example.InventoryManagementSystem.Controller.MainController;
import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.control.SelectionMode;
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
        tableItems = TableFilter.getFilteredResult(items);
    }

    public static void refreshTable(TableView<Asset> table) {
        Task<Void> refresh = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                retrieveTableItemsData();
                return null;
            }

            @Override
            protected void succeeded() {
                table.getItems().clear();
                table.getItems().addAll(tableItems);
                table.refresh();
            }
        };
        new Thread(refresh).start();
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
        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
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
                            Stage stage = StageManager.switchToStage(StageInfo.ASSET_DESCRIPTION_STAGE);
                            stage.show();
                        }
                        catch (IOException e) {
                            System.err.println("No way");
                        }
                    });

                    JFXButton button1_5 = new JFXButton();
                    button1_5.setFocusTraversable(false);
                    iconView = new FontAwesomeIconView(FontAwesomeIcon.PAINT_BRUSH);
                    iconView.setFill(Color.WHITE);
                    button1_5.setGraphic(iconView);
                    button1_5.getStyleClass().addAll("btn-xs", "btn-info");
                    button1_5.setOnAction(actionEvent -> {
                        EditController.selectedAsset = this.getTableRow().getItem();
                        try {
                            Stage stage = StageManager.switchToStage(StageInfo.EDIT_STAGE);

                            stage.setOnHiding((event) -> {
                                refreshTable(table);
                            });
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
                    button2.setOnAction(actionEvent -> {
                        History.goForward(this.getTableRow().getItem().getTag());
                        MainController.getController().reloadWindow(this.getTableRow().getItem());
                    });

                    hbox.getChildren().addAll(button1, button1_5, button2);

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
