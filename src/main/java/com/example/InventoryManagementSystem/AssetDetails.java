package com.example.InventoryManagementSystem;

public class AssetDetails {
    protected String manufacturerName, model, partNo, rangeUnit, location, remark;
    protected Integer qty, rangeMin, rangeMax;

    public AssetDetails(String manufacturerName, String model, String partNo, String rangeUnit, String location, String remark, Integer qty, Integer rangeMin, Integer rangeMax) {
        this.manufacturerName = manufacturerName;
        this.model = model;
        this.partNo = partNo;
        this.rangeUnit = rangeUnit;
        this.location = location;
        this.remark = remark;
        this.qty = qty;
        this.rangeMin = rangeMin;
        this.rangeMax = rangeMax;
    }
}
