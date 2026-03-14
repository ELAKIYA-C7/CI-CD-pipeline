# 🚀 Quick Start Guide

## Prerequisites
- **Java 17+** installed
- **Maven 3.9+** installed
- **Git** (for version control)

## Build the Project

```bash
cd d:\projects\javafx-ci-cd-app
mvn clean compile
```

**Output:**
```
✅ [INFO] BUILD SUCCESS
✅ All 8 Java files compiled
```

## Run Unit Tests

```bash
mvn test
```

**Expected:**
```
✅ Tests run: 21, Failures: 0, Errors: 0
```

## Run the Application

### Option 1: Using Maven (Recommended)
```powershell
Set-Location "d:\projects\javafx-ci-cd-app"
mvn exec:java "-Dexec.mainClass=com.example.app.MainApp"
```

### Option 2: Using Maven JavaFX Plugin
```bash
mvn javafx:run
```

### Option 3: Using JAR (requires JavaFX modules)
```bash
java -jar target\javafx-ci-cd-app-1.0.0-jar-with-dependencies.jar
```

## Test Login Credentials

| Username | Password | Result |
|----------|----------|--------|
| `john_doe` | `password123` | ✅ Success |
| `jane_smith` | `secure_pass` | ✅ Success |
| `invalid` | `wrong` | ❌ Error |

## Application Features

### 1️⃣ Login Page
- Username and password validation
- Real-time error messages
- Clean, intuitive UI

### 2️⃣ Dashboard Page
- Welcome message with username
- System statistics cards
- Recent activity feed
- Navigation to other sections

### 3️⃣ Profile Page
- View and edit user information
- Email, phone, role display
- Save/Reset buttons
- Profile update confirmation

### 4️⃣ Settings Page
- **General:** Language, timezone, date format
- **Appearance:** Theme, font size, compact mode
- **Security:** 2FA, session timeout, login alerts
- **Notifications:** Email, push, SMS preferences

### 5️⃣ Admin Panel
- **User Management:** Add/edit/delete users, reset passwords
- **System Status:** CPU, memory, database health checks
- **Reports:** Generate and download reports
- **Configuration:** System settings, maintenance mode

## Build & Package

### Create Executable JAR
```bash
mvn clean package
```

**Outputs:**
- `target/javafx-ci-cd-app-1.0.0.jar` (26 KB) - Standard JAR
- `target/javafx-ci-cd-app-1.0.0-jar-with-dependencies.jar` (9.4 MB) - Uber JAR with all dependencies

### Build Docker Image
```bash
docker build -t javafx-ci-cd-app:1.0.0 .
docker run -it javafx-ci-cd-app:1.0.0
```

### Run with Docker Compose
```bash
docker-compose up
```

## CI/CD Pipeline

The project includes full CI/CD automation:

### GitHub Actions - Continuous Integration (CI)
**File:** `.github/workflows/ci.yml`

**Triggers on:**
- `git push` to `main` or `develop`
- Pull requests

**Performs:**
1. ✅ Compile code
2. ✅ Run 21 unit tests
3. ✅ Code quality checks
4. ✅ Security scanning
5. ✅ Artifact upload

### GitHub Actions - Continuous Deployment (CD)
**File:** `.github/workflows/cd.yml`

**Triggers on:**
- Successful CI pipeline + `main` branch push

**Performs:**
1. ✅ Build Docker image
2. ✅ Push to Docker Hub
3. ✅ Generate deployment ready artifact

## Project Structure

```
javafx-ci-cd-app/
├── .github/workflows/
│   ├── ci.yml                    # CI Pipeline
│   └── cd.yml                    # CD Pipeline
├── src/
│   ├── main/java/com/example/
│   │   ├── app/
│   │   │   ├── MainApp.java             # Entry point
│   │   │   ├── LoginValidator.java      # Business logic
│   │   │   └── ValidationResult.java    # Result object
│   │   ├── AdminPage.java               # Admin UI
│   │   ├── DashboardPage.java           # Dashboard UI
│   │   ├── ProfilePage.java             # Profile UI
│   │   ├── SettingsPage.java            # Settings UI
│   │   └── SceneManager.java            # Navigation
│   └── test/java/com/example/
│       └── app/LoginValidatorTest.java  # 21 unit tests
├── pom.xml                       # Maven configuration
├── Dockerfile                    # Docker build
├── docker-compose.yml            # Local deployment
└── README.md                     # Documentation
```

## Key Technologies

| Technology | Version | Purpose |
|-----------|---------|---------|
| Java | 17 LTS | Programming language |
| JavaFX | 21.0.2 | GUI framework |
| Maven | 3.9.12 | Build automation |
| JUnit | 5.9.3 | Unit testing |
| Docker | Latest | Containerization |
| GitHub Actions | - | CI/CD automation |

## Troubleshooting

### Issue: `Unable to access jarfile`
**Solution:** Ensure you're in the correct directory and JAR exists
```bash
ls target/*.jar
```

### Issue: `JavaFX runtime components are missing`
**Solution:** Use Maven to run (it handles JavaFX setup)
```bash
mvn exec:java "-Dexec.mainClass=com.example.app.MainApp"
```

### Issue: Maven not found
**Solution:** Add Maven to PATH or use full path
```bash
# Windows
mvn clean compile
```

### Issue: Port already in use (Docker)
**Solution:** Change port in docker-compose.yml
```yaml
ports:
  - "8081:8080"  # Changed from 8080:8080
```

## Development Workflow

### 1. Make changes to code
```bash
# Edit Java files in src/main/java/
```

### 2. Test locally
```bash
mvn clean test
```

### 3. Compile and run
```bash
mvn clean compile
mvn exec:java "-Dexec.mainClass=com.example.app.MainApp"
```

### 4. Commit and push
```bash
git add .
git commit -m "Feature: Add new functionality"
git push origin main
```

### 5. GitHub Actions runs automatically
- CI pipeline builds & tests
- CD pipeline deploys if CI succeeds

## Performance Tips

- **First build:** ~2-3 minutes (downloads dependencies)
- **Subsequent builds:** ~30 seconds
- **Tests:** ~100ms
- **Runtime:** Starts in <2 seconds

## Support

For issues or questions:
1. Check [CI-CD_PROOF.md](CI-CD_PROOF.md) for automation details
2. Review [ARCHITECTURE.md](ARCHITECTURE.md) for design patterns
3. Check [FINAL_CHECKLIST.md](FINAL_CHECKLIST.md) for verification

---

**Happy coding!** 🎉 Your production-ready JavaFX CI/CD application is ready to deploy.
