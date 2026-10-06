package com.devkit.ui;

import com.devkit.utils.ConsoleColors;

public class ConsoleUI {
    public static void printHeader(String title) {
        System.out.println();
        System.out.println(ConsoleColors.CYAN + "╔" + "═".repeat(46) + "╗");
        int padding = (46 - title.length()) / 2;
        int extra = (46 - title.length()) % 2;
        System.out.println("║" + " ".repeat(padding) + ConsoleColors.BOLD + title + ConsoleColors.CYAN + " ".repeat(padding + extra) + "║");
        System.out.println("╚" + "═".repeat(46) + "╝" + ConsoleColors.RESET);
    }

    public static void printSuccess(String message) {
        System.out.println(ConsoleColors.GREEN + "✓ " + message + ConsoleColors.RESET);
    }

    public static void printError(String message) {
        System.out.println(ConsoleColors.RED + "✗ " + message + ConsoleColors.RESET);
    }

    public static void printInfo(String message) {
        System.out.println(ConsoleColors.YELLOW + "ℹ " + message + ConsoleColors.RESET);
    }
}
