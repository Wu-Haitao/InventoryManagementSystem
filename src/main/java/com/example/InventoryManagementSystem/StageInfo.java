package com.example.InventoryManagementSystem;

import javafx.stage.StageStyle;

public enum StageInfo {
    LOGIN_STAGE,
    MAIN_STAGE,
    TEXT_INPUT_STAGE;

    public static String getFXML(StageInfo stageName) {
        return switch (stageName) {
            case LOGIN_STAGE -> "login-view.fxml";
            case MAIN_STAGE -> "main-view.fxml";
            case TEXT_INPUT_STAGE -> "text-input-view.fxml";
        };
    }

    public static int[] getHeightAndWidth(StageInfo stageName) {
        return switch (stageName) {
            case LOGIN_STAGE -> new int[]{400, 250};
            case MAIN_STAGE -> new int[]{900, 650};
            case TEXT_INPUT_STAGE -> new int[]{500, 250};
        };
    }

    public static String getTitle(StageInfo stageName) {
        return switch (stageName) {
            case LOGIN_STAGE -> "Login";
            case MAIN_STAGE -> "Inventory System";
            case TEXT_INPUT_STAGE -> "Input";
        };
    }

    public static StageStyle getStyle(StageInfo stageName) {
        return switch (stageName) {
            case LOGIN_STAGE, TEXT_INPUT_STAGE -> StageStyle.UTILITY;
            case MAIN_STAGE -> StageStyle.UNDECORATED;
        };
    }

    public static boolean getResizable(StageInfo stageName) {
        return switch (stageName) {
            case LOGIN_STAGE, MAIN_STAGE, TEXT_INPUT_STAGE -> false;
            default -> true;
        };
    }
}
