package com.devkit.utils;

public class Validator {
    public static boolean isNotEmpty(String input) {
        return input != null && !input.trim().isEmpty();
    }
}
