package com.devkit.ui;

import com.devkit.services.DataService;
import com.devkit.services.BackgroundAutoSave;
import com.devkit.services.tools.*;
import com.devkit.services.Tool;
import com.devkit.model.HistoryEntry;
import com.devkit.utils.ConsoleColors;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class MainMenu {
    private final List<Tool> tools;
    private final BackgroundAutoSave autoSave;

    public MainMenu() {
        DataService.loadAll();
        autoSave = new BackgroundAutoSave(DataService.settings.getAutoSaveIntervalMinutes());
        new Thread(autoSave).start();
        
        tools = new ArrayList<>();
        tools.add(new JsonTool());
        tools.add(new EncodingTool());
        tools.add(new HashTool());
        tools.add(new RegexTool());
        tools.add(new TextTool());
        tools.add(new DateTimeTool());
        tools.add(new CodeTool());
        tools.add(new SnippetTool());
        tools.add(new NotesTool());
        tools.add(new ProjectTool());
    }

    public void start() {
        boolean running = true;
        while (running) {
            displayDashboard();
            int choice = InputHandler.getInt("Enter choice: ", 0, 12);
            
            if (choice == 0) {
                running = false;
            } else if (choice >= 1 && choice <= 10) {
                tools.get(choice - 1).execute();
            } else if (choice == 11) {
                viewHistory();
            } else if (choice == 12) {
                settingsMenu();
            }
        }
        
        System.out.println("Saving data and shutting down...");
        autoSave.stop();
        DataService.saveAll();
        System.out.println("Goodbye, " + DataService.settings.getDeveloperName() + "!");
    }
    
    private void displayDashboard() {
        System.out.println();
        String c = DataService.settings.isUseColors() ? ConsoleColors.CYAN : "";
        String res = DataService.settings.isUseColors() ? ConsoleColors.RESET : "";
        
        System.out.println(c + "╔══════════════════════════════════════════════╗" + res);
        System.out.println(c + "║              DEVKIT TOOLKIT                  ║" + res);
        System.out.println(c + "╠══════════════════════════════════════════════╣" + res);
        System.out.println(c + "║ Welcome back, " + DataService.settings.getDeveloperName() + "!" + " ".repeat(Math.max(0, 30 - DataService.settings.getDeveloperName().length())) + "║" + res);
        System.out.println(c + "║ Quick Stats: Snippets: " + DataService.snippets.size() + " | Projects: " + DataService.projects.size() + " ".repeat(Math.max(0, 15)) + "║" + res);
        System.out.println(c + "╠══════════════════════════════════════════════╣" + res);
        
        for (int i = 0; i < tools.size(); i++) {
            System.out.printf(c + "║ [%2d] %-40s ║\n" + res, (i + 1), tools.get(i).getName());
        }
        System.out.println(c + "║ [11] History                                 ║" + res);
        System.out.println(c + "║ [12] Settings                                ║" + res);
        System.out.println(c + "║ [ 0] Exit                                    ║" + res);
        System.out.println(c + "╚══════════════════════════════════════════════╝" + res);
        System.out.println(autoSave.getStatus());
    }

    private void viewHistory() {
        ConsoleUI.printHeader("HISTORY");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if (DataService.history.isEmpty()) {
            System.out.println("No history found.");
        } else {
            for (HistoryEntry e : DataService.history) {
                System.out.println(e.getTimestamp().format(dtf) + " | " + e.getTool() + " | " + e.getAction());
            }
        }
        InputHandler.waitForEnter();
    }
    
    private void settingsMenu() {
        ConsoleUI.printHeader("SETTINGS");
        System.out.println("Developer Name: " + DataService.settings.getDeveloperName());
        System.out.println("Use Colors: " + DataService.settings.isUseColors());
        System.out.println("Auto-save Interval: " + DataService.settings.getAutoSaveIntervalMinutes() + " mins");
        
        System.out.println("\n[1] Change Name");
        System.out.println("[2] Toggle Colors");
        System.out.println("[0] Back");
        int c = InputHandler.getInt("Choice: ", 0, 2);
        if (c == 1) {
            DataService.settings.setDeveloperName(InputHandler.getRequiredString("New Name: "));
            ConsoleUI.printSuccess("Name updated.");
        } else if (c == 2) {
            DataService.settings.setUseColors(!DataService.settings.isUseColors());
            ConsoleUI.printSuccess("Colors toggled.");
        }
    }
}
