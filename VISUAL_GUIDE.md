# Visual Guide: Running Your JavaFX CI/CD Project

## Quick Visual Reference

### 1. Project Layout

```
javafx-ci-cd-app/
│
├── 📄 pom.xml                          Maven configuration
├── 📄 Dockerfile                       Container definition
├── 📄 docker-compose.yml               Local container setup
├── 📄 .gitignore                       Git ignore patterns
│
├── 📁 src/
│   ├── main/java/com/example/app/
│   │   ├── 📜 MainApp.java             ← Run this to see GUI
│   │   ├── 📜 LoginValidator.java
│   │   └── 📜 ValidationResult.java
│   ├── main/resources/                 ← Add FXML, images here
│   └── test/java/com/example/app/
│       └── 📜 LoginValidatorTest.java   ← 30+ test cases
│
├── 📁 .github/workflows/
│   ├── 📄 ci.yml                       Auto build & test
│   └── 📄 cd.yml                       Auto deploy
│
└── 📁 target/                          ← Generated (ignore)
    ├── classes/                         Compiled code
    ├── test-classes/                    Test code
    ├── *.jar                            Runnable JAR
    └── surefire-reports/                Test results
```

---

## 2. Local Development Workflow

```
┌─────────────────────────────────────────────────────────────┐
│                    YOU (Developer)                          │
└────────────┬────────────────────────────────────────────────┘
             │ Edit MainApp.java
             ▼
┌─────────────────────────────────────────────────────────────┐
│  IDE (VS Code / IntelliJ)                                  │
│  • Code highlighting                                        │
│  • Code completion                                          │
│  • Debug mode                                               │
└────────────┬────────────────────────────────────────────────┘
             │ Save file
             ▼
┌─────────────────────────────────────────────────────────────┐
│  $ mvn clean javafx:run                                    │
│                                                             │
│  [Maven Phase 1] COMPILE                                   │
│  • Reads pom.xml                                           │
│  • Downloads dependencies (first time only)                │
│  • Compiles Java files                                     │
│  • ~30 seconds                                             │
│                                                             │
│  [Maven Phase 2] PACKAGE                                   │
│  • Packages compiled classes                               │
│  • Includes all dependencies                               │
│  • ~20 seconds                                             │
│                                                             │
│  [Maven Phase 3] EXEC                                      │
│  • Launches JavaFX application                             │
│  • ~2 seconds                                              │
└────────────┬────────────────────────────────────────────────┘
             │
             ▼
    ╔═══════════════════════════════╗
    ║  JavaFX Application Window    ║
    ║  ┌─────────────────────────┐  ║
    ║  │ JavaFX Login Portal     │  ║
    ║  │                         │  ║
    ║  │ Username: [________]    │  ║
    ║  │ Password: [________]    │  ║
    ║  │                         │  ║
    ║  │ Error: Username empty   │  ║
    ║  │                         │  ║
    ║  │     [ Login ]           │  ║
    ║  └─────────────────────────┘  ║
    ╚═══════════════════════════════╝
             │
             │ Enter: john_doe / password123
             ▼
    ╔═══════════════════════════════╗
    ║  ✓ Login successful!          ║
    ║    Welcome, john_doe          ║
    ╚═══════════════════════════════╝
```

---

## 3. Testing Workflow

```
┌─────────────────────────────────────────────────────────────┐
│  $ mvn test                                                │
└────────────┬────────────────────────────────────────────────┘
             │
             ▼
┌─────────────────────────────────────────────────────────────┐
│  [INFO] Scanning for projects...                           │
│  [INFO] Building javafx-ci-cd-app 1.0.0                   │
│  [INFO] ────────────────────────────────────                │
│  [INFO] Compiling 1 source file to target/classes          │
│  [INFO] Compiling 1 test source file                       │
└────────────┬────────────────────────────────────────────────┘
             │
             ▼
┌─────────────────────────────────────────────────────────────┐
│  [INFO] ─── T E S T   E X E C U T I O N ─────              │
│                                                             │
│  Running com.example.app.LoginValidatorTest               │
│  [  1] ✓ testEmptyUsername (0.02s)                        │
│  [  2] ✓ testNullUsername (0.01s)                         │
│  [  3] ✓ testUsernameTooShort (0.01s)                     │
│  [  4] ✓ testUsernameTooLong (0.01s)                      │
│  [  5] ✓ testUsernameWithSpecialChars (0.01s)             │
│  [  6] ✓ testValidUsernameLetters (0.01s)                 │
│  [  7] ✓ testValidUsernameNumbers (0.01s)                 │
│  [  8] ✓ testValidUsernameUnderscores (0.01s)             │
│  ...                                                        │
│  [ 30] ✓ testPasswordWithSpecialChars (0.01s)             │
│                                                             │
│  Tests run: 30, Failures: 0, Errors: 0, Skipped: 0       │
│  ✓ All tests passed!                                       │
└────────────┬────────────────────────────────────────────────┘
             │
             ▼
┌─────────────────────────────────────────────────────────────┐
│  [INFO] BUILD SUCCESS                                      │
│  [INFO] Total time: 8.234 s                                │
│  [INFO] Finished at: 2026-01-30T10:30:45-05:00            │
└─────────────────────────────────────────────────────────────┘
```

---

## 4. Docker Build & Run

```
┌────────────────────────────────────────────────────────────┐
│  $ docker build -t javafx-app:1.0 .                       │
└─────────────┬──────────────────────────────────────────────┘
              │
        ┌─────┴─────┐
        │           │
        ▼           ▼
    STAGE 1:   STAGE 2:
    Builder    Runtime
    ┌─────┐    ┌──────┐
    │Maven│    │ JRE  │
    │ JDK │    │ Only │
    └─────┘    └──────┘
        │           │
        └─────┬─────┘
              │ (Compiled code copied)
              ▼
    ┌────────────────────┐
    │   Docker Image     │
    │   javafx-app:1.0   │
    │   ~400 MB          │
    └─────────┬──────────┘
              │
              ▼
    $ docker run javafx-app:1.0
    
    ✓ Container starts
    ✓ Application launches
    ✓ Ready to accept requests
```

---

## 5. CI/CD Pipeline - Visual Timeline

```
Developer pushes code to GitHub
             │
             ▼ (Webhook trigger)
┌──────────────────────────────────────────────────────────────┐
│  GITHUB ACTIONS - CI Pipeline (ci.yml)                      │
│  Duration: ~3-5 minutes                                     │
├──────────────────────────────────────────────────────────────┤
│  ┌─────────────────────────────────────────────────────┐    │
│  │ Job: build-and-test                     ⏱ 90s      │    │
│  │ ├─ Checkout code                        ✓ (10s)   │    │
│  │ ├─ Setup Java 17                        ✓ (15s)   │    │
│  │ ├─ Build (mvn compile)                  ✓ (30s)   │    │
│  │ ├─ Test (mvn test)                      ✓ (20s)   │    │
│  │ └─ Package (mvn package)                ✓ (15s)   │    │
│  └─────────────────────────────────────────────────────┘    │
│                                                              │
│  ┌─────────────────────────────────────────────────────┐    │
│  │ Job: code-analysis                      ⏱ 30s      │    │
│  │ └─ Static analysis checks                ✓         │    │
│  └─────────────────────────────────────────────────────┘    │
│                                                              │
│  ┌─────────────────────────────────────────────────────┐    │
│  │ Job: security-scan                      ⏱ 30s      │    │
│  │ └─ Dependency vulnerability scan         ✓         │    │
│  └─────────────────────────────────────────────────────┘    │
│                                                              │
│  ✓ RESULT: BUILD SUCCESS                                   │
│  ✓ Test Reports uploaded                                   │
│  ✓ JAR artifact created                                    │
└──────────────────────────────────────────────────────────────┘
             │
             │ (Only if on 'main' branch)
             ▼
┌──────────────────────────────────────────────────────────────┐
│  GITHUB ACTIONS - CD Pipeline (cd.yml)                      │
│  Duration: ~10-15 minutes                                   │
├──────────────────────────────────────────────────────────────┤
│  ┌─────────────────────────────────────────────────────┐    │
│  │ Job: build-docker-image                 ⏱ 3min    │    │
│  │ ├─ Setup Docker Buildx                  ✓ (10s)   │    │
│  │ ├─ Login to Docker Hub                  ✓ (5s)    │    │
│  │ ├─ Build multi-stage image              ✓ (2min)  │    │
│  │ └─ Push to Docker Hub                   ✓ (50s)   │    │
│  └─────────────────────────────────────────────────────┘    │
│                                                              │
│  ┌─────────────────────────────────────────────────────┐    │
│  │ Job: deploy-production                  ⏱ 2min    │    │
│  │ ├─ Kubernetes deployment                ✓ (1min)  │    │
│  │ └─ Health checks                        ✓ (1min)  │    │
│  └─────────────────────────────────────────────────────┘    │
│                                                              │
│  ✓ RESULT: DEPLOYMENT SUCCESS                             │
│  ✓ Application running in production                       │
│  ✓ Docker Hub image available                             │
└──────────────────────────────────────────────────────────────┘
             │
             ▼
    Application Live in Production!
```

---

## 6. GitHub Actions Dashboard

```
Repository: javafx-ci-cd-app
Branch: main

Actions Tab:
┌─────────────────────────────────────────────────────────────┐
│  Recent workflow runs:                                      │
│                                                             │
│  ✓ ✓ 2026-01-30 10:35   Pushed new validation rules       │
│                  CI ✓ (3m 45s)                             │
│                  CD ✓ (12m 30s)                            │
│                  docker.io/user/app:latest                 │
│                                                             │
│  ✓ ✓ 2026-01-30 10:20   Fixed login bug                    │
│                  CI ✓ (3m 50s)                             │
│                  CD ✓ (11m 20s)                            │
│                  docker.io/user/app:v1.0.1                │
│                                                             │
│  ✓ ✓ 2026-01-30 09:45   Initial commit                     │
│                  CI ✓ (4m 10s)                             │
│                  CD ✓ (13m 00s)                            │
│                  docker.io/user/app:v1.0.0                │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

---

## 7. Docker Hub Registry

```
Docker Hub Repository: username/javafx-ci-cd-app

Overview:
┌─────────────────────────────────────────────────────────────┐
│  Repository: username/javafx-ci-cd-app                     │
│  Visibility: Public / Private                              │
│  Stars: ⭐⭐⭐⭐ (4 stars)                                    │
│                                                             │
│  Tags:
│  ┌─────────────────────────────────────────────────────┐  │
│  │ latest (2026-01-30)                                 │  │
│  │   Size: 398 MB                                      │  │
│  │   Scanned: ✓ No vulnerabilities                     │  │
│  │   FROM eclipse-temurin:17-jre-focal                │  │
│  │   USER appuser                                      │  │
│  │   ENTRYPOINT ["java", "-jar", "app.jar"]            │  │
│  └─────────────────────────────────────────────────────┘  │
│                                                             │
│  ┌─────────────────────────────────────────────────────┐  │
│  │ v1.0.1 (2026-01-30)                                 │  │
│  │   Size: 398 MB                                      │  │
│  │   Scanned: ✓ No vulnerabilities                     │  │
│  └─────────────────────────────────────────────────────┘  │
│                                                             │
│  ┌─────────────────────────────────────────────────────┐  │
│  │ v1.0.0 (2026-01-29)                                 │  │
│  │   Size: 400 MB                                      │  │
│  │   Scanned: ✓ No vulnerabilities                     │  │
│  └─────────────────────────────────────────────────────┘  │
│                                                             │
└─────────────────────────────────────────────────────────────┘

Pull command:
$ docker pull username/javafx-ci-cd-app:latest
$ docker run username/javafx-ci-cd-app:latest
```

---

## 8. File Dependencies

```
pom.xml (Maven config)
    │
    ├─→ Downloads Dependencies
    │   ├─ javafx-controls:21.0.2
    │   ├─ javafx-fxml:21.0.2
    │   ├─ javafx-graphics:21.0.2
    │   ├─ junit-jupiter:5.9.3
    │   └─ junit:4.13.2
    │
    ├─→ Compiles
    │   ├─ src/main/java/*.java
    │   │   ├─ MainApp.java
    │   │   ├─ LoginValidator.java
    │   │   └─ ValidationResult.java
    │   │
    │   └─ src/test/java/*.java
    │       └─ LoginValidatorTest.java
    │
    └─→ Creates
        ├─ target/classes/              (compiled code)
        ├─ target/*.jar                 (executable)
        ├─ target/surefire-reports/     (test results)
        └─ Dockerfile                   (reads target/*.jar)
            │
            └─→ Creates Docker Image (398 MB)
```

---

## 9. Error Handling Flow

```
User Input (username, password)
         │
         ▼ MainApp.handleLogin()
         │
    ┌────┴────────────────────────────┐
    │                                  │
    ▼                                  ▼
Empty?                           validateCredentials()
  │                                   │
  ├─ YES → Error: Empty              ├─ Valid ✓
  │        Display message           │ └─ Success dialog
  │                                  │
  └─ NO → Continue                   └─ Invalid ✗
                                        └─ Show error
                                           - Too short
                                           - Invalid chars
                                           - etc.
```

---

## 10. Environment Setup Checklist

```
Pre-Requisites:
┌─────────────────────────────────────────────────────────┐
│ [ ] Java 17+         │ java -version                   │
│ [ ] Maven 3.9.0+     │ mvn --version                   │
│ [ ] Git 2.40+        │ git --version                   │
│ [ ] Docker (opt)     │ docker --version                │
│ [ ] IDE (opt)        │ VS Code / IntelliJ              │
└─────────────────────────────────────────────────────────┘

GitHub Setup:
┌─────────────────────────────────────────────────────────┐
│ [ ] Create GitHub account                              │
│ [ ] Create new repository                              │
│ [ ] Add DOCKER_USERNAME secret                         │
│ [ ] Add DOCKER_PASSWORD secret                         │
│ [ ] Create Docker Hub account                          │
│ [ ] Generate Docker access token                       │
└─────────────────────────────────────────────────────────┘

Local Setup:
┌─────────────────────────────────────────────────────────┐
│ [ ] Clone repository                                    │
│ [ ] $ mvn clean compile    (verify setup)              │
│ [ ] $ mvn test             (verify tests)              │
│ [ ] $ mvn javafx:run       (verify GUI)                │
│ [ ] $ docker build -t app . (verify Docker)           │
└─────────────────────────────────────────────────────────┘
```

---

## 11. Quick Command Reference Card

```
╔════════════════════════════════════════════════════════════╗
║              QUICK COMMAND REFERENCE                       ║
╠════════════════════════════════════════════════════════════╣
║ MAVEN COMMANDS                                            ║
║ mvn clean                    Clean build directory        ║
║ mvn compile                  Compile only                 ║
║ mvn test                     Run all tests               ║
║ mvn package                  Create JAR                   ║
║ mvn javafx:run               Run GUI app                  ║
║ mvn clean javafx:run         Clean rebuild and run        ║
║ mvn test -Dtest=ClassTest   Run specific test           ║
╠════════════════════════════════════════════════════════════╣
║ DOCKER COMMANDS                                           ║
║ docker build -t app:1.0 .   Build image                 ║
║ docker run app:1.0          Run container               ║
║ docker ps                    List containers             ║
║ docker logs -f app          View logs                   ║
║ docker-compose up            Start with compose         ║
╠════════════════════════════════════════════════════════════╣
║ GIT COMMANDS                                              ║
║ git add .                    Stage changes               ║
║ git commit -m "msg"          Commit                      ║
║ git push                     Push to GitHub              ║
║ git pull                     Pull changes                ║
║ git status                   Show status                 ║
╚════════════════════════════════════════════════════════════╝
```

---

This visual guide makes the entire workflow crystal clear!
