package com.devkit.services.tools;

import com.devkit.services.Tool;
import com.devkit.services.DataService;
import com.devkit.services.HistoryService;
import com.devkit.enums.HistoryType;
import com.devkit.ui.ConsoleUI;
import com.devkit.ui.InputHandler;
import com.devkit.model.DeveloperNote;
import com.devkit.utils.IdGenerator;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class NotesTool implements Tool {
    @Override
    public String getName() {
        return "Developer Notes";
    }

    @Override
    public void execute() {
        while (true) {
            ConsoleUI.printHeader("DEVELOPER NOTES");
            System.out.println("[1] View Notes");
            System.out.println("[2] Add Note");
            System.out.println("[3] Search Notes");
            System.out.println("[4] Delete Note");
            System.out.println("[0] Back");
            
            int choice = InputHandler.getInt("Enter choice: ", 0, 4);
            if (choice == 0) break;
            
            switch (choice) {
                case 1:
                    displayNotes(DataService.notes);
                    break;
                case 2:
                    addNote();
                    break;
                case 3:
                    searchNotes();
                    break;
                case 4:
                    deleteNote();
                    break;
            }
            InputHandler.waitForEnter();
        }
    }
    
    private void displayNotes(List<DeveloperNote> list) {
        if (list.isEmpty()) {
            ConsoleUI.printInfo("No notes found.");
            return;
        }
        for (DeveloperNote n : list) {
            System.out.println("\nID: " + n.getId() + " | Title: " + n.getTitle());
            System.out.println("Tags: " + String.join(", ", n.getTags()));
            System.out.println("Content: " + n.getContent());
            System.out.println("---------");
        }
    }
    
    private void addNote() {
        String title = InputHandler.getRequiredString("Title: ");
        System.out.println("Enter Content (type 'END' on a new line to finish):");
        StringBuilder content = new StringBuilder();
        while (true) {
            String line = InputHandler.getString("> ");
            if ("END".equals(line)) break;
            content.append(line).append("\n");
        }
        String tagsStr = InputHandler.getString("Tags (comma separated): ");
        List<String> tags = Arrays.stream(tagsStr.split(",")).map(String::trim).collect(Collectors.toList());
        
        DeveloperNote note = new DeveloperNote(IdGenerator.generateId("NOTE"), title, content.toString(), tags);
        DataService.notes.add(note);
        ConsoleUI.printSuccess("Note added! ID: " + note.getId());
        HistoryService.log(HistoryType.NOTE, "Added note " + title);
    }
    
    private void searchNotes() {
        String query = InputHandler.getRequiredString("Search term: ").toLowerCase();
        List<DeveloperNote> results = DataService.notes.stream()
            .filter(n -> n.getTitle().toLowerCase().contains(query) || 
                         n.getContent().toLowerCase().contains(query) ||
                         n.getTags().stream().anyMatch(t -> t.toLowerCase().contains(query)))
            .collect(Collectors.toList());
        displayNotes(results);
        HistoryService.log(HistoryType.NOTE, "Searched notes");
    }
    
    private void deleteNote() {
        String id = InputHandler.getRequiredString("Enter Note ID to delete: ");
        boolean removed = DataService.notes.removeIf(n -> n.getId().equalsIgnoreCase(id));
        if (removed) {
            ConsoleUI.printSuccess("Note deleted.");
            HistoryService.log(HistoryType.NOTE, "Deleted note " + id);
        } else {
            ConsoleUI.printError("Note not found.");
        }
    }
}
