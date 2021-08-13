package com.example.InventoryManagementSystem;

import javafx.stage.Modality;
import javafx.stage.StageStyle;

public enum StageInfo {
    LOGIN_STAGE,
    MAIN_STAGE,
    TEXT_INPUT_STAGE,
    ASSET_DESCRIPTION_STAGE,
    ADD_STAGE,
    EDIT_STAGE;

    public static String getFXML(StageInfo stageName) {
        return switch (stageName) {
            case LOGIN_STAGE -> "login-view.fxml";
            case MAIN_STAGE -> "main-view.fxml";
            case TEXT_INPUT_STAGE -> "text-input-view.fxml";
            case ASSET_DESCRIPTION_STAGE -> "asset-description-view.fxml";
            case ADD_STAGE -> "add-view.fxml";
            case EDIT_STAGE -> "edit-view.fxml";
        };
    }

    public static int[] getHeightAndWidth(StageInfo stageName) {
        return switch (stageName) {
            case LOGIN_STAGE -> new int[]{400, 250};
            case MAIN_STAGE -> new int[]{1200, 650};
            case TEXT_INPUT_STAGE -> new int[]{500, 250};
            case ASSET_DESCRIPTION_STAGE -> new int[]{400, 500};
            case ADD_STAGE, EDIT_STAGE -> new int[]{400, 600};
        };
    }

    public static String getTitle(StageInfo stageName) {
        return switch (stageName) {
            case LOGIN_STAGE -> "Login";
            case MAIN_STAGE -> "Inventory System";
            case TEXT_INPUT_STAGE -> "Input";
            case ASSET_DESCRIPTION_STAGE -> "Description";
            case ADD_STAGE -> "Add";
            case EDIT_STAGE -> "Edit";
        };
    }

    public static StageStyle getStyle(StageInfo stageName) {
        return switch (stageName) {
            case LOGIN_STAGE, TEXT_INPUT_STAGE, ASSET_DESCRIPTION_STAGE, ADD_STAGE, EDIT_STAGE -> StageStyle.UTILITY;
            case MAIN_STAGE -> StageStyle.UNDECORATED;
        };
    }

    public static boolean getResizable(StageInfo stageName) {
        return switch (stageName) {
            case LOGIN_STAGE, MAIN_STAGE, TEXT_INPUT_STAGE, ASSET_DESCRIPTION_STAGE, ADD_STAGE, EDIT_STAGE -> false;
            default -> true;
        };
    }

    public static Modality getModality(StageInfo stageName) {
        return switch (stageName) {
            case ADD_STAGE, EDIT_STAGE, TEXT_INPUT_STAGE -> Modality.APPLICATION_MODAL;
            default -> Modality.NONE;
        };
    }
}
