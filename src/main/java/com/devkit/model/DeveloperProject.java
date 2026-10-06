package com.devkit.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DeveloperProject implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private String description;
    private String mainLanguage;
    private LocalDateTime createdDate;
    private List<TodoTask> tasks;
    private List<String> noteIds;
    private List<String> snippetIds;

    public DeveloperProject(String id, String name, String description, String mainLanguage) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.mainLanguage = mainLanguage;
        this.createdDate = LocalDateTime.now();
        this.tasks = new ArrayList<>();
        this.noteIds = new ArrayList<>();
        this.snippetIds = new ArrayList<>();
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getMainLanguage() { return mainLanguage; }
    public void setMainLanguage(String mainLanguage) { this.mainLanguage = mainLanguage; }
    public LocalDateTime getCreatedDate() { return createdDate; }
    public List<TodoTask> getTasks() { return tasks; }
    public List<String> getNoteIds() { return noteIds; }
    public List<String> getSnippetIds() { return snippetIds; }
}
