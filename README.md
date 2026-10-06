# DevKit – Developer Utility Toolkit

## Project Description
DevKit is a comprehensive, terminal-based Core Java application designed to provide developers with essential daily utility tools in a single integrated application. It eliminates the need to rely on external web-based utilities for common tasks like JSON formatting, regex testing, encoding/decoding, text analysis, and code snippet management.

## Features
- **JSON Tools**: Format, minify, validate, and analyze JSON structure.
- **Encoding & Decoding**: Base64, URL, Decimal/Hex/Binary converters.
- **Security & Hashing**: MD5, SHA-1, SHA-256, SHA-512 generators, UUID, and Secure Password Generator.
- **Regex Tester**: Test custom and predefined regex patterns against text.
- **Text Tools**: Statistics, frequency analysis, string reversal, and cleanup.
- **Date & Time Tools**: UTC conversions, Unix timestamps, date math.
- **Code Utilities**: File statistics (LOC, comments, TODOs) and Text Diff.
- **Developer Snippet Manager**: CRUD operations, filtering, sorting, tagging.
- **Developer Notes**: Persisted note-taking with tags.
- **Project Workspace**: Simple project tracker and task/TODO management.
- **History Tracker**: Local log of developer actions.
- **Settings**: Customizable interface and auto-save configuration.

## Architecture & Core Java Concepts Demonstrated
This project employs a clean layered architecture, completely independent of external libraries, frameworks, or databases. It heavily relies on local file serialization.

- **OOP Principles**: Extensively uses inheritance, encapsulation, and abstraction (e.g., `Tool` interface implemented by `JsonTool`, `HashTool`, etc.).
- **Collections Framework**: Employs `List`, `ArrayList`, `Map`, `HashMap` for data management and sorting.
- **Serialization & File Handling**: `ObjectOutputStream`/`ObjectInputStream` for persistence.
- **Multithreading**: Uses `Runnable` for background `BackgroundAutoSave` thread.
- **Java Date/Time API**: `LocalDateTime`, `Instant`, and `ZoneId` formatting.
- **Exception Handling**: Graceful error handling avoiding ungraceful crashes.
- **Regular Expressions**: Java `Pattern` and `Matcher` for text validation.
- **Generics**: Generic Stream operations and data management.

## Project Structure
```
src/main/java/com/devkit/
 ├── Main.java               # Application entry point
 ├── ui/                     # CLI Rendering, Inputs, Dashboard
 ├── model/                  # POJOs for Data structures (Snippets, Tasks)
 ├── services/               # Data processing logic, Auto-save thread
 │    └── tools/             # Specific implementations of utility tools
 ├── utils/                  # File I/O, Generators, Console Colors
 ├── enums/                  # Fixed types for tracking & tasks
 └── exceptions/             # Custom error classes
```

## How to Compile
Using standard JDK (Java 17+ recommended):
```bash
cd DevKit
javac -d bin src/main/java/com/devkit/**/*.java src/main/java/com/devkit/*.java
```

## How to Run
```bash
cd DevKit
java -cp bin com.devkit.Main
```

## Future Improvements (Not currently implemented)
- GUI using JavaFX
- Git integration
- Terminal command integration
- Database storage (e.g., SQLite/MySQL)
- Cloud synchronization
- Plugin system & IDE integration
- Code syntax highlighting in the terminal
- AI-assisted developer tools

*Note: All storage is localized to the `data/` and `exports/` folder automatically generated upon launch.*
