# Product Requirements Document (PRD) – DevKit

## 1. Project Overview & Vision
**Product Name:** DevKit
**Vision:** To provide developers with a premium, local-first "Command Center" that unifies all essential daily utilities (JSON formatting, cryptographic hashing, API testing, regex validation) into a single, cohesive, offline workspace. It aims to eliminate context-switching and eradicate the privacy risks associated with pasting proprietary code into third-party web tools.

## 2. Target Audience
- **Software Engineers & Developers:** Backend, Frontend, and Full-Stack developers who frequently work with APIs, data manipulation, and text parsing.
- **Security-Conscious Professionals:** Developers working with proprietary code, internal APIs, or sensitive keys (e.g., enterprise, healthcare, or fintech developers) who cannot use public web utilities.
- **Sysadmins & DevOps Engineers:** Professionals who frequently construct cron jobs, parse stack traces, or manipulate environment variables.

## 3. Problem Statement & Solution
**The Problem:**
1. **Privacy Risks:** Developers often use random web utilities (e.g., free JSON formatters, Base64 decoders) and inadvertently expose sensitive data to third-party tracking or logging.
2. **Context Switching:** Developers constantly interrupt their "flow state" by jumping between their IDE, Terminal, API clients (like Postman), and dozens of Chrome tabs.

**The Solution:**
A unified local application (DevKit) that feels like an IDE extension. By keeping all processing local and combining tools into a keyboard-first interface, developers maintain their privacy and productivity.

## 4. Product Principles
- **Local-First & Private:** Zero external data harvesting. All processing happens on the local machine. Data is stored locally (`localStorage` for UI, `java.io` serialization for Backend).
- **Keyboard-Centric:** Heavy reliance on hotkeys (e.g., `Ctrl+K` Command Palette) for instant navigation.
- **Lightweight & Fast:** The UI must run on pure Vanilla JS/HTML/CSS without heavy framework latency, while the backend relies on Core Java (no heavy Spring/Jakarta overhead).
- **Premium Aesthetics:** "Majestic Gold/Cream" UI theme, moving away from generic SaaS dashboards toward an IDE-style workspace.

## 5. Key Features & Capabilities (Scope)

### 5.1 Workspace Management
- **Feature:** Users can organize snippets, notes, and environment variables under specific isolated "Workspaces".
- **Feature:** Global Command Palette (`Ctrl+K`) for rapid navigation.
- **Feature:** Global History Log recording all actions (e.g., "Generated SHA-256 Hash").

### 5.2 Network & APIs (Postman-Lite)
- **Feature:** API Collections to build, save, and execute HTTP requests (GET, POST, PUT, DELETE) with headers and payloads.
- **Feature:** Environment Variables Manager to securely inject API keys and base URLs into requests.
- **Feature:** Webhook Sandbox to simulate and inspect incoming payload headers.

### 5.3 Core Developer Utilities
- **JSON Toolkit:** Format, minify, and validate raw JSON.
- **Cryptography & Hashing:** Native generation of MD5, SHA-1, SHA-256, and SHA-512 hashes.
- **Text & Code Analysis:** Git-style Diff Checker, Regular Expression (Regex) Explainer, and Deterministic Error Stack Trace Analyzer.
- **Cron Decoder:** Visual decoder mapping cron strings (e.g., `*/5 * * * *`) into human-readable schedules.

### 5.4 Data Storage & Export
- **Feature:** Seamless JSON export of the entire workspace state for secure local backups.
- **Feature:** Persistent developer notes and code snippet saving.

## 6. Future Iterations (Roadmap)
- **Phase 1 (Current):** Standalone Java CLI backend and a standalone Vanilla JS Frontend prototype using `localStorage` and `mock` APIs.
- **Phase 2 (Integration):** Bridge the Java backend to the Frontend using a local Java HTTP Server (`com.sun.net.httpserver`). The UI will send `fetch` requests directly to the Java backend.
- **Phase 3 (Persistence):** Migrate from browser `localStorage` to local SQLite or Java `.dat` files managed by the backend engine.
- **Phase 4 (Packaging):** Package the unified client-server architecture into a single executable application (e.g., using Electron or JavaFX WebView).
