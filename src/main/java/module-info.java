module com.example.InventoryManagementSystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires org.kordamp.bootstrapfx.core;
    requires com.jfoenix;
    requires java.sql;
    requires de.jensd.fx.glyphs.fontawesome;
    requires org.xerial.sqlitejdbc;
    requires org.apache.logging.log4j;
    requires java.desktop;

    opens com.example.InventoryManagementSystem to javafx.fxml;
    opens com.example.InventoryManagementSystem.Controller to javafx.fxml;

    exports com.example.InventoryManagementSystem;

}