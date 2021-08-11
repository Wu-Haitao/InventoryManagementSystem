package com.example.InventoryManagementSystem;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

public class TableFilter {
    private static String sku = "", name = "";
    private static Integer qty = null;
    private static String qtyCompare = "";

    public static ObservableList<TableItem> getFilteredResult(ObservableList<TableItem> items) {
        return new FilteredList<>(items, tableItem -> {
            boolean flag = (tableItem.getSku().toLowerCase().contains(sku.toLowerCase()))
                    && (tableItem.getName().toLowerCase().contains(name.toLowerCase()));
            return switch (qtyCompare) {
                case "More" -> flag && ((qty == null) || (tableItem.getQty() > qty));
                case "Equal" -> flag && ((qty == null) || tableItem.getQty().equals(qty));
                case "Less" -> flag && ((qty == null) || (tableItem.getQty() < qty));
                default -> flag;
            };
        });
    }

    public static void setFilter(String sku, String name, String qty, String qtyCompare) {
        TableFilter.sku = sku;
        TableFilter.name = name;
        if (!qty.isEmpty()) TableFilter.qty = Integer.parseInt(qty);
        else TableFilter.qty = null;
        TableFilter.qtyCompare = qtyCompare;
    }
}
