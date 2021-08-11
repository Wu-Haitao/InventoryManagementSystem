package com.example.InventoryManagementSystem;

import java.util.List;

public class Asset {
    public static boolean findInDatabase(String tag) {
        //find in database;
        return false;
    }
    public static List<Asset> getAllAssets() {
        return null;
    }

    private String tag, manufacturerName, partNo, rangeUnit, location, remark;
    private Integer qty, rangeMin, rangeMax;
    private List<Integer> accessoryList;
    private boolean inDatabase;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getManufacturerName() {
        return manufacturerName;
    }

    public void setManufacturerName(String manufacturerName) {
        this.manufacturerName = manufacturerName;
    }

    public String getPartNo() {
        return partNo;
    }

    public void setPartNo(String partNo) {
        this.partNo = partNo;
    }

    public String getRangeUnit() {
        return rangeUnit;
    }

    public void setRangeUnit(String rangeUnit) {
        this.rangeUnit = rangeUnit;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public Integer getRangeMin() {
        return rangeMin;
    }

    public void setRangeMin(Integer rangeMin) {
        this.rangeMin = rangeMin;
    }

    public Integer getRangeMax() {
        return rangeMax;
    }

    public void setRangeMax(Integer rangeMax) {
        this.rangeMax = rangeMax;
    }

    public List<Integer> getAccessoryList() {
        return accessoryList;
    }

    public void setAccessoryList(List<Integer> accessoryList) {
        this.accessoryList = accessoryList;
    }

    public boolean isInDatabase() {
        return inDatabase;
    }

    public void setInDatabase(boolean inDatabase) {
        this.inDatabase = inDatabase;
    }



    public Asset(String tag, String manufacturerName, String partNo, Integer rangeMin, Integer rangeMax, String rangeUnit, Integer qty, String location, String remark) {
        this.tag = tag;
        this.manufacturerName = manufacturerName;
        this.partNo = partNo;
        this.rangeMin = rangeMin;
        this.rangeMax = rangeMax;
        this.rangeUnit = rangeUnit;
        this.qty = qty;
        this.location = location;
        this.remark = remark;
    }

    public void applyToDatabase() {
        if (Asset.findInDatabase(this.tag)) {
            //if in database
            return;
        }
        else {
            //if not in database
            return;
        }
    }
}
