package com.example.InventoryManagementSystem;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class LoginVerifier {
    public static boolean verify(String username, String password) {
        if (DatabaseHandler.init) {
            DatabaseHandler.registerUser(username, MD5Encode(password));
            return true;
        }
        else {
            return DatabaseHandler.checkUser(username, MD5Encode(password));
        }
    }

    private static String MD5Encode(String origin) {
        StringBuilder result = new StringBuilder();
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(origin.getBytes(StandardCharsets.UTF_8));
            byte[] bytes = messageDigest.digest();
            for (byte aByte : bytes) {
                result.append(Integer.toHexString(aByte & 0xff));
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return result.toString();
    }
}
