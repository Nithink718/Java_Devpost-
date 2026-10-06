package com.devkit.services.tools;

import com.devkit.services.Tool;
import com.devkit.services.HistoryService;
import com.devkit.enums.HistoryType;
import com.devkit.ui.ConsoleUI;
import com.devkit.ui.InputHandler;
import java.util.Base64;
import java.net.URLEncoder;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class EncodingTool implements Tool {
    @Override
    public String getName() {
        return "Encoding & Decoding";
    }

    @Override
    public void execute() {
        while (true) {
            ConsoleUI.printHeader("ENCODING & DECODING");
            System.out.println("[1] Base64 Encode");
            System.out.println("[2] Base64 Decode");
            System.out.println("[3] URL Encode");
            System.out.println("[4] URL Decode");
            System.out.println("[5] Decimal to Binary/Hex");
            System.out.println("[0] Back");
            
            int choice = InputHandler.getInt("Enter choice: ", 0, 5);
            if (choice == 0) break;
            
            try {
                switch (choice) {
                    case 1:
                        String b64e = InputHandler.getRequiredString("Enter text: ");
                        System.out.println("Base64: " + Base64.getEncoder().encodeToString(b64e.getBytes(StandardCharsets.UTF_8)));
                        HistoryService.log(HistoryType.ENCODING, "Base64 Encode");
                        break;
                    case 2:
                        String b64d = InputHandler.getRequiredString("Enter Base64: ");
                        System.out.println("Text: " + new String(Base64.getDecoder().decode(b64d), StandardCharsets.UTF_8));
                        HistoryService.log(HistoryType.ENCODING, "Base64 Decode");
                        break;
                    case 3:
                        String urle = InputHandler.getRequiredString("Enter URL: ");
                        System.out.println("Encoded: " + URLEncoder.encode(urle, StandardCharsets.UTF_8.name()));
                        HistoryService.log(HistoryType.ENCODING, "URL Encode");
                        break;
                    case 4:
                        String urld = InputHandler.getRequiredString("Enter Encoded URL: ");
                        System.out.println("Decoded: " + URLDecoder.decode(urld, StandardCharsets.UTF_8.name()));
                        HistoryService.log(HistoryType.ENCODING, "URL Decode");
                        break;
                    case 5:
                        int dec = InputHandler.getInt("Enter Decimal Number: ");
                        System.out.println("Binary: " + Integer.toBinaryString(dec));
                        System.out.println("Hex   : " + Integer.toHexString(dec).toUpperCase());
                        HistoryService.log(HistoryType.ENCODING, "Decimal Convert");
                        break;
                }
            } catch (Exception e) {
                ConsoleUI.printError("Error: " + e.getMessage());
            }
            InputHandler.waitForEnter();
        }
    }
}
