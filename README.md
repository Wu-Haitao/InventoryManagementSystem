# InventoryManagementSystem

## Introduction

This is an application aimed at facilitating the management of spare parts.

The application is developed with **JavaFX** and **SQLite**.

## Features

### View

#### Asset Details

The panel left contains all the information about the current selected asset including its tag, quantity, etc.

Some information may be wrapped. Click on the button on top right to view the full details.

#### Accessories List

Each asset may or may not have multiple accessories. All accessories of the current selected asset are displayed in the table on the right side.

#### View Mode

The view mode can be changed by toggling the switch under the table. When toggled, all assets will be displayed.

### Filter/Search

To quickly spot a specific asset, set the filter located at up left.

The filter now supports tag and quantity attribute.

### Navigation

By clicking the right-arrow button in the table row, the corresponding accessory will be selected.

To go back, click on the left-arrow button.

### Add

Click on the add button to open the add window. After filling in the asset details and clicking the save button, the asset will be added as an accessory of the current selected asset.

Note that the asset tag is necessary. If the tag is already in the accessories list, the operation will fail.

#### Copy Accessories List

This function is for the case when adding an asset that is a variant of another and they share the same accessories list.

Click on the copy button in the asset details panel. Then in the add window, check the copy option and when the addition is completed the copied list will be appended to the newly added accessory.

### Edit

Click on the edit button to open the edit window. After modifying the asset details and clicking the save button, the asset will be updated accordingly.

Note that the asset tag cannot be changed. Instead, delete this asset and add a new one.

The copy accessories list function can be implemented in this operation as well.

#### Edit an Accessory

The edit buttons can also be found in the accessories list, for the convenience of editing each accessory.

### Delete

Select the assets in the table (hold ctrl or shift to select multiple). Then click on the delete button.

The delete function removes the selected assets from the accessories list of the current selected asset. Accessories under the selected assets may be deleted as well depending on whether they have any 'parent' assets.

Some of the deleted assets may still be in the database. An asset will be fully removed only when it doesn't belong to any asset as an accessory.

When 'view all' mode is activated, deleting an asset will completely remove it from the database.

### Log

Operations will be recorded into the log file located at ~/AppData/Log folder. Do check the log file when encounter any issue.

## Appendix

### ER Diagram

![ER Diagram](https://user-images.githubusercontent.com/59434361/131804584-7b10b0b1-473e-4b04-adef-39e592e7bf0d.png)
