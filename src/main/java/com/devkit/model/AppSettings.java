package com.devkit.model;

import java.io.Serializable;

public class AppSettings implements Serializable {
    private static final long serialVersionUID = 1L;

    private String developerName;
    private boolean useColors;
    private int autoSaveIntervalMinutes;
    private int historyLimit;

    public AppSettings() {
        this.developerName = "Developer";
        this.useColors = true;
        this.autoSaveIntervalMinutes = 5;
        this.historyLimit = 100;
    }

    public String getDeveloperName() { return developerName; }
    public void setDeveloperName(String developerName) { this.developerName = developerName; }
    public boolean isUseColors() { return useColors; }
    public void setUseColors(boolean useColors) { this.useColors = useColors; }
    public int getAutoSaveIntervalMinutes() { return autoSaveIntervalMinutes; }
    public void setAutoSaveIntervalMinutes(int minutes) { this.autoSaveIntervalMinutes = minutes; }
    public int getHistoryLimit() { return historyLimit; }
    public void setHistoryLimit(int limit) { this.historyLimit = limit; }
}
