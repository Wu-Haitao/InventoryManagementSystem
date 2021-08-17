package com.example.InventoryManagementSystem;

import java.util.List;

public class Asset {
    private String tag;
    private AssetDetails details;
    private List<String> accessories;

    public Asset(String tag, AssetDetails details, List<String> accessories) {
        this.tag = tag;
        this.details = details;
        this.accessories = accessories;
    }

    public String getTag() {
        return tag;
    }

    public List<String> getAccessories() {
        return accessories;
    }

    public AssetDetails getDetails() {
        return details;
    }

    public String getManufacturerName() {
        return details.manufacturerName;
    }

    public String getModel() {
        return details.model;
    }

    public String getPartNo() {
        return details.partNo;
    }

    public String getLocation() {
        return details.location;
    }

    public String getRemark() {
        return details.remark;
    }

    public Integer getRangeMin() {
        return details.rangeMin;
    }

    public Integer getRangeMax() {
        return details.rangeMax;
    }

    public String getRangeUnit() {
        return details.rangeUnit;
    }

    public String getRange() {
        return String.format("%d - %d %s", details.rangeMin, details.rangeMax, details.rangeUnit);
    }

    public Integer getQty() {
        return details.qty;
    }
}
