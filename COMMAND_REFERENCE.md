# Maven Commands Reference Card

## Build Commands

```bash
# Full clean rebuild
mvn clean install

# Compile only (no tests)
mvn clean compile

# Compile and run tests
mvn clean test

# Compile, test, and package
mvn clean package

# Compile, test, package, and install locally
mvn clean install
```

## Run Application

```bash
# Run JavaFX application (BEST FOR DEV)
mvn clean javafx:run

# Build and run JAR
mvn clean package
java -jar target/javafx-ci-cd-app-1.0.0-jar-with-dependencies.jar

# Fast run (skip tests)
mvn javafx:run -DskipTests
```

## Testing

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=LoginValidatorTest

# Run specific test method
mvn test -Dtest=LoginValidatorTest#testValidUsername

# Run tests with debug output
mvn test -X

# Skip tests during build
mvn package -DskipTests
```

## Dependency Management

```bash
# Show dependency tree
mvn dependency:tree

# Download all dependencies
mvn dependency:go-offline

# Show dependency updates available
mvn versions:display-dependency-updates

# Update all dependencies to latest
mvn versions:use-latest-versions
```

## Clean & Rebuild

```bash
# Remove all generated files
mvn clean

# Remove specific directory
mvn clean -DskipTests

# Full rebuild
mvn clean rebuild
```

---

# Docker Commands Reference Card

## Build

```bash
# Build image with tag
docker build -t javafx-app:1.0 .

# Build with specific Dockerfile
docker build -f Dockerfile -t javafx-app:1.0 .

# Build with no-cache (force rebuild)
docker build --no-cache -t javafx-app:1.0 .

# Build and push to registry
docker build -t username/javafx-app:1.0 .
docker push username/javafx-app:1.0
```

## Run

```bash
# Run container
docker run javafx-app:1.0

# Run in background (detached)
docker run -d --name myapp javafx-app:1.0

# Run with port mapping
docker run -p 8080:8080 javafx-app:1.0

# Run with environment variables
docker run -e JAVA_OPTS="-Xmx512m" javafx-app:1.0

# Run with volume mount
docker run -v /host/path:/container/path javafx-app:1.0

# Run and remove container after exit
docker run --rm javafx-app:1.0
```

## Container Management

```bash
# List running containers
docker ps

# List all containers (including stopped)
docker ps -a

# Stop container
docker stop myapp

# Start container
docker start myapp

# Remove container
docker rm myapp

# Remove image
docker rmi javafx-app:1.0

# View logs
docker logs myapp

# View live logs
docker logs -f myapp

# Get container details
docker inspect myapp

# Execute command in container
docker exec -it myapp /bin/bash

# View resource usage
docker stats myapp
```

## Image Management

```bash
# List images
docker images

# Tag image
docker tag javafx-app:1.0 javafx-app:latest

# Push image to registry
docker push username/javafx-app:1.0

# Pull image from registry
docker pull username/javafx-app:1.0

# Remove unused images
docker image prune

# Show image history
docker history javafx-app:1.0

# Show image size
docker images --format "table {{.Repository}}\t{{.Size}}"
```

## Docker Compose

```bash
# Start services
docker-compose up

# Start in background
docker-compose up -d

# Stop services
docker-compose stop

# Stop and remove containers
docker-compose down

# View service status
docker-compose ps

# View logs
docker-compose logs

# View live logs
docker-compose logs -f

# Rebuild images
docker-compose build

# Remove volumes
docker-compose down -v
```

---

# Git Commands Reference Card

## Initial Setup

```bash
# Initialize repository
git init

# Configure user
git config user.name "Your Name"
git config user.email "your.email@example.com"

# Set globally (all repos)
git config --global user.name "Your Name"
git config --global user.email "your.email@example.com"
```

## Basic Workflow

```bash
# Check status
git status

# See what changed
git diff

# Stage all changes
git add .

# Stage specific file
git add src/main/java/com/example/app/MainApp.java

# Unstage file
git reset src/main/java/com/example/app/MainApp.java

# Commit changes
git commit -m "Add feature: login validation"

# Commit with detailed message
git commit -m "Fix: login validation

- Added username format validation
- Added password length check
- Improved error messages"

# View commit history
git log

# View concise log
git log --oneline

# View log with graph
git log --graph --oneline --all
```

## Remote Repository

```bash
# Add remote (GitHub)
git remote add origin https://github.com/username/javafx-ci-cd-app.git

# View remotes
git remote -v

# Push to remote
git push origin main

# Push to GitHub (first time)
git branch -M main
git push -u origin main

# Pull from remote
git pull origin main

# Fetch without merging
git fetch origin
```

## Branches

```bash
# Create new branch
git branch feature/new-feature

# Switch to branch
git checkout feature/new-feature

# Create and switch
git checkout -b feature/new-feature

# List branches
git branch

# List remote branches
git branch -r

# Delete branch (local)
git branch -d feature/new-feature

# Merge branch
git merge feature/new-feature

# Rebase branch
git rebase main
```

## Undoing Changes

```bash
# Discard changes in working directory
git checkout -- src/file.java

# Unstage file
git reset src/file.java

# Undo last commit (keep changes)
git reset --soft HEAD~1

# Undo last commit (discard changes)
git reset --hard HEAD~1

# Revert commit (create new commit)
git revert HEAD

# Stash changes temporarily
git stash

# Apply stashed changes
git stash pop

# List stashes
git stash list
```

---

# GitHub Actions Commands Reference Card

## Workflow Triggers

```yaml
# Trigger on push to main branch
on:
  push:
    branches: [ main ]

# Trigger on pull request
on:
  pull_request:
    branches: [ main ]

# Trigger on schedule (cron)
on:
  schedule:
    - cron: '0 2 * * *'  # Daily at 2 AM UTC

# Trigger manually
on:
  workflow_dispatch:

# Trigger on release
on:
  release:
    types: [published]
```

## Common Actions

```yaml
# Checkout code
- uses: actions/checkout@v4

# Setup Java
- uses: actions/setup-java@v4
  with:
    java-version: '17'
    distribution: 'temurin'

# Setup Node
- uses: actions/setup-node@v4
  with:
    node-version: '18'

# Run command
- name: Run test
  run: mvn test

# Upload artifact
- uses: actions/upload-artifact@v3
  with:
    name: my-artifact
    path: target/*.jar

# Download artifact
- uses: actions/download-artifact@v3
  with:
    name: my-artifact

# Cache dependencies
- uses: actions/cache@v3
  with:
    path: ~/.m2
    key: ${{ runner.os }}-maven-${{ hashFiles('**/pom.xml') }}
```

## Job Configuration

```yaml
jobs:
  build:
    runs-on: ubuntu-latest
    strategy:
      matrix:
        java-version: [17, 18]
    steps:
      - name: Checkout
        uses: actions/checkout@v4
```

---

# Common Issues & Solutions

## Maven Issues

**Issue**: "Command not found: mvn"
```bash
# Solution: Add Maven to PATH or use full path
/usr/local/maven/bin/mvn --version
```

**Issue**: "Java version mismatch"
```bash
# Solution: Set JAVA_HOME
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk
```

**Issue**: "OutOfMemoryError during build"
```bash
# Solution: Increase heap size
export MAVEN_OPTS="-Xmx1024m"
```

## Docker Issues

**Issue**: "Cannot connect to Docker daemon"
```bash
# Solution: Start Docker
systemctl start docker  # Linux
open /Applications/Docker.app  # macOS
```

**Issue**: "Permission denied"
```bash
# Solution: Add user to docker group
sudo usermod -aG docker $USER
sudo systemctl restart docker
```

## Git Issues

**Issue**: "Permission denied (publickey)"
```bash
# Solution: Setup SSH keys
ssh-keygen -t ed25519 -C "your.email@example.com"
cat ~/.ssh/id_ed25519.pub  # Copy to GitHub SSH keys
```

**Issue**: "Merge conflict"
```bash
# Solution: Resolve and commit
git status  # See conflicts
# Edit files to resolve
git add .
git commit -m "Resolve merge conflict"
```

---

# Performance Tips

## Maven

- Use `-o` (offline mode) if all deps cached
- Use `-T 1C` for parallel builds (1 thread per core)
- Skip tests in dev: `-DskipTests`
- Skip javadoc: `-Dskip.javadoc=true`

## Docker

- Use `.dockerignore` to exclude files
- Layer dependencies first in Dockerfile
- Use multi-stage builds for smaller images
- Pin base image versions (not `latest`)

## Git

- Use `.gitignore` to exclude unnecessary files
- Keep commits small and logical
- Write meaningful commit messages
- Use branches for features

---

This reference card covers 90% of common commands!
Print or bookmark for quick reference.
