package com.devkit.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public class CodeSnippet implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String id;
    private String title;
    private String language;
    private String code;
    private String description;
    private List<String> tags;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    public CodeSnippet(String id, String title, String language, String code, String description, List<String> tags) {
        this.id = id;
        this.title = title;
        this.language = language;
        this.code = code;
        this.description = description;
        this.tags = tags;
        this.createdDate = LocalDateTime.now();
        this.updatedDate = LocalDateTime.now();
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; this.updatedDate = LocalDateTime.now(); }
    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; this.updatedDate = LocalDateTime.now(); }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; this.updatedDate = LocalDateTime.now(); }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; this.updatedDate = LocalDateTime.now(); }
    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; this.updatedDate = LocalDateTime.now(); }
    public LocalDateTime getCreatedDate() { return createdDate; }
    public LocalDateTime getUpdatedDate() { return updatedDate; }
}
