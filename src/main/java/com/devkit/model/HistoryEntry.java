package com.devkit.model;

import com.devkit.enums.HistoryType;
import java.io.Serializable;
import java.time.LocalDateTime;

public class HistoryEntry implements Serializable {
    private static final long serialVersionUID = 1L;

    private LocalDateTime timestamp;
    private HistoryType tool;
    private String action;

    public HistoryEntry(HistoryType tool, String action) {
        this.timestamp = LocalDateTime.now();
        this.tool = tool;
        this.action = action;
    }

    public LocalDateTime getTimestamp() { return timestamp; }
    public HistoryType getTool() { return tool; }
    public String getAction() { return action; }
}
