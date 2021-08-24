package com.example.InventoryManagementSystem;

import java.util.Stack;

public class History {
    private static Stack<String> history = new Stack<>();
    private static boolean flag = false;

    public static void goForward(String newAssetTag) {
        flag = true;
        history.push(newAssetTag);
    }

    public static String goBackward() {
        if (history.lastElement().equals("root")) {
            return "root";
        }
        if (flag) {
            flag = false;
            history.pop();
            if (history.lastElement().equals("root")) {
                return "root";
            }
        }
        return history.pop();
    }
}
