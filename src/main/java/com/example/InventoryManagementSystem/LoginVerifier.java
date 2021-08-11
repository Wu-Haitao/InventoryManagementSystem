package com.example.InventoryManagementSystem;

public class LoginVerifier {
    public static boolean verify(String username, String password) {
        return ((username.equals("Admin")) && (password.equals("Admin")));
    }
}
