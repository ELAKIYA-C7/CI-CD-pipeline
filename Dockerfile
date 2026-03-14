# -------- Stage 1: Build the application --------
FROM maven:3.9.5-eclipse-temurin-17-focal AS builder

WORKDIR /app

# Copy pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy source code
COPY src ./src

# Build the jar file
RUN mvn clean package -DskipTests


# -------- Stage 2: Create runtime image --------
FROM eclipse-temurin:17-jre-focal

WORKDIR /app

# Install JavaFX runtime
RUN apt-get update && apt-get install -y openjfx

# Copy jar file from builder stage
COPY --from=builder /app/target/*.jar app.jar

# Create non-root user (security best practice)
RUN useradd -m -u 1000 appuser && chown -R appuser:appuser /app
USER appuser

# Expose port (optional)
EXPOSE 8080

# Run the JavaFX application
ENTRYPOINT ["java","--module-path","/usr/share/openjfx/lib","--add-modules","javafx.controls,javafx.fxml","-jar","app.jar"]
