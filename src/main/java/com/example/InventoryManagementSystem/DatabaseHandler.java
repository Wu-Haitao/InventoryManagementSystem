package com.example.InventoryManagementSystem;

import javax.xml.transform.Result;
import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DatabaseHandler {
    private static Connection connection = null;
    public static boolean init = false;

    public static void connectDatabase(String address) {
        if (!new File(address).exists()) init = true;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + address);
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
                    " QTY          INT NOT NULL," +
                    " LOCATION     TEXT," +
                    " REMARK       TEXT," +
                    " ACCESSORIES  TEXT);";
            statement.executeUpdate(sql);
            sql = "INSERT INTO ASSETS VALUES" +
                    "('root','','','',0,0,'',0,'','','');";
            statement.executeUpdate(sql);
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

    public static Asset getRoot() {
        return getAssetWithTag("root");
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

    public static Boolean findInDatabase(String tag) {
        try {
            ResultSet rs = execCommandQuery(String.format("SELECT * FROM ASSETS WHERE TAG=='%s';", tag));
            return (rs.isClosed());
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
        String accessoriesText = rs.getString("ACCESSORIES");
        List<String> accessories = (accessoriesText.equals(""))? new ArrayList<>():Arrays.asList(accessoriesText.split(","));
        return new Asset(rs.getString("TAG"), details, accessories);
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
        for (String childAssetTag:
             getAssetWithTag(parentAsset.getTag()).getAccessories()) {
            try {
                ResultSet rs = execCommandQuery(String.format("SELECT * FROM ASSETS WHERE TAG=='%s';", childAssetTag));
                if (!rs.isClosed()) assets.add(getAssetFromResultSet(rs));
            }
            catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
        return assets;
    }

    public static List<Asset> getAllAssets() {
        List<Asset> assets = new ArrayList<>();
        try {
            ResultSet rs = execCommandQuery("SELECT * FROM ASSETS;");
            while (rs.next()) {
                assets.add(getAssetFromResultSet(rs));
            }
        }
        catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return assets;
    }

    public static boolean addAsset(Asset asset) {
        try {
            String accessories = (asset.getAccessories().isEmpty())? "":String.join(",", asset.getAccessories());
            execCommandUpdate(String.format("INSERT INTO ASSETS VALUES ('%s','%s','%s','%s',%d,%d,'%s',%d,'%s','%s','%s');",
                    asset.getTag(),
                    asset.getManufacturerName(),
                    asset.getModel(),
                    asset.getPartNo(),
                    asset.getRangeMin(),
                    asset.getRangeMax(),
                    asset.getRangeUnit(),
                    asset.getQty(),
                    asset.getLocation(),
                    asset.getRemark().replace('\'', '\"'),
                    accessories));
        }
        catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
        return true;
    }

    public static boolean deleteAsset(Asset asset) {
        try {
            execCommandUpdate(String.format("DELETE FROM ASSETS WHERE TAG=='%s';", asset.getTag()));
        }
        catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
        return true;
    }

    public static boolean updateAsset(Asset asset) {
        try {
            String accessories = (asset.getAccessories().isEmpty())? "":String.join(",", asset.getAccessories());
            execCommandUpdate(String.format("UPDATE ASSETS SET MAKE='%s',MODEL='%s',PARTNO='%s',RANGEMIN=%d,RANGEMAX=%d,RANGEUNIT='%s',QTY=%d,LOCATION='%s',REMARK='%s',ACCESSORIES='%s' WHERE TAG=='%s';",
                    asset.getManufacturerName(),
                    asset.getModel(),
                    asset.getPartNo(),
                    asset.getRangeMin(),
                    asset.getRangeMax(),
                    asset.getRangeUnit(),
                    asset.getQty(),
                    asset.getLocation(),
                    asset.getRemark().replace('\'', '\"'),
                    accessories,
                    asset.getTag()));
        }
        catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
        return true;
    }
}
