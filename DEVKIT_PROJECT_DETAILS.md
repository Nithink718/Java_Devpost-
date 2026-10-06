# DevKit — Utility Workbench
**Complete Project Architecture & Feature Specification**

## 1. Project Vision
DevKit is not just a collection of utilities; it is a **Developer Command Center**. It is designed to be a premium, local-first developer productivity application that developers can realistically keep open alongside VS Code. 

The core philosophy is: `TOOLS → WORKSPACE → PROJECT → WORKFLOWS → PRODUCTIVITY`.

## 2. System Architecture

The project currently exists in two decoupled layers that represent the complete vision:

### A. Core Engine (Java Backend)
- **Language:** Core Java (JDK 8+)
- **Execution:** Terminal-based CLI interface (`com.devkit.Main`).
- **Storage:** Local file system via `java.io` serialization (stored in `/data`).
- **Capabilities:** High-performance implementations of JSON parsing, cryptographic hashing, Base64 encoding, regex matching, text manipulation, and file-based snippets.

### B. Prototype UI (Web Frontend)
- **Stack:** Vanilla HTML5, CSS3, JavaScript (ES6+). Zero heavy frameworks.
- **Iconography:** Lucide Icons (CDN).
- **Execution:** Local HTTP Server (e.g., Python `http.server 8080`).
- **Storage:** Browser `localStorage` (Local-first, no external databases).
- **Design System:** "Majestic Gold/Cream Light Theme" – A highly premium, luxurious aesthetic using *Playfair Display* for headings and *Fira Code* for monospace developer data.

---

## 3. Comprehensive Feature List (UI Prototype)

### 🚀 Workspaces & Navigation
- **Workspace Context:** The entire application is organized around Workspaces. APIs, Snippets, and Environments are tied to the active workspace.
- **Command Palette (`Ctrl + K`):** A global spotlight-style search bar that allows instant keyboard navigation to any tool or workspace category.
- **Global History:** Tracks all developer actions (e.g., "Generated SHA-256 Hash", "Formatted JSON") across the application into a unified timeline.

### 🌐 Network & APIs
- **API Collections:** A "Postman-lite" built directly into the UI. Developers can create HTTP requests (GET/POST/PUT/DELETE), set custom headers, define payloads, and send real network fetch requests with live response timing.
- **Environments Manager:** A localized key-value store to manage API Keys, URLs, and environment variables with masked visibility.
- **Webhook Tester (Mock Mode):** A local sandbox to simulate incoming webhook payloads and inspect raw HTTP headers and JSON bodies.

### 🛠️ Developer Tools
- **JSON Formatter:** Formats, minifies, and syntax-highlights raw JSON data.
- **Hash Generator:** Generates SHA-256, SHA-1, SHA-384, and SHA-512 cryptographic digests using the browser's native `crypto.subtle` API.
- **Diff Checker:** Git-style line-by-line text comparison highlighting additions (green) and deletions (red).
- **Error Analyzer:** Deterministically parses Java/Node stack traces, extracts the Exception type (e.g., `NullPointerException`), isolates the file/line number, and provides actionable fixes.
- **Regex Explain:** Parses regular expressions (e.g., `^[a-zA-Z]+$`) and breaks them down into human-readable visual components.
- **Cron Builder:** Visually decodes Cron expressions (e.g., `*/5 * * * *`) into human-readable text ("Every 5 minutes") with quick presets.
- **Snippets & Notes:** A side-by-side IDE layout to create, edit, and save markdown/code notes directly to the workspace.

### 💾 Data Management
- **Import / Export Data:** Full JSON export capabilities to backup the entire DevKit database (APIs, Environments, History, Snippets) locally.
- **Clear Data:** One-click reset to wipe `localStorage`.

---

## 4. UI / UX Design Principles

- **Not a SaaS Dashboard:** The UI avoids generic dashboard cards. Instead, it utilizes **IDE-style split views**, floating panels, asymmetric layouts, and dense developer information.
- **Visual Identity:** 
  - Background: Premium Cream/White (`#FAf8f5`)
  - Typography: *Playfair Display* (Luxury), *Inter* (UI), *Fira Code* (Code).
  - Accents: Deep Gold (`#C49B5B`), with pastel squircle icons and subtle radial wave backgrounds on tool cards.
- **Keyboard First:** Relies heavily on `Ctrl + K` (Command Palette) and keyboard-friendly focus states.

---

## 5. Next Steps for Production Integration

To turn this prototype into the final product, the next architectural step is the **Java-to-Web Bridge**:
1. Implement `com.sun.net.httpserver.HttpServer` inside the Java Backend.
2. Expose the Core Java logic (Hashing, Code Generation, File operations) as RESTful JSON endpoints (e.g., `POST /api/hash`).
3. Point the Web UI's JavaScript fetch calls away from local mock logic and directly into the Java local server.
4. Replace `localStorage` with the Java backend's `java.io` persistent storage engine.
