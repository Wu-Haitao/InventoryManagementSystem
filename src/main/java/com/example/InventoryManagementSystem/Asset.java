package com.example.InventoryManagementSystem;

import java.util.List;

public class Asset {
    private String tag;
    private AssetDetails details;

    public Asset(String tag, AssetDetails details) {
        this.tag = tag;
        this.details = details;
    }

    public String getTag() {
        return tag;
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
