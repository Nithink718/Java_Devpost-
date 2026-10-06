package com.devkit.services.tools;

import com.devkit.services.Tool;
import com.devkit.services.HistoryService;
import com.devkit.enums.HistoryType;
import com.devkit.ui.ConsoleUI;
import com.devkit.ui.InputHandler;
import java.io.File;
import java.nio.file.Files;
import java.util.List;
import com.devkit.utils.ConsoleColors;

public class CodeTool implements Tool {
    @Override
    public String getName() {
        return "Code Utilities";
    }

    @Override
    public void execute() {
        while (true) {
            ConsoleUI.printHeader("CODE UTILITIES");
            System.out.println("[1] Code Statistics");
            System.out.println("[2] Simple Text Diff");
            System.out.println("[0] Back");
            
            int choice = InputHandler.getInt("Enter choice: ", 0, 2);
            if (choice == 0) break;
            
            try {
                if (choice == 1) {
                    String path = InputHandler.getRequiredString("Enter file path: ");
                    File file = new File(path);
                    if (!file.exists() || !file.isFile()) {
                        ConsoleUI.printError("Invalid file path.");
                        continue;
                    }
                    analyzeCode(file);
                    HistoryService.log(HistoryType.CODE, "Code Statistics");
                } else if (choice == 2) {
                    System.out.println("Enter Version A (type 'END' on a new line to finish):");
                    String a = readMultiline();
                    System.out.println("Enter Version B (type 'END' on a new line to finish):");
                    String b = readMultiline();
                    showDiff(a, b);
                    HistoryService.log(HistoryType.CODE, "Text Diff");
                }
            } catch (Exception e) {
                ConsoleUI.printError("Error: " + e.getMessage());
            }
            InputHandler.waitForEnter();
        }
    }
    
    private void analyzeCode(File file) throws Exception {
        List<String> lines = Files.readAllLines(file.toPath());
        int total = lines.size();
        int blank = 0, comment = 0, code = 0, todos = 0, fixmes = 0;
        
        for (String line : lines) {
            String trimmed = line.trim();
            if (trimmed.isEmpty()) blank++;
            else if (trimmed.startsWith("//") || trimmed.startsWith("/*") || trimmed.startsWith("*")) comment++;
            else code++;
            
            if (trimmed.toUpperCase().contains("TODO")) todos++;
            if (trimmed.toUpperCase().contains("FIXME")) fixmes++;
        }
        
        System.out.println("\nCode Statistics for " + file.getName());
        System.out.println("Total Lines   : " + total);
        System.out.println("Code Lines    : " + code);
        System.out.println("Comment Lines : " + comment);
        System.out.println("Blank Lines   : " + blank);
        System.out.println("TODOs         : " + todos);
        System.out.println("FIXMEs        : " + fixmes);
    }
    
    private String readMultiline() {
        StringBuilder sb = new StringBuilder();
        while (true) {
            String line = InputHandler.getString("> ");
            if ("END".equals(line)) break;
            sb.append(line).append("\n");
        }
        return sb.toString();
    }
    
    private void showDiff(String a, String b) {
        String[] linesA = a.split("\n");
        String[] linesB = b.split("\n");
        int max = Math.max(linesA.length, linesB.length);
        int added = 0, removed = 0, changed = 0;
        
        for (int i = 0; i < max; i++) {
            String lA = i < linesA.length ? linesA[i] : null;
            String lB = i < linesB.length ? linesB[i] : null;
            
            if (lA == null && lB != null) {
                System.out.println(ConsoleColors.GREEN + "ADDED     | " + lB + ConsoleColors.RESET);
                added++;
            } else if (lB == null && lA != null) {
                System.out.println(ConsoleColors.RED + "REMOVED   | " + lA + ConsoleColors.RESET);
                removed++;
            } else if (lA.equals(lB)) {
                System.out.println("UNCHANGED | " + lA);
            } else {
                System.out.println(ConsoleColors.YELLOW + "CHANGED(A)| " + lA);
                System.out.println("CHANGED(B)| " + lB + ConsoleColors.RESET);
                changed++;
            }
        }
        System.out.println("\nDiff Summary: Added: " + added + ", Removed: " + removed + ", Changed: " + changed);
    }
}
