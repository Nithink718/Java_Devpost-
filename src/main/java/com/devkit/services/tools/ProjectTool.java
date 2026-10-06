package com.devkit.services.tools;

import com.devkit.services.Tool;
import com.devkit.services.DataService;
import com.devkit.services.HistoryService;
import com.devkit.enums.HistoryType;
import com.devkit.enums.TaskPriority;
import com.devkit.ui.ConsoleUI;
import com.devkit.ui.InputHandler;
import com.devkit.model.DeveloperProject;
import com.devkit.model.TodoTask;
import com.devkit.utils.IdGenerator;
import java.time.LocalDate;
import java.util.List;

public class ProjectTool implements Tool {
    @Override
    public String getName() {
        return "Project Workspace";
    }

    @Override
    public void execute() {
        while (true) {
            ConsoleUI.printHeader("PROJECT WORKSPACE");
            System.out.println("[1] View Projects");
            System.out.println("[2] Create Project");
            System.out.println("[3] Manage Tasks in Project");
            System.out.println("[0] Back");
            
            int choice = InputHandler.getInt("Enter choice: ", 0, 3);
            if (choice == 0) break;
            
            switch (choice) {
                case 1:
                    displayProjects();
                    break;
                case 2:
                    createProject();
                    break;
                case 3:
                    manageTasks();
                    break;
            }
            InputHandler.waitForEnter();
        }
    }
    
    private void displayProjects() {
        List<DeveloperProject> list = DataService.projects;
        if (list.isEmpty()) {
            ConsoleUI.printInfo("No projects found.");
            return;
        }
        for (DeveloperProject p : list) {
            System.out.println("\nID: " + p.getId() + " | " + p.getName() + " (" + p.getMainLanguage() + ")");
            System.out.println("Desc : " + p.getDescription());
            System.out.println("Tasks: " + p.getTasks().size() + " | Notes: " + p.getNoteIds().size());
        }
    }
    
    private void createProject() {
        String name = InputHandler.getRequiredString("Project Name: ");
        String desc = InputHandler.getString("Description: ");
        String lang = InputHandler.getRequiredString("Main Language: ");
        
        DeveloperProject proj = new DeveloperProject(IdGenerator.generateId("PROJ"), name, desc, lang);
        DataService.projects.add(proj);
        ConsoleUI.printSuccess("Project created! ID: " + proj.getId());
        HistoryService.log(HistoryType.PROJECT, "Created project " + name);
    }
    
    private void manageTasks() {
        String projId = InputHandler.getRequiredString("Enter Project ID: ");
        DeveloperProject project = DataService.projects.stream().filter(p -> p.getId().equalsIgnoreCase(projId)).findFirst().orElse(null);
        if (project == null) {
            ConsoleUI.printError("Project not found.");
            return;
        }
        
        while (true) {
            ConsoleUI.printHeader("TASKS: " + project.getName());
            System.out.println("[1] View Tasks");
            System.out.println("[2] Add Task");
            System.out.println("[3] Complete Task");
            System.out.println("[0] Back to Projects");
            
            int tc = InputHandler.getInt("Choice: ", 0, 3);
            if (tc == 0) break;
            
            if (tc == 1) {
                for (TodoTask t : project.getTasks()) {
                    System.out.println(t.getId() + " | [" + t.getStatus() + "] " + t.getTitle() + " - Priority: " + t.getPriority());
                }
            } else if (tc == 2) {
                String title = InputHandler.getRequiredString("Task Title: ");
                String desc = InputHandler.getString("Description: ");
                System.out.println("Priority: 1=LOW, 2=MEDIUM, 3=HIGH, 4=CRITICAL");
                int p = InputHandler.getInt("Select Priority: ", 1, 4);
                TaskPriority priority = TaskPriority.values()[p - 1];
                
                TodoTask task = new TodoTask(IdGenerator.generateId("TASK"), title, desc, priority, LocalDate.now().plusDays(7));
                project.getTasks().add(task);
                ConsoleUI.printSuccess("Task added!");
                HistoryService.log(HistoryType.PROJECT, "Added task to " + project.getName());
            } else if (tc == 3) {
                String tid = InputHandler.getRequiredString("Enter Task ID: ");
                project.getTasks().stream().filter(t -> t.getId().equalsIgnoreCase(tid)).findFirst().ifPresent(t -> {
                    t.setStatus(com.devkit.enums.TaskStatus.COMPLETED);
                    ConsoleUI.printSuccess("Task marked as completed.");
                });
            }
        }
    }
}
