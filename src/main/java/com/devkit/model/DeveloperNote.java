package com.devkit.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public class DeveloperNote implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String title;
    private String content;
    private List<String> tags;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public DeveloperNote(String id, String title, String content, List<String> tags) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.tags = tags;
        this.createdDate = LocalDateTime.now();
        this.modifiedDate = LocalDateTime.now();
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; this.modifiedDate = LocalDateTime.now(); }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; this.modifiedDate = LocalDateTime.now(); }
    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; this.modifiedDate = LocalDateTime.now(); }
    public LocalDateTime getCreatedDate() { return createdDate; }
    public LocalDateTime getModifiedDate() { return modifiedDate; }
}
