package com.example.InventoryManagementSystem;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

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

    private static Path getDatabasePath() {
        return Path.of(System.getProperty("user.dir"), "AppData", "inventory.db");
    }

    public static void backupDatabase(File dest) {
        try {
            Files.copy(getDatabasePath(), dest.toPath().resolve(Path.of("inventory.db")), StandardCopyOption.REPLACE_EXISTING);
            Desktop.getDesktop().open(dest);
        }
        catch (Exception e) {
            MyLogger.logErr(String.format("Failed to backup database - %s", e.getMessage()));
        }
    }

    public static void exportToJson(File dest) {
        try {
            List<Asset> items = getAllAssets();
            JSONArray itemList = new JSONArray();
            for (Asset item:
                 items) {
                JSONObject currentItem = new JSONObject();
                currentItem.put("tag", item.getTag());
                currentItem.put("make", item.getManufacturerName());
                currentItem.put("model", item.getModel());
                currentItem.put("partNo", item.getPartNo());
                currentItem.put("range", item.getRange());
                currentItem.put("qty", item.getQty());
                currentItem.put("location", item.getLocation());
                itemList.add(currentItem);
            }
            FileWriter file = new FileWriter(new File(dest, "inventory.json"));
            file.write(itemList.toJSONString());
            file.flush();
            Desktop.getDesktop().open(dest);
        }
        catch (Exception e) {
            MyLogger.logErr(String.format("Failed to export database - %s", e.getMessage()));
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
            sql = "CREATE TABLE USER" +
                    "(USERNAME    TEXT PRIMARY KEY," +
                    " PASSWORD    TEXT);";
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

    public static void registerUser(String username, String password) {
        try {
            execCommandUpdate(String.format("INSERT INTO USER VALUES('%s', '%s');", username, password));
        }
        catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public static boolean checkUser(String username, String password) {
        try {
            ResultSet rs = execCommandQuery(String.format("SELECT * FROM USER WHERE USERNAME=='%s' AND PASSWORD=='%s'", username, password));
            return (!rs.isClosed());
        }
        catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    public static boolean findInDatabase(String tag) {
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
            rs.next();
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

    public static List<Asset> getAllAssets() {
        List<Asset> assets = new ArrayList<>();
        try {
            ResultSet rs = execCommandQuery("SELECT * FROM ASSETS WHERE TAG!='root';");
            while (rs.next()) {
                assets.add(getAssetFromResultSet(rs));
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
            MyLogger.logErr(String.format("Failed to add %s as the accessory of %s - %s", childTag, parentTag, e.getMessage()));
            return;
        }
        MyLogger.logInfo(String.format("Succeeded to add %s as the accessory of %s", childTag, parentTag));
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
            MyLogger.logErr(String.format("Failed to add asset %s - %s", asset.getTag(), e.getMessage()));
            return;
        }
        MyLogger.logInfo(String.format("Succeeded to add asset %s", asset.getTag()));
    }

    public static boolean deleteAccessory(String parentAssetTag, String childAssetTag) {
        try {
            execCommandUpdate(String.format("DELETE FROM ACCESSORIES WHERE PARENTTAG=='%s' AND CHILDTAG=='%s';", parentAssetTag, childAssetTag));
            if (!execCommandQuery(String.format("SELECT * FROM ACCESSORIES WHERE CHILDTAG=='%s';", childAssetTag)).next()) {
                //This accessory doesn't belong to any asset
                try {
                    execCommandUpdate(String.format("DELETE FROM ASSETS WHERE TAG=='%s';", childAssetTag)); //Delete from asset table

                    ResultSet rs = execCommandQuery(String.format("SELECT * FROM ACCESSORIES WHERE PARENTTAG=='%s';", childAssetTag));
                    while (rs.next()) {
                        String childTag = rs.getString("CHILDTAG");
                        deleteAccessory(childAssetTag, childTag);
                    }

                    MyLogger.logInfo(String.format("Succeeded to delete asset %s", childAssetTag));
                }
                catch (SQLException e) {
                    System.err.println(e.getMessage());
                    MyLogger.logErr(String.format("Failed to delete asset %s - %s", childAssetTag, e.getMessage()));
                    throw e;
                }
            }
        }
        catch (SQLException e) {
            System.err.println(e.getMessage());
            MyLogger.logErr(String.format("Failed to delete %s as the accessory of %s - %s", childAssetTag, parentAssetTag, e.getMessage()));
            return false;
        }
        MyLogger.logInfo(String.format("Succeeded to delete %s as the accessory of %s", childAssetTag, parentAssetTag));
        return true;
    }

    public static boolean deleteAsset(String assetTag) {
        try {
            execCommandUpdate(String.format("DELETE FROM ACCESSORIES WHERE CHILDTAG=='%s';", assetTag)); //Delete from accessories list table

            execCommandUpdate(String.format("DELETE FROM ASSETS WHERE TAG=='%s';", assetTag)); //Delete from asset table

            ResultSet rs = execCommandQuery(String.format("SELECT * FROM ACCESSORIES WHERE PARENTTAG=='%s';", assetTag));
            while (rs.next()) {
                String childTag = rs.getString("CHILDTAG");
                deleteAccessory(assetTag, childTag);
            }
            MyLogger.logInfo(String.format("Succeeded to delete asset %s completely", assetTag));
            return true;
        }
        catch (SQLException e) {
            System.err.println(e.getMessage());
            MyLogger.logErr(String.format("Failed to delete asset %s completely - %s", assetTag, e.getMessage()));
            return false;
        }
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
            MyLogger.logErr(String.format("Failed to update asset %s - %s", asset.getTag(), e.getMessage()));
            return false;
        }
        MyLogger.logInfo(String.format("Succeeded to update asset %s", asset.getTag()));
        return true;
    }
}
