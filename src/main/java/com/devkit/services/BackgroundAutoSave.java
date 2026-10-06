package com.devkit.services;

import java.time.LocalDateTime;

public class BackgroundAutoSave implements Runnable {
    private volatile boolean running = true;
    private final int intervalMinutes;
    private LocalDateTime lastSave;

    public BackgroundAutoSave(int intervalMinutes) {
        this.intervalMinutes = intervalMinutes;
        this.lastSave = LocalDateTime.now();
    }

    @Override
    public void run() {
        while (running) {
            try {
                Thread.sleep(intervalMinutes * 60 * 1000L);
                if (!running) break;
                // Auto-save logic triggers here by asking DataManager/Repositories
                // For simplicity, we just trigger a save all
                DataService.saveAll();
                lastSave = LocalDateTime.now();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    public void stop() {
        this.running = false;
    }

    public String getStatus() {
        return "ACTIVE (Last save: " + lastSave.getHour() + ":" + String.format("%02d", lastSave.getMinute()) + ":" + String.format("%02d", lastSave.getSecond()) + ")";
    }
}
