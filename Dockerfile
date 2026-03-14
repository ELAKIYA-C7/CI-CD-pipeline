# -------- Stage 1: Build --------
FROM maven:3.9.5-eclipse-temurin-17-focal AS builder

WORKDIR /app

# Copy pom.xml
COPY pom.xml .

# Download dependencies
RUN mvn dependency:go-offline

# Copy source code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests


# -------- Stage 2: Runtime --------
FROM eclipse-temurin:17-jre-focal

WORKDIR /app

# Install JavaFX
RUN apt-get update && apt-get install -y openjfx

# Copy the built jar
COPY --from=builder /app/target/*.jar app.jar

# Create non-root user
RUN useradd -m -u 1000 appuser && chown -R appuser:appuser /app
USER appuser

# Expose port
EXPOSE 8080

# Run JavaFX app
ENTRYPOINT ["java","--module-path","/usr/share/openjfx/lib","--add-modules","javafx.controls,javafx.fxml","-jar","app.jar"]
