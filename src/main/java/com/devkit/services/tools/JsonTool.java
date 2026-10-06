package com.devkit.services.tools;

import com.devkit.services.Tool;
import com.devkit.services.HistoryService;
import com.devkit.enums.HistoryType;
import com.devkit.ui.ConsoleUI;
import com.devkit.ui.InputHandler;

public class JsonTool implements Tool {
    @Override
    public String getName() {
        return "JSON Tools";
    }

    @Override
    public void execute() {
        while (true) {
            ConsoleUI.printHeader("JSON TOOLS");
            System.out.println("[1] Format JSON");
            System.out.println("[2] Minify JSON");
            System.out.println("[3] JSON Statistics");
            System.out.println("[0] Back");
            
            int choice = InputHandler.getInt("Enter choice: ", 0, 3);
            if (choice == 0) break;
            
            String json = InputHandler.getRequiredString("Enter JSON: ");
            if (!validateJson(json)) {
                ConsoleUI.printError("Invalid JSON structure.");
                continue;
            }
            
            try {
                switch (choice) {
                    case 1:
                        String formatted = formatJson(json);
                        System.out.println("\nFormatted JSON:\n" + formatted);
                        HistoryService.log(HistoryType.JSON, "Formatted JSON");
                        break;
                    case 2:
                        String minified = minifyJson(json);
                        System.out.println("\nMinified JSON:\n" + minified);
                        HistoryService.log(HistoryType.JSON, "Minified JSON");
                        break;
                    case 3:
                        showStatistics(json);
                        HistoryService.log(HistoryType.JSON, "Generated JSON stats");
                        break;
                }
            } catch (Exception e) {
                ConsoleUI.printError("Error processing JSON: " + e.getMessage());
            }
            InputHandler.waitForEnter();
        }
    }
    
    private boolean validateJson(String json) {
        int braces = 0;
        int brackets = 0;
        boolean inQuotes = false;
        boolean escape = false;
        for (char c : json.toCharArray()) {
            if (escape) {
                escape = false;
                continue;
            }
            if (c == '\\') {
                escape = true;
                continue;
            }
            if (c == '"') {
                inQuotes = !inQuotes;
            }
            if (!inQuotes) {
                if (c == '{') braces++;
                else if (c == '}') braces--;
                else if (c == '[') brackets++;
                else if (c == ']') brackets--;
            }
            if (braces < 0 || brackets < 0) return false;
        }
        return braces == 0 && brackets == 0 && !inQuotes;
    }
    
    private String formatJson(String json) {
        StringBuilder formatted = new StringBuilder();
        int indent = 0;
        boolean inQuotes = false;
        boolean escape = false;
        
        for (char c : json.toCharArray()) {
            if (escape) {
                formatted.append(c);
                escape = false;
                continue;
            }
            if (c == '\\') {
                formatted.append(c);
                escape = true;
                continue;
            }
            if (c == '"') {
                inQuotes = !inQuotes;
                formatted.append(c);
                continue;
            }
            if (inQuotes) {
                formatted.append(c);
            } else {
                if (c == '{' || c == '[') {
                    formatted.append(c).append('\n');
                    indent++;
                    formatted.append("  ".repeat(indent));
                } else if (c == '}' || c == ']') {
                    formatted.append('\n');
                    indent--;
                    formatted.append("  ".repeat(indent)).append(c);
                } else if (c == ',') {
                    formatted.append(c).append('\n');
                    formatted.append("  ".repeat(indent));
                } else if (c == ':') {
                    formatted.append(c).append(' ');
                } else if (!Character.isWhitespace(c)) {
                    formatted.append(c);
                }
            }
        }
        return formatted.toString();
    }
    
    private String minifyJson(String json) {
        StringBuilder minified = new StringBuilder();
        boolean inQuotes = false;
        boolean escape = false;
        for (char c : json.toCharArray()) {
            if (escape) {
                minified.append(c);
                escape = false;
                continue;
            }
            if (c == '\\') {
                minified.append(c);
                escape = true;
                continue;
            }
            if (c == '"') {
                inQuotes = !inQuotes;
                minified.append(c);
                continue;
            }
            if (inQuotes) {
                minified.append(c);
            } else if (!Character.isWhitespace(c)) {
                minified.append(c);
            }
        }
        return minified.toString();
    }
    
    private void showStatistics(String json) {
        int objects = 0, arrays = 0, fields = 0, strings = 0;
        boolean inQuotes = false;
        boolean escape = false;
        for (char c : json.toCharArray()) {
            if (escape) {
                escape = false;
                continue;
            }
            if (c == '\\') {
                escape = true;
                continue;
            }
            if (c == '"') {
                if (!inQuotes) strings++;
                inQuotes = !inQuotes;
            }
            if (!inQuotes) {
                if (c == '{') objects++;
                if (c == '[') arrays++;
                if (c == ':') fields++;
            }
        }
        System.out.println("Objects: " + objects);
        System.out.println("Arrays: " + arrays);
        System.out.println("Fields: " + fields);
        System.out.println("Strings: " + strings);
    }
}
