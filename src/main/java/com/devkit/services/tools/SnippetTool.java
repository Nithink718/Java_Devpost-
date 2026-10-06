package com.devkit.services.tools;

import com.devkit.services.Tool;
import com.devkit.services.DataService;
import com.devkit.services.HistoryService;
import com.devkit.enums.HistoryType;
import com.devkit.ui.ConsoleUI;
import com.devkit.ui.InputHandler;
import com.devkit.model.CodeSnippet;
import com.devkit.utils.IdGenerator;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Comparator;

public class SnippetTool implements Tool {
    @Override
    public String getName() {
        return "Snippet Manager";
    }

    @Override
    public void execute() {
        while (true) {
            ConsoleUI.printHeader("SNIPPET MANAGER");
            System.out.println("[1] View All Snippets");
            System.out.println("[2] Add Snippet");
            System.out.println("[3] Search Snippets");
            System.out.println("[4] Sort Snippets");
            System.out.println("[5] Delete Snippet");
            System.out.println("[0] Back");
            
            int choice = InputHandler.getInt("Enter choice: ", 0, 5);
            if (choice == 0) break;
            
            switch (choice) {
                case 1:
                    displaySnippets(DataService.snippets);
                    break;
                case 2:
                    addSnippet();
                    break;
                case 3:
                    searchSnippets();
                    break;
                case 4:
                    sortSnippets();
                    break;
                case 5:
                    deleteSnippet();
                    break;
            }
            InputHandler.waitForEnter();
        }
    }
    
    private void displaySnippets(List<CodeSnippet> list) {
        if (list.isEmpty()) {
            ConsoleUI.printInfo("No snippets found.");
            return;
        }
        for (CodeSnippet s : list) {
            System.out.println("\nID: " + s.getId() + " | Title: " + s.getTitle() + " | Lang: " + s.getLanguage());
            System.out.println("Tags: " + String.join(", ", s.getTags()));
            System.out.println("Code:\n" + s.getCode());
        }
    }
    
    private void addSnippet() {
        String title = InputHandler.getRequiredString("Title: ");
        String lang = InputHandler.getRequiredString("Language: ");
        System.out.println("Enter Code (type 'END' on a new line to finish):");
        StringBuilder code = new StringBuilder();
        while (true) {
            String line = InputHandler.getString("> ");
            if ("END".equals(line)) break;
            code.append(line).append("\n");
        }
        String desc = InputHandler.getString("Description: ");
        String tagsStr = InputHandler.getString("Tags (comma separated): ");
        List<String> tags = Arrays.stream(tagsStr.split(",")).map(String::trim).collect(Collectors.toList());
        
        CodeSnippet snippet = new CodeSnippet(IdGenerator.generateId("SNIP"), title, lang, code.toString(), desc, tags);
        DataService.snippets.add(snippet);
        ConsoleUI.printSuccess("Snippet added successfully! ID: " + snippet.getId());
        HistoryService.log(HistoryType.SNIPPET, "Added snippet " + title);
    }
    
    private void searchSnippets() {
        String query = InputHandler.getRequiredString("Search term (Title/Lang/Tag): ").toLowerCase();
        List<CodeSnippet> results = DataService.snippets.stream()
            .filter(s -> s.getTitle().toLowerCase().contains(query) || 
                         s.getLanguage().toLowerCase().contains(query) ||
                         s.getTags().stream().anyMatch(t -> t.toLowerCase().contains(query)))
            .collect(Collectors.toList());
        displaySnippets(results);
        HistoryService.log(HistoryType.SNIPPET, "Searched snippets");
    }
    
    private void sortSnippets() {
        System.out.println("[1] Sort by Title");
        System.out.println("[2] Sort by Language");
        System.out.println("[3] Sort by Date");
        int c = InputHandler.getInt("Choice: ", 1, 3);
        
        if (c == 1) DataService.snippets.sort(Comparator.comparing(CodeSnippet::getTitle));
        else if (c == 2) DataService.snippets.sort(Comparator.comparing(CodeSnippet::getLanguage));
        else if (c == 3) DataService.snippets.sort(Comparator.comparing(CodeSnippet::getCreatedDate).reversed());
        
        ConsoleUI.printSuccess("Snippets sorted.");
        displaySnippets(DataService.snippets);
        HistoryService.log(HistoryType.SNIPPET, "Sorted snippets");
    }
    
    private void deleteSnippet() {
        String id = InputHandler.getRequiredString("Enter Snippet ID to delete: ");
        boolean removed = DataService.snippets.removeIf(s -> s.getId().equalsIgnoreCase(id));
        if (removed) {
            ConsoleUI.printSuccess("Snippet deleted.");
            HistoryService.log(HistoryType.SNIPPET, "Deleted snippet " + id);
        } else {
            ConsoleUI.printError("Snippet not found.");
        }
    }
}
