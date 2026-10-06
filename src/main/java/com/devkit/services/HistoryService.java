package com.devkit.services;

import com.devkit.enums.HistoryType;
import com.devkit.model.HistoryEntry;

public class HistoryService {
    public static void log(HistoryType tool, String action) {
        DataService.history.add(new HistoryEntry(tool, action));
        if (DataService.history.size() > DataService.settings.getHistoryLimit()) {
            DataService.history.remove(0);
        }
    }
}
