package com.devkit.services;

import com.devkit.model.*;
import com.devkit.utils.FileManager;
import java.util.ArrayList;
import java.util.List;

public class DataService {
    public static List<CodeSnippet> snippets = new ArrayList<>();
    public static List<DeveloperNote> notes = new ArrayList<>();
    public static List<DeveloperProject> projects = new ArrayList<>();
    public static List<HistoryEntry> history = new ArrayList<>();
    public static AppSettings settings = new AppSettings();

    @SuppressWarnings("unchecked")
    public static void loadAll() {
        Object s = FileManager.loadObject("snippets.dat");
        if (s != null) snippets = (List<CodeSnippet>) s;
        else initSampleSnippets();

        Object n = FileManager.loadObject("notes.dat");
        if (n != null) notes = (List<DeveloperNote>) n;
        else initSampleNotes();

        Object p = FileManager.loadObject("projects.dat");
        if (p != null) projects = (List<DeveloperProject>) p;
        else initSampleProjects();

        Object h = FileManager.loadObject("history.dat");
        if (h != null) history = (List<HistoryEntry>) h;

        Object set = FileManager.loadObject("settings.dat");
        if (set != null) settings = (AppSettings) set;
    }

    public static void saveAll() {
        FileManager.saveObject("snippets.dat", snippets);
        FileManager.saveObject("notes.dat", notes);
        FileManager.saveObject("projects.dat", projects);
        FileManager.saveObject("history.dat", history);
        FileManager.saveObject("settings.dat", settings);
    }
    
    private static void initSampleSnippets() {
        snippets.add(new CodeSnippet("SNIP-1", "Binary Search", "Java", 
            "public int binarySearch(int arr[], int x) {\n  int l = 0, r = arr.length - 1;\n  while (l <= r) {\n    int m = l + (r - l) / 2;\n    if (arr[m] == x) return m;\n    if (arr[m] < x) l = m + 1;\n    else r = m - 1;\n  }\n  return -1;\n}",
            "Classic binary search algorithm.", List.of("dsa", "search")));
    }
    
    private static void initSampleNotes() {
        notes.add(new DeveloperNote("NOTE-1", "Java Collections", "List: Ordered, allows duplicates.\nSet: Unordered, no duplicates.\nMap: Key-value pairs.", List.of("java", "core")));
    }
    
    private static void initSampleProjects() {
        projects.add(new DeveloperProject("PROJ-1", "DevKit Demo", "Sample project for DevKit", "Java"));
    }
}
