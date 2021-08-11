package com.example.InventoryManagementSystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class TableManager {
    private static ObservableList<TableItem> tableItems;

    private static String randomName(Integer length) {
        StringBuilder result = new StringBuilder();
        String s = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        for (int i = 0; i < length; i++) {
            result.append(s.charAt((int) (Math.random() * s.length())));
        }
        return result.toString();
    }

    private static void retrieveTableItemsData() {
        ObservableList<TableItem> items = FXCollections.observableArrayList();
        for (int i=0; i<100; i++) {
            items.add(new TableItem("K00" + Integer.toString(i), randomName((int)(Math.random() * 3 + 5)), (int)(Math.random() * 100), "<h1>Heading</h1><p>this is a sentence which is very very very very long!!!!!!!!!</p>"));
        }
        tableItems = items;
    }

    public static void refreshTable(TableView<TableItem> table) {
        table.getItems().clear();
        table.getItems().addAll(TableFilter.getFilteredResult(tableItems));
        table.refresh();
    }

    public static void initTable(TableView<TableItem> table) {
        TableColumn<TableItem, ?> colSKU = table.getColumns().get(0);
        TableColumn<TableItem, ?> colName = table.getColumns().get(1);
        TableColumn<TableItem, ?> colQty = table.getColumns().get(2);
        colSKU.setCellValueFactory(new PropertyValueFactory<>("sku"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        retrieveTableItemsData();
        refreshTable(table);
    }

    public static int countTableItems(TableView<TableItem> table) {
        return table.getItems().size();
    }

    public static TableItem getCurrentSelectedItem(TableView<TableItem> table) {
        return table.getSelectionModel().getSelectedItem();
    }
}
