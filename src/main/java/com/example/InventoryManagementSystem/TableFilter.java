package com.example.InventoryManagementSystem;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

public class TableFilter {
    private static String tag = "";
    private static Integer qty = null;
    private static String qtyCompare = "";
    private static Asset parentAsset;

    public static ObservableList<Asset> getFilteredResult(ObservableList<Asset> assets) {
        return new FilteredList<>(assets, asset -> {
            boolean flag = asset.getTag().toLowerCase().contains(tag.toLowerCase());
            return switch (qtyCompare) {
                case "More" -> flag && ((qty == null) || (asset.getQty() > qty));
                case "Equal" -> flag && ((qty == null) || asset.getQty().equals(qty));
                case "Less" -> flag && ((qty == null) || (asset.getQty() < qty));
                default -> flag;
            };
        });
    }

    public static void setFilter(String tag, String qty, String qtyCompare, Asset parentAsset) {
        TableFilter.tag = tag;
        if (!qty.isEmpty()) TableFilter.qty = Integer.parseInt(qty);
        else TableFilter.qty = null;
        TableFilter.qtyCompare = qtyCompare;
        TableFilter.parentAsset = parentAsset;
    }

    public static Asset getParentAsset() {
        return parentAsset;
    }
}
