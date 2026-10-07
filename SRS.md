# Software Requirements Specification (SRS) – DevKit

## 1. Introduction

### 1.1 Purpose
The purpose of this document is to define the software requirements for **DevKit**, a local-first, peer-to-peer developer utility workspace. It outlines the functional and non-functional requirements, system architecture, and specific tool capabilities.

### 1.2 Scope
DevKit is a desktop-environment application consisting of a Core Java Backend engine and a Vanilla Web UI frontend. It is designed to replace multiple external web-based utilities by bringing JSON formatting, cryptographic hashing, API testing, and workspace management to a secure, offline environment.

## 2. Overall Description

### 2.1 System Architecture
The system employs a decoupled, client-server paradigm running locally on a single host machine:
- **Client (Frontend):** A Single Page Application (SPA) built using Vanilla JavaScript (ES6+), HTML5, and CSS3. State is managed via `localStorage`.
- **Server (Backend):** A Core Java (JDK 8+) application executing logic processes (Hashing, RegEx parsing, File I/O) independent of external frameworks.
- **Future Integration:** The frontend will communicate with the backend via a localized HTTP server instantiated by Java's `com.sun.net.httpserver`.

### 2.2 User Characteristics
The system is built for technical users (Software Engineers). They expect rapid feedback, keyboard shortcuts (Command Palette), dark/light IDE-style themes, and accurate parsing of technical data like stack traces and cron jobs.

### 2.3 Constraints
- **Privacy Constraint:** The application must never make unauthorized external API calls with user data.
- **Dependency Constraint:** The system must rely on native APIs (`java.util`, `java.security`, Web Crypto API, Fetch API) to minimize bloat. No massive node modules or Java Spring/Jakarta overhead.

## 3. Functional Requirements

### 3.1 Workspace & Navigation Module
- **REQ-1.1:** The system shall provide a Command Palette triggered by `Ctrl+K` to search and navigate all features.
- **REQ-1.2:** The system shall maintain an isolated state of snippets, API keys, and notes bound to a user-defined Workspace.
- **REQ-1.3:** The system shall log significant user interactions to a Global History Tracker.

### 3.2 API & Networking Module
- **REQ-2.1:** The system shall allow users to construct HTTP GET, POST, PUT, and DELETE requests.
- **REQ-2.2:** The system shall support custom HTTP Headers and raw JSON body payloads.
- **REQ-2.3:** The system shall mask and securely store Environment Variables used for dynamic URL/Header injection.

### 3.3 Core Processing Utilities
- **REQ-3.1 (JSON):** The system shall parse stringified JSON, format it with indentation, and catch syntax errors.
- **REQ-3.2 (Crypto):** The system shall compute MD5, SHA-1, SHA-256, and SHA-512 hashes of string inputs.
- **REQ-3.3 (Diff):** The system shall compare two string arrays and highlight additions (green) and deletions (red).
- **REQ-3.4 (Regex):** The system shall evaluate a string against a user-defined regular expression and return boolean matches and capture groups.
- **REQ-3.5 (Error Analysis):** The system shall parse Java and Node.js stack traces to extract the Exception class, File name, and Line number.

### 3.4 Data Persistence
- **REQ-4.1:** The frontend shall persist UI state and workspace data instantly to browser `localStorage`.
- **REQ-4.2:** The Java backend shall serialize user object models to local `.dat` files via `java.io.ObjectOutputStream`.
- **REQ-4.3:** The system shall provide a global mechanism to export the entire workspace state as a JSON backup file.

## 4. External Interface Requirements

### 4.1 User Interfaces
- **Design System:** "Majestic Gold/Cream Light Theme" utilizing CSS Grid and Flexbox for an IDE-style split-pane view.
- **Typography:** *Playfair Display* for primary headers, *Inter* for standard UI text, and *Fira Code* for technical code blocks.

### 4.2 Software Interfaces
- **Web Crypto API:** Used natively by the browser for secure hashing operations (until integrated with the Java backend).
- **Fetch API:** Used by the UI's API Collections tool to transmit standard HTTP network requests.
- **Java SE API:** `java.io` for filesystem access, `java.util.regex` for text analysis, and `java.security.MessageDigest` for cryptography.

## 5. Non-Functional Requirements

### 5.1 Performance
- **Responsiveness:** The Vanilla JS UI must render route changes in under 50ms.
- **Processing Time:** Large string manipulation (JSON formatting, Hashing) must complete in under 500ms to prevent main thread blocking.

### 5.2 Security & Privacy
- **Data Locality:** 100% of user data (API keys, code snippets, notes) must reside on the local host machine.
- **Masking:** API keys stored in Environment Variables must be visually masked (`••••••••`) by default in the UI.

### 5.3 Reliability
- **Graceful Error Handling:** Malformed JSON inputs or Regex patterns must not crash the application. The system must catch the exception and render a human-readable error in the UI or CLI.
- **Auto-Save:** Data (Snippets/Notes) shall be auto-saved asynchronously to prevent data loss during application closure.
