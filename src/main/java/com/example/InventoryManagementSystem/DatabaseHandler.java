package com.example.InventoryManagementSystem;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler {
    private static Connection connection = null;
    public static boolean init = false;

    public static boolean checkDrivers() {
        try {
            Class.forName("org.sqlite.JDBC");
            DriverManager.registerDriver(new org.sqlite.JDBC());
            return true;
        } catch (ClassNotFoundException | SQLException exception) {
            System.err.println(exception.getMessage());
            return false;
        }
    }
    public static void connectDatabase(String address) {
        String filePath = address + "/inventory.db";
        File dir = new File(address);
        if (!dir.exists()) dir.mkdirs();

        File file = new File(filePath);
        try {
            init = file.createNewFile();
        }
        catch (IOException e) {
            System.err.println(e.getMessage());
        }


        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + filePath);
        }
        catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        System.out.println("Connection established!");
    }

    public static void initDatabase () {
        try {
            Statement statement = connection.createStatement();
            String sql = "CREATE TABLE ASSETS" +
                    "(TAG         TEXT PRIMARY KEY," +
                    " MAKE        TEXT," +
                    " MODEL        TEXT," +
                    " PARTNO       TEXT," +
                    " RANGEMIN     INT," +
                    " RANGEMAX     INT," +
                    " RANGEUNIT    TEXT," +
                    " QTY          INT," +
                    " LOCATION     TEXT," +
                    " REMARK       TEXT);";
            statement.executeUpdate(sql);
            sql = "INSERT INTO ASSETS VALUES" +
                    "('root','','','',0,0,'',0,'','');";
            statement.executeUpdate(sql);
            sql = "CREATE TABLE ACCESSORIES" +
                    "(PARENTTAG    TEXT," +
                    " CHILDTAG     TEXT," +
                    " CONSTRAINT ACCESSORIES_PK PRIMARY KEY (PARENTTAG, CHILDTAG)," +
                    " CONSTRAINT ACCESSORIES_FK FOREIGN KEY (PARENTTAG, CHILDTAG) REFERENCES ASSETS(TAG, TAG));";
            statement.executeUpdate(sql);
            statement.close();
        }
        catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public static void closeDatabase () {
        try {
            connection.close();
            System.out.println("Database closed");
        }
        catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public static ResultSet execCommandQuery(String sql) throws SQLException {
        Statement statement = connection.createStatement();
        return statement.executeQuery(sql);
    }

    public static void execCommandUpdate(String sql) throws SQLException {
        Statement statement = connection.createStatement();
        statement.executeUpdate(sql);
        statement.close();
    }

    public static Asset getRoot() {
        return getAssetWithTag("root");
    }

    public static Boolean findInDatabase(String tag) {
        try {
            ResultSet rs = execCommandQuery(String.format("SELECT * FROM ASSETS WHERE TAG=='%s';", tag));
            return (rs.next());
        }
        catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    private static Asset getAssetFromResultSet(ResultSet rs) throws SQLException {
        AssetDetails details = new AssetDetails(rs.getString("MAKE"),
                rs.getString("MODEL"),
                rs.getString("PARTNO"),
                rs.getString("RANGEUNIT"),
                rs.getString("LOCATION"),
                rs.getString("REMARK").replace('\"', '\''),
                rs.getInt("QTY"),
                rs.getInt("RANGEMIN"),
                rs.getInt("RANGEMAX"));
        return new Asset(rs.getString("TAG"), details);
    }

    public static Asset getAssetWithTag(String tag) {
        Asset asset = null;
        try {
            ResultSet rs = execCommandQuery(String.format("SELECT * FROM ASSETS WHERE TAG=='%s';", tag));
            asset = getAssetFromResultSet(rs);
        }
        catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return asset;
    }

    public static List<Asset> getChildAssets(Asset parentAsset) {
        List<Asset> assets = new ArrayList<>();
        try {
            ResultSet rs = execCommandQuery(String.format("SELECT * FROM ACCESSORIES WHERE PARENTTAG=='%s';", parentAsset.getTag()));
            while (rs.next()) {
                assets.add(getAssetWithTag(rs.getString("CHILDTAG")));
            }
        }
        catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return assets;
    }

    public static boolean checkRelation(String parentTag, String childTag) {
        try {
            ResultSet rs = execCommandQuery(String.format("SELECT * FROM ACCESSORIES WHERE PARENTTAG=='%s' AND CHILDTAG=='%s';", parentTag, childTag));
            return !rs.isClosed();
        }
        catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    public static void addAccessoriesRelation(String parentTag, String childTag) {
        try {
            execCommandUpdate(String.format("INSERT INTO ACCESSORIES VALUES('%s', '%s');", parentTag, childTag));
        }
        catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public static void copyAccessoriesRelation(String copiedAssetTag, String newAssetTag) {
        try {
            ResultSet rs1 = execCommandQuery(String.format("SELECT * FROM ACCESSORIES WHERE PARENTTAG=='%s';", copiedAssetTag));
            while (rs1.next()) {
                String childTag = rs1.getString("CHILDTAG");
                addAccessoriesRelation(newAssetTag, childTag);
            }
        }
        catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public static void addAccessory(Asset asset) {
        try {
            execCommandUpdate(String.format("INSERT INTO ASSETS VALUES ('%s','%s','%s','%s',%d,%d,'%s',%d,'%s','%s');",
                    asset.getTag(),
                    asset.getManufacturerName(),
                    asset.getModel(),
                    asset.getPartNo(),
                    asset.getRangeMin(),
                    asset.getRangeMax(),
                    asset.getRangeUnit(),
                    asset.getQty(),
                    asset.getLocation(),
                    asset.getRemark().replace('\'', '\"')));
        }
        catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public static boolean deleteAccessory(String parentAssetTag, String childAssetTag) {
        try {
            execCommandUpdate(String.format("DELETE FROM ACCESSORIES WHERE PARENTTAG=='%s' AND CHILDTAG=='%s';", parentAssetTag, childAssetTag));
            if (!execCommandQuery(String.format("SELECT * FROM ACCESSORIES WHERE CHILDTAG=='%s';", childAssetTag)).next()) {
                //This accessory doesn't belong to any asset
                execCommandUpdate(String.format("DELETE FROM ASSETS WHERE TAG=='%s';", childAssetTag)); //Delete from asset table
                ResultSet rs = execCommandQuery(String.format("SELECT * FROM ACCESSORIES WHERE PARENTTAG=='%s';", childAssetTag));
                while (rs.next()) {
                    String childTag = rs.getString("CHILDTAG");
                    deleteAccessory(childAssetTag, childTag);
                }
            }

        }
        catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
        return true;
    }

    public static boolean updateAsset(Asset asset) {
        try {
            execCommandUpdate(String.format("UPDATE ASSETS SET MAKE='%s',MODEL='%s',PARTNO='%s',RANGEMIN=%d,RANGEMAX=%d,RANGEUNIT='%s',QTY=%d,LOCATION='%s',REMARK='%s' WHERE TAG=='%s';",
                    asset.getManufacturerName(),
                    asset.getModel(),
                    asset.getPartNo(),
                    asset.getRangeMin(),
                    asset.getRangeMax(),
                    asset.getRangeUnit(),
                    asset.getQty(),
                    asset.getLocation(),
                    asset.getRemark().replace('\'', '\"'),
                    asset.getTag()));
        }
        catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
        return true;
    }
}
