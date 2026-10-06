package com.devkit.services.tools;

import com.devkit.services.Tool;
import com.devkit.services.HistoryService;
import com.devkit.enums.HistoryType;
import com.devkit.ui.ConsoleUI;
import com.devkit.ui.InputHandler;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class RegexTool implements Tool {
    @Override
    public String getName() {
        return "Regex Tester";
    }

    @Override
    public void execute() {
        while (true) {
            ConsoleUI.printHeader("REGEX TESTER");
            System.out.println("[1] Custom Regex");
            System.out.println("[2] Predefined: Email");
            System.out.println("[3] Predefined: URL");
            System.out.println("[4] Predefined: IPv4");
            System.out.println("[0] Back");
            
            int choice = InputHandler.getInt("Enter choice: ", 0, 4);
            if (choice == 0) break;
            
            String regex = "";
            switch (choice) {
                case 1:
                    regex = InputHandler.getRequiredString("Enter Regex: ");
                    break;
                case 2:
                    regex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
                    System.out.println("Using Email Regex: " + regex);
                    break;
                case 3:
                    regex = "^(https?|ftp)://[^\\s/$.?#].[^\\s]*$";
                    System.out.println("Using URL Regex: " + regex);
                    break;
                case 4:
                    regex = "^(?:[0-9]{1,3}\\.){3}[0-9]{1,3}$";
                    System.out.println("Using IPv4 Regex: " + regex);
                    break;
            }
            
            String testString = InputHandler.getRequiredString("Enter test string: ");
            
            try {
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(testString);
                
                int count = 0;
                while (matcher.find()) {
                    if (count == 0) {
                        ConsoleUI.printSuccess("MATCH");
                    }
                    count++;
                    System.out.println("Match " + count + ": '" + matcher.group() + "' at [" + matcher.start() + "-" + matcher.end() + "]");
                }
                if (count == 0) {
                    ConsoleUI.printError("NO MATCH");
                } else {
                    System.out.println("Total matches: " + count);
                }
                HistoryService.log(HistoryType.REGEX, "Tested regex");
            } catch (PatternSyntaxException e) {
                ConsoleUI.printError("Invalid Regex Syntax: " + e.getMessage());
            }
            InputHandler.waitForEnter();
        }
    }
}
