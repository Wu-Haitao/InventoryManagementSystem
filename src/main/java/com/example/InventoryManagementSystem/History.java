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
        else {
            history.pop();
            while (!DatabaseHandler.findInDatabase(history.peek())) {
                history.pop();
            }
            return history.peek();
        }
    }
}
