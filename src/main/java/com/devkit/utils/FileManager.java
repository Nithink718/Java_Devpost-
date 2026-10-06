package com.devkit.utils;

import java.io.*;

public class FileManager {
    public static final String DATA_DIR = "data";
    public static final String EXPORT_DIR = "exports";

    static {
        new File(DATA_DIR).mkdirs();
        new File(EXPORT_DIR).mkdirs();
        new File(EXPORT_DIR + "/snippets").mkdirs();
        new File(EXPORT_DIR + "/notes").mkdirs();
        new File(EXPORT_DIR + "/reports").mkdirs();
    }

    public static void saveObject(String filename, Object obj) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_DIR + "/" + filename))) {
            oos.writeObject(obj);
        } catch (IOException e) {
            System.out.println(ConsoleColors.RED + "Error saving data to " + filename + ": " + e.getMessage() + ConsoleColors.RESET);
        }
    }

    public static Object loadObject(String filename) {
        File file = new File(DATA_DIR + "/" + filename);
        if (!file.exists()) return null;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(ConsoleColors.RED + "Error loading data from " + filename + " (Starting fresh) " + ConsoleColors.RESET);
            return null;
        }
    }

    public static void exportText(String path, String content) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {
            bw.write(content);
            System.out.println(ConsoleColors.GREEN + "Successfully exported to " + path + ConsoleColors.RESET);
        } catch (IOException e) {
            System.out.println(ConsoleColors.RED + "Error exporting file: " + e.getMessage() + ConsoleColors.RESET);
        }
    }
}
