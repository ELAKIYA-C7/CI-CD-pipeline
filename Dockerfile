# -------- Stage 1: Build --------
FROM maven:3.9.5-eclipse-temurin-17-focal AS builder

WORKDIR /app

# Copy all project files
COPY . .

# Build fat JAR (app.jar)
RUN mvn clean package -DskipTests

# -------- Stage 2: Run --------
FROM eclipse-temurin:17-jre-focal

WORKDIR /app

# Install JavaFX runtime
RUN apt-get update && apt-get install -y openjfx && rm -rf /var/lib/apt/lists/*

# Copy the fat JAR from builder stage
COPY --from=builder /app/target/app.jar app.jar

# Expose port if needed (optional)
EXPOSE 8080

# Run the JavaFX application
ENTRYPOINT ["java", "--module-path", "/usr/share/openjfx/lib", "--add-modules", "javafx.controls,javafx.fxml", "-jar", "app.jar"]
