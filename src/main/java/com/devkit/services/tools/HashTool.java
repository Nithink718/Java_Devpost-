package com.devkit.services.tools;

import com.devkit.services.Tool;
import com.devkit.services.HistoryService;
import com.devkit.enums.HistoryType;
import com.devkit.ui.ConsoleUI;
import com.devkit.ui.InputHandler;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.UUID;

public class HashTool implements Tool {
    @Override
    public String getName() {
        return "Security & Hashing";
    }

    @Override
    public void execute() {
        while (true) {
            ConsoleUI.printHeader("SECURITY & HASHING");
            ConsoleUI.printInfo("Hashing utilities are provided for development/testing purposes.");
            System.out.println("[1] Generate MD5");
            System.out.println("[2] Generate SHA-1");
            System.out.println("[3] Generate SHA-256");
            System.out.println("[4] Generate SHA-512");
            System.out.println("[5] Random UUID");
            System.out.println("[6] Secure Password Generator");
            System.out.println("[0] Back");
            
            int choice = InputHandler.getInt("Enter choice: ", 0, 6);
            if (choice == 0) break;
            
            try {
                if (choice >= 1 && choice <= 4) {
                    String text = InputHandler.getRequiredString("Enter text: ");
                    String algo = switch (choice) {
                        case 1 -> "MD5";
                        case 2 -> "SHA-1";
                        case 3 -> "SHA-256";
                        case 4 -> "SHA-512";
                        default -> "";
                    };
                    System.out.println(algo + ": " + hashText(text, algo));
                    HistoryService.log(HistoryType.HASH, "Generated " + algo);
                } else if (choice == 5) {
                    System.out.println("UUID: " + UUID.randomUUID().toString());
                    HistoryService.log(HistoryType.HASH, "Generated UUID");
                } else if (choice == 6) {
                    int length = InputHandler.getInt("Password Length (8-64): ", 8, 64);
                    System.out.println("Generated: " + generatePassword(length));
                    HistoryService.log(HistoryType.HASH, "Generated Password");
                }
            } catch (Exception e) {
                ConsoleUI.printError("Error: " + e.getMessage());
            }
            InputHandler.waitForEnter();
        }
    }
    
    private String hashText(String text, String algorithm) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(algorithm);
        byte[] digest = md.digest(text.getBytes());
        StringBuilder sb = new StringBuilder();
        for (byte b : digest) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
    
    private String generatePassword(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_+";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }
}
