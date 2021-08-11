package com.example.InventoryManagementSystem;

public class TableItem {
    private String sku, name;
    private Integer qty;
    private String description;

    public TableItem(String sku, String name, Integer qty) {
        this(sku, name, qty, "");
    }
    public TableItem(String sku, String name, Integer qty, String description) {
        this.sku = sku;
        this.name = name;
        this.qty = qty;
        this.description = description;
    }

    public String getSku() {
        return sku;
    }
    public String getName() {
        return name;
    }
    public Integer getQty() {
        return qty;
    }
    public String getDescription() {
        return description;
    }
}
