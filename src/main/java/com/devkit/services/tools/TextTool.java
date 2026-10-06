package com.devkit.services.tools;

import com.devkit.services.Tool;
import com.devkit.services.HistoryService;
import com.devkit.enums.HistoryType;
import com.devkit.ui.ConsoleUI;
import com.devkit.ui.InputHandler;
import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;

public class TextTool implements Tool {
    @Override
    public String getName() {
        return "Text Tools";
    }

    @Override
    public void execute() {
        while (true) {
            ConsoleUI.printHeader("TEXT TOOLS");
            System.out.println("[1] Text Statistics (Words, Chars, Lines)");
            System.out.println("[2] Frequency Analysis");
            System.out.println("[3] Reverse Text");
            System.out.println("[4] Convert Uppercase/Lowercase");
            System.out.println("[5] Remove Extra Spaces");
            System.out.println("[0] Back");
            
            int choice = InputHandler.getInt("Enter choice: ", 0, 5);
            if (choice == 0) break;
            
            String text = InputHandler.getRequiredString("Enter text: ");
            
            switch (choice) {
                case 1:
                    String[] words = text.trim().split("\\s+");
                    String[] lines = text.split("\r?\n");
                    System.out.println("Characters: " + text.length());
                    System.out.println("Words     : " + (text.trim().isEmpty() ? 0 : words.length));
                    System.out.println("Lines     : " + lines.length);
                    HistoryService.log(HistoryType.TEXT, "Text Stats");
                    break;
                case 2:
                    Map<String, Integer> freq = new HashMap<>();
                    for (String w : text.toLowerCase().trim().split("\\s+")) {
                        freq.put(w, freq.getOrDefault(w, 0) + 1);
                    }
                    freq.entrySet().stream()
                        .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                        .limit(10)
                        .forEach(e -> System.out.println(e.getKey() + ": " + e.getValue()));
                    HistoryService.log(HistoryType.TEXT, "Frequency Analysis");
                    break;
                case 3:
                    System.out.println("Reversed: " + new StringBuilder(text).reverse().toString());
                    HistoryService.log(HistoryType.TEXT, "Reverse Text");
                    break;
                case 4:
                    System.out.println("Uppercase: " + text.toUpperCase());
                    System.out.println("Lowercase: " + text.toLowerCase());
                    HistoryService.log(HistoryType.TEXT, "Change Case");
                    break;
                case 5:
                    System.out.println("Cleaned: " + text.replaceAll("\\s+", " ").trim());
                    HistoryService.log(HistoryType.TEXT, "Remove Spaces");
                    break;
            }
            InputHandler.waitForEnter();
        }
    }
}
