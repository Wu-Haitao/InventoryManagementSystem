package com.example.InventoryManagementSystem;

import javafx.stage.StageStyle;

public enum StageInfo {
    LOGIN_STAGE,
    MAIN_STAGE,
    TEXT_INPUT_STAGE,
    ASSET_DESCRIPTION_VIEW;

    public static String getFXML(StageInfo stageName) {
        return switch (stageName) {
            case LOGIN_STAGE -> "login-view.fxml";
            case MAIN_STAGE -> "main-view.fxml";
            case TEXT_INPUT_STAGE -> "text-input-view.fxml";
            case ASSET_DESCRIPTION_VIEW -> "asset-description-view.fxml";
        };
    }

    public static int[] getHeightAndWidth(StageInfo stageName) {
        return switch (stageName) {
            case LOGIN_STAGE -> new int[]{400, 250};
            case MAIN_STAGE -> new int[]{1200, 650};
            case TEXT_INPUT_STAGE -> new int[]{500, 250};
            case ASSET_DESCRIPTION_VIEW -> new int[]{400, 500};
        };
    }

    public static String getTitle(StageInfo stageName) {
        return switch (stageName) {
            case LOGIN_STAGE -> "Login";
            case MAIN_STAGE -> "Inventory System";
            case TEXT_INPUT_STAGE -> "Input";
            case ASSET_DESCRIPTION_VIEW -> "Description";
        };
    }

    public static StageStyle getStyle(StageInfo stageName) {
        return switch (stageName) {
            case LOGIN_STAGE, TEXT_INPUT_STAGE, ASSET_DESCRIPTION_VIEW -> StageStyle.UTILITY;
            case MAIN_STAGE -> StageStyle.UNDECORATED;
        };
    }

    public static boolean getResizable(StageInfo stageName) {
        return switch (stageName) {
            case LOGIN_STAGE, MAIN_STAGE, TEXT_INPUT_STAGE, ASSET_DESCRIPTION_VIEW -> false;
            default -> true;
        };
    }
}
