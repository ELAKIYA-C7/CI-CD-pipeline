# -------- Stage 1: Build --------
FROM maven:3.9.5-eclipse-temurin-17-focal AS builder

WORKDIR /app

# Copy everything
COPY . .

# Ensure all dependencies download and build fat JAR
RUN mvn clean package -DskipTests

# -------- Stage 2: Run --------
FROM eclipse-temurin:17-jre-focal

WORKDIR /app

# Install JavaFX
RUN apt-get update && apt-get install -y openjfx && rm -rf /var/lib/apt/lists/*

# Copy the built fat JAR (with dependencies)
COPY --from=builder /app/target/*-jar-with-dependencies.jar app.jar

# Run with JavaFX modules
ENTRYPOINT ["java", "--module-path", "/usr/share/openjfx/lib", "--add-modules", "javafx.controls,javafx.fxml", "-jar", "app.jar"]
