# Quick Start Guide

## 5-Minute Setup

### Prerequisites
```bash
# Check installations
java -version        # Should be 17+
mvn --version       # Should be 3.9.0+
git --version       # Any recent version
docker --version    # Optional, for containerization
```

---

## Step 1: Clone & Setup (2 minutes)

```bash
# Navigate to project
cd javafx-ci-cd-app

# Initialize git (if not cloned)
git init
git add .
git commit -m "Initial commit"
```

---

## Step 2: Build & Run (3 minutes)

### Run Locally
```bash
# Option 1: Direct Maven (FASTEST)
mvn clean javafx:run

# Option 2: Build then run JAR
mvn clean package
java -jar target/javafx-ci-cd-app-1.0.0-jar-with-dependencies.jar

# Option 3: Run in IDE
# IntelliJ: Right-click MainApp.java → Run
# VS Code: Open MainApp.java → Run (top-right button)
```

### Using Docker
```bash
# Build image
docker build -t javafx-app:1.0 .

# Run container
docker run -d --name javafx javafx-app:1.0

# View logs
docker logs -f javafx
```

---

## Step 3: Run Tests (1 minute)

```bash
# Run all tests
mvn test

# View results
open target/surefire-reports/index.html  # macOS
start target/surefire-reports/index.html # Windows
```

---

## Step 4: Deploy to GitHub (Optional)

```bash
# Add GitHub remote
git remote add origin https://github.com/YOUR-USERNAME/javafx-ci-cd-app.git

# Push code
git branch -M main
git push -u origin main

# GitHub Actions will automatically:
# ✓ Build the project
# ✓ Run all 30+ tests
# ✓ Create JAR artifact
# ✓ Build Docker image
# ✓ Push to Docker Hub (if secrets configured)
```

---

## Step 5: Configure CI/CD (5 minutes)

### Add GitHub Secrets

1. Go to: Settings → Secrets and variables → Actions
2. Create new secrets:

```
DOCKER_USERNAME = your-docker-hub-username
DOCKER_PASSWORD = your-docker-access-token (not password!)
```

### Get Docker Hub Access Token
1. Login to [Docker Hub](https://hub.docker.com)
2. Account Settings → Security → New Access Token
3. Copy and paste into GitHub secret

---

## Common Issues & Fixes

### Issue: "javafx:run not found"
```
Solution: Make sure pom.xml has JavaFX Maven plugin
mvn clean compile javafx:run
```

### Issue: "Java 17 not found"
```
Solution: Install Java 17 or update JAVA_HOME
# Set JAVA_HOME (Windows)
set JAVA_HOME=C:\Program Files\Java\jdk-17.0.x

# Set JAVA_HOME (macOS/Linux)
export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk-17.0.x/Contents/Home
```

### Issue: Tests fail
```
Solution: Run with debug output
mvn test -X
```

### Issue: Docker image too large
```
Solution: Already using multi-stage build (optimal)
Typical size: ~400MB
```

---

## File Structure

```
javafx-ci-cd-app/
├── pom.xml                 ← Maven configuration
├── Dockerfile              ← Docker setup
├── docker-compose.yml      ← Docker Compose
├── .gitignore             ← What NOT to commit
├── README.md              ← Full documentation
├── .github/workflows/
│   ├── ci.yml            ← GitHub Actions CI
│   └── cd.yml            ← GitHub Actions CD
└── src/
    ├── main/java/com/example/app/
    │   ├── MainApp.java              ← Entry point
    │   ├── LoginValidator.java       ← Business logic
    │   └── ValidationResult.java     ← Result object
    └── test/java/com/example/app/
        └── LoginValidatorTest.java   ← 30+ tests
```

---

## Key Commands

```bash
# Build & Run
mvn clean javafx:run                # Run app
mvn package                         # Create JAR

# Testing
mvn test                           # Run all tests
mvn test -Dtest=LoginValidatorTest # Run specific test

# Docker
docker build -t javafx-app:1.0 .   # Build image
docker run javafx-app:1.0          # Run container
docker-compose up                  # Use Compose

# Git
git add .                          # Stage changes
git commit -m "message"            # Commit
git push                           # Push to GitHub
```

---

## What Happens When You Push to GitHub?

```
1. CI Pipeline Runs (ci.yml)
   ✓ Checkout code
   ✓ Setup Java 17
   ✓ Compile with Maven
   ✓ Run 30+ unit tests
   ✓ Create JAR artifact
   → Takes ~3-5 minutes

2. If on 'main' branch, CD Pipeline Runs (cd.yml)
   ✓ Build Docker image
   ✓ Push to Docker Hub
   ✓ Deploy to production
   → Takes ~10-15 minutes
```

Check status: GitHub → Actions tab

---

## Next Steps

1. **Understand the code**: Read MainApp.java
2. **Learn validation**: Study LoginValidator.java
3. **Run tests**: Execute `mvn test`
4. **Build Docker image**: `docker build -t javafx-app:1.0 .`
5. **Deploy to GitHub**: Create repo and push
6. **Configure secrets**: Add Docker Hub credentials
7. **Watch CI/CD**: See automatic build and test

---

## Resources

- [Maven Documentation](https://maven.apache.org/guides/)
- [JavaFX Tutorial](https://openjfx.io/openjfx-docs/)
- [JUnit 5 Guide](https://junit.org/junit5/docs/current/user-guide/)
- [Docker Documentation](https://docs.docker.com/)
- [GitHub Actions](https://docs.github.com/en/actions)

---

**Questions?** Check [README.md](README.md) for detailed documentation!
