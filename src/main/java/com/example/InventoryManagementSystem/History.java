package com.example.InventoryManagementSystem;

import java.util.Stack;

public class History {
    private static Stack<String> history = new Stack<>();

    public static void goForward(String newAssetTag) {
        history.push(newAssetTag);
    }

    public static String goBackward() {
        if (history.lastElement().equals("root")) {
            return "root";
        }
        history.pop();
        if (history.lastElement().equals("root")) {
            return "root";
        }
        return history.pop();
    }
}
