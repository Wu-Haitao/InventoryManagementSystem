package com.example.InventoryManagementSystem;

public class Asset {
    public static Asset rootAsset = new Asset("root", new AssetDetails("root","root","root","","root","<p>root</p>",0,0,0), "");

    private String tag;
    private AssetDetails details;
    private String parentTag;

    public Asset(String tag, AssetDetails details, String parentTag) {
        this.tag = tag;
        this.details = details;
        this.parentTag = parentTag;
    }

    public String getTag() {
        return tag;
    }

    public String getParentTag() {
        return parentTag;
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
