# Architecture & Design Documentation

## Application Architecture

### Layered Architecture

```
┌─────────────────────────────────────────────┐
│           Presentation Layer                │
│  (JavaFX GUI - MainApp.java)               │
│  - Login Form UI                            │
│  - User Input Handling                      │
│  - Status Display                           │
└────────────────┬────────────────────────────┘
                 │
                 ▼
┌─────────────────────────────────────────────┐
│           Business Logic Layer              │
│  (LoginValidator.java)                      │
│  - Input Validation                         │
│  - Business Rules                           │
│  - Error Handling                           │
└────────────────┬────────────────────────────┘
                 │
                 ▼
┌─────────────────────────────────────────────┐
│           Data Layer (Future)               │
│  - Database Connection                      │
│  - User Repository                          │
│  - Authentication Service                   │
└─────────────────────────────────────────────┘
```

### Class Diagram

```
┌──────────────────────────────┐
│        MainApp (extends Application)
├──────────────────────────────┤
│ - usernameField: TextField   │
│ - passwordField: PasswordField│
│ - statusLabel: Label         │
├──────────────────────────────┤
│ + start(Stage): void         │
│ - handleLogin(): void        │
│ - showSuccessDialog(String)  │
│ + main(String[]): void       │
└──────────┬───────────────────┘
           │ uses
           ▼
┌──────────────────────────────┐
│   LoginValidator (static)     │
├──────────────────────────────┤
│ - MIN_USERNAME_LENGTH = 3    │
│ - MAX_USERNAME_LENGTH = 20   │
│ - MIN_PASSWORD_LENGTH = 6    │
│ - MAX_PASSWORD_LENGTH = 30   │
├──────────────────────────────┤
│ + validateCredentials(...)   │
│ + validateUsername(...)      │
│ + validatePassword(...)      │
└──────────┬───────────────────┘
           │ returns
           ▼
┌──────────────────────────────┐
│   ValidationResult (immutable)
├──────────────────────────────┤
│ - valid: boolean             │
│ - errorMessage: String       │
├──────────────────────────────┤
│ + isValid(): boolean         │
│ + getErrorMessage(): String  │
│ + toString(): String         │
└──────────────────────────────┘
```

### Validation Flow

```
User Input (username, password)
           │
           ▼
    ┌──────────────┐
    │  handleLogin │
    └──────┬───────┘
           │
           ▼
    ┌──────────────────────────────┐
    │  validateCredentials()       │
    │  - Check empty inputs        │
    │  - Check length constraints  │
    │  - Check format (regex)      │
    └──────────────┬───────────────┘
                   │
           ┌───────┴────────┐
           │                │
    Valid ▼                  ▼ Invalid
    ┌────────────┐    ┌──────────────────┐
    │ Success    │    │ Error Message    │
    │ Message    │    │ (specific cause) │
    └────────────┘    └──────────────────┘
           │                   │
           └───────┬───────────┘
                   │
                   ▼
           Update UI with Result
```

---

## Build & Deployment Pipeline

### Maven Build Process

```
pom.xml
   │
   ▼
┌─────────────┐
│  COMPILE    │  - Compile src/main/java
└──────┬──────┘  - Generate target/classes
       │
       ▼
┌─────────────┐
│   TEST      │  - Compile src/test/java
└──────┬──────┘  - Run JUnit tests
       │         - Generate test reports
       │
       ▼
┌─────────────┐
│  PACKAGE    │  - Create JAR file
└──────┬──────┘  - Include all dependencies
       │         - Set Main-Class in manifest
       │
       ▼
┌─────────────┐
│   TARGET    │  - JAR with all deps
└─────────────┘  - Ready for execution
```

### CI/CD Pipeline Flow

```
┌──────────────────────┐
│  Developer Push      │
│  (git push origin)   │
└──────────┬───────────┘
           │
           ▼
┌──────────────────────┐
│  GitHub Webhook      │
│  (Trigger)           │
└──────────┬───────────┘
           │
    ┌──────┴──────┐
    │ Push on dev?│
    └──────┬──────┘
           │ (Yes)
           ▼
┌──────────────────────┐
│  CI Pipeline         │  ~5 minutes
│  (ci.yml)            │
│  ✓ Checkout         │
│  ✓ Setup Java       │
│  ✓ Build            │
│  ✓ Test (30+)       │
│  ✓ Package          │
└──────────┬───────────┘
           │
      ┌────┴─────┐
      │ Passed?  │
      └────┬─────┘
           │ (Yes)
           ▼
┌──────────────────────┐
│  Push on Main?       │
└──────────┬───────────┘
           │ (Yes)
           ▼
┌──────────────────────┐
│  CD Pipeline         │  ~15 minutes
│  (cd.yml)            │
│  ✓ Build Docker img  │
│  ✓ Push to Hub       │
│  ✓ Deploy to K8s     │
│  ✓ Health checks     │
└──────────┬───────────┘
           │
           ▼
┌──────────────────────┐
│  Production Ready    │
│  docker.io repo      │
└──────────────────────┘
```

### Docker Multi-Stage Build

```
STAGE 1: BUILDER
┌────────────────────────────┐
│  FROM maven:3.9.5          │
│  - Alpine Linux base       │
│  - Maven installed         │
│  - Java 17 JDK             │
│                            │
│  COPY pom.xml              │
│  RUN mvn dependency:download
│                            │  <- Cached Layer
│  COPY src/                 │
│  RUN mvn clean package     │
│                            │
│  OUTPUT: /app/target/*.jar │
└────────────────────────────┘
         │
         │ (COPY from builder)
         ▼
STAGE 2: RUNTIME
┌────────────────────────────┐
│  FROM eclipse-temurin:17   │
│  - Minimal Java 17 JRE     │
│  - No build tools          │
│  - Lean & secure           │
│                            │
│  COPY --from=builder       │
│    /app/target/*.jar       │
│                            │
│  USER appuser (non-root)   │
│  ENTRYPOINT [java -jar]    │
│                            │
│  SIZE: ~400MB              │
└────────────────────────────┘
```

---

## Test Coverage Strategy

### Test Pyramid

```
                  ▲
                 /│\
                / │ \
               /  │  \  End-to-End Tests (5%)
              /   │   \  - Full app flows
             /    │    \
            /─────┼─────\
           /      │      \  Integration Tests (15%)
          /       │       \ - Component interactions
         /        │        \
        /─────────┼─────────\
       /          │          \ Unit Tests (80%)
      /           │           \ - LoginValidator
     /            │            \ - ValidationResult
    /─────────────┼─────────────\
   /              │              \
  ▼───────────────▼───────────────▼

Cost per test: High → Low
Execution speed: Slow → Fast
Maintenance: Hard → Easy
```

### Test Categories Implemented

**Unit Tests (30+):**
```
✓ Empty/null inputs
✓ Length constraints
✓ Format validation (regex)
✓ Combined credentials
✓ Edge cases
```

**Test Execution Flow:**
```
Test Suite
    │
    ├── Username Tests (10)
    │   ├── Empty username
    │   ├── Null username
    │   ├── Too short (< 3)
    │   ├── Too long (> 20)
    │   ├── Special characters
    │   ├── Valid letters
    │   ├── Valid numbers
    │   ├── Valid underscores
    │   └── Edge cases
    │
    ├── Password Tests (8)
    │   ├── Empty password
    │   ├── Null password
    │   ├── Too short (< 6)
    │   ├── Too long (> 30)
    │   ├── Valid password
    │   └── Edge cases
    │
    └── Integration Tests (12)
        ├── Both empty
        ├── Valid + invalid combo
        ├── Invalid + valid combo
        ├── Both valid
        ├── Minimum length
        └── Special characters
```

---

## Deployment Architecture

### Single Server Deployment

```
┌─────────────────────────────────────┐
│       Docker Host / VM              │
│  (Linux, 2GB RAM, 10GB Storage)    │
├─────────────────────────────────────┤
│  Docker Daemon                      │
│  ┌─────────────────────────────┐   │
│  │  javafx-app Container       │   │
│  │  ┌───────────────────────┐  │   │
│  │  │ PID Namespace         │  │   │
│  │  │ ┌─────────────────┐   │  │   │
│  │  │ │ Java Process    │   │  │   │
│  │  │ │ JVM Heap: 512MB │   │  │   │
│  │  │ │ appuser (non-root)
│  │  │ │ Port: 8080      │   │  │   │
│  │  │ └─────────────────┘   │  │   │
│  │  │ Network Namespace    │  │   │
│  │  │ Mount Namespace      │  │   │
│  │  └───────────────────────┘  │   │
│  └─────────────────────────────┘   │
│  Volume Storage (app.jar)           │
└─────────────────────────────────────┘
         │
         ▼
    Port 8080
    (Public Access)
```

### Kubernetes Deployment (Production)

```
┌────────────────────────────────────────────────────┐
│         Kubernetes Cluster (GKE/EKS/AKS)          │
├────────────────────────────────────────────────────┤
│  ┌──────────────────────────────────────────────┐  │
│  │         Deployment (3 replicas)              │  │
│  │  ┌────────┐  ┌────────┐  ┌────────┐         │  │
│  │  │ Pod 1  │  │ Pod 2  │  │ Pod 3  │         │  │
│  │  │Container
│  │  │ javafxapp:latest      │         │  │
│  │  │ ┌──────┐ │ ┌──────┐ │ │ ┌──────┐ │  │
│  │  │ │8080  │ │ │8080  │ │ │ │8080  │ │  │
│  │  │ └──────┘ │ └──────┘ │ │ └──────┘ │  │
│  │  └────────┘  └────────┘  └────────┘  │  │
│  └───────────────────┬───────────────────┘  │
│                      │                      │
│  ┌──────────────────┴──────────────────┐   │
│  │      LoadBalancer Service           │   │
│  │      Port: 80/443 → 8080            │   │
│  └──────────────────┬──────────────────┘   │
└─────────────────────┼────────────────────────┘
                      │
                      ▼
                  Internet
            (Public Access)
```

---

## Security Architecture

### Security Layers

```
┌─────────────────────────────────────────┐
│  Network Layer                          │
│  - Firewall Rules (iptables)            │
│  - Network Policies                     │
│  - Ingress Controller (TLS termination) │
└─────────────────────┬───────────────────┘
                      │
┌─────────────────────▼───────────────────┐
│  Application Layer                      │
│  - Input Validation (LoginValidator)    │
│  - Error Handling                       │
│  - Logging                              │
└─────────────────────┬───────────────────┘
                      │
┌─────────────────────▼───────────────────┐
│  Container Layer                        │
│  - Non-root user (appuser)              │
│  - Read-only filesystem                 │
│  - Resource limits (CPU, Memory)        │
│  - Security scanning                    │
└─────────────────────┬───────────────────┘
                      │
┌─────────────────────▼───────────────────┐
│  Host Layer                             │
│  - OS Security updates                  │
│  - Kernel hardening                     │
│  - SELinux / AppArmor                   │
└─────────────────────────────────────────┘
```

### CI/CD Security

```
GitHub Repository
      │
      ├─→ Code Review
      │   └─ Pull Request checks
      │   └─ Require approvals
      │
      ├─→ Branch Protection
      │   └─ No direct push to main
      │   └─ Require passing checks
      │
      ├─→ Secrets Management
      │   └─ GitHub Secrets encrypted
      │   └─ Docker access tokens
      │   └─ Never in source code
      │
      └─→ Dependency Scanning
          └─ Maven dependence check
          └─ GitHub security alerts
```

---

## Monitoring & Observability

### Logging Strategy

```
Application
    ↓
┌──────────────────────┐
│  Event Generated     │
│  - Login attempt     │
│  - Validation error  │
│  - Exception         │
└─────────┬────────────┘
          │
┌─────────▼────────────┐
│  Log Level           │
│  - DEBUG (dev)       │
│  - INFO (events)     │
│  - WARN (issues)     │
│  - ERROR (failures)  │
└─────────┬────────────┘
          │
┌─────────▼────────────┐
│  Storage Options     │
│  - Files (logs/)     │
│  - ELK Stack         │
│  - CloudWatch        │
│  - Datadog           │
└────────────────────────┘
```

### Health Checks

```
┌──────────────────────────────┐
│  Container Health Check      │
│  (Every 30 seconds)          │
├──────────────────────────────┤
│  Probes:                     │
│  ✓ Liveness Probe            │
│    - Is JVM running?         │
│  ✓ Readiness Probe           │
│    - Can accept requests?    │
│  ✓ Startup Probe             │
│    - App initialized?        │
├──────────────────────────────┤
│  Action if Failed:           │
│  - Retry 3 times             │
│  - Auto-restart container    │
│  - Alert monitoring team     │
└──────────────────────────────┘
```

---

## Performance Optimization

### JVM Tuning

```bash
# Current Configuration
java -jar app.jar

# Optimized Configuration
java -Xmx512m \        # Max heap: 512MB
     -Xms256m \        # Initial heap: 256MB
     -XX:+UseG1GC \    # Garbage collector
     -jar app.jar
```

### Build Optimization

```
First Build:  3-4 minutes (downloads deps)
              └─ Cached dependencies
              
Subsequent:   30-60 seconds (uses cache)
```

---

## Scalability Strategy

### Vertical Scaling
- ❌ Not recommended for modern apps
- Increase heap size only as last resort
- Better: Add more instances

### Horizontal Scaling
- ✓ Run N containers
- ✓ Load balancer distributes traffic
- ✓ Each instance independent

### Database Scaling (Future)
- ✓ Connection pooling
- ✓ Read replicas
- ✓ Sharding for large datasets

---

## Disaster Recovery

### Backup Strategy

```
Source Code
├─ GitHub (primary)
├─ GitHub Backup (automated)
└─ Local backup (developer)

Docker Images
├─ Docker Hub (primary)
├─ Private registry (backup)
└─ Automated snapshots

Database (Future)
├─ Daily snapshots
├─ Cross-region replication
└─ Point-in-time recovery
```

### Recovery Procedure

```
Issue Detected
      ↓
1. Identify Problem
   - Check logs
   - Review metrics
   
2. Assess Impact
   - Users affected
   - Data integrity
   
3. Initiate Recovery
   - Option 1: Rollback (60 sec)
   - Option 2: Scale up (5 min)
   - Option 3: Rebuild (15 min)
   
4. Verify Fix
   - Health checks
   - Smoke tests
   - User testing
```

---

This document serves as both design reference and interview preparation material!
