package com.devkit.services.tools;

import com.devkit.services.Tool;
import com.devkit.services.HistoryService;
import com.devkit.enums.HistoryType;
import com.devkit.ui.ConsoleUI;
import com.devkit.ui.InputHandler;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class DateTimeTool implements Tool {
    @Override
    public String getName() {
        return "Date & Time Tools";
    }

    @Override
    public void execute() {
        while (true) {
            ConsoleUI.printHeader("DATE & TIME TOOLS");
            System.out.println("[1] Current Date & Time");
            System.out.println("[2] Unix Timestamp to Date");
            System.out.println("[3] Date to Unix Timestamp");
            System.out.println("[4] Add/Subtract Days");
            System.out.println("[0] Back");
            
            int choice = InputHandler.getInt("Enter choice: ", 0, 4);
            if (choice == 0) break;
            
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            
            try {
                switch (choice) {
                    case 1:
                        System.out.println("Local Time: " + LocalDateTime.now().format(formatter));
                        System.out.println("UTC Time  : " + LocalDateTime.now(ZoneId.of("UTC")).format(formatter));
                        System.out.println("Unix Time : " + Instant.now().getEpochSecond());
                        HistoryService.log(HistoryType.DATE_TIME, "Current Date/Time");
                        break;
                    case 2:
                        long ts = Long.parseLong(InputHandler.getRequiredString("Enter Unix Timestamp (seconds): "));
                        LocalDateTime date = LocalDateTime.ofInstant(Instant.ofEpochSecond(ts), ZoneId.systemDefault());
                        System.out.println("Date: " + date.format(formatter));
                        HistoryService.log(HistoryType.DATE_TIME, "Unix to Date");
                        break;
                    case 3:
                        System.out.println("Feature in development (Enter format yyyy-MM-ddTHH:mm:ss).");
                        break;
                    case 4:
                        int days = InputHandler.getInt("Enter days to add (negative to subtract): ");
                        System.out.println("Result: " + LocalDateTime.now().plusDays(days).format(formatter));
                        HistoryService.log(HistoryType.DATE_TIME, "Date Math");
                        break;
                }
            } catch (Exception e) {
                ConsoleUI.printError("Invalid input: " + e.getMessage());
            }
            InputHandler.waitForEnter();
        }
    }
}
