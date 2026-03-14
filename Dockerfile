# Multi-stage Docker build for JavaFX application
# Stage 1: Build with Maven
FROM maven:3.9.5-eclipse-temurin-17-focal AS builder

WORKDIR /app

# Copy pom.xml
COPY pom.xml .

# Download dependencies (cached layer)
RUN mvn dependency:go-offline

# Copy source code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Stage 2: Runtime image
FROM eclipse-temurin:17-jre-focal

WORKDIR /app

# Copy built JAR from builder stage
COPY --from=builder /app/target/*-jar-with-dependencies.jar app.jar

# Create non-root user for security
RUN useradd -m -u 1000 appuser && chown -R appuser:appuser /app
USER appuser

# Expose port (if needed for network operations)
EXPOSE 8080

# Health check (optional)
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
    CMD java -version || exit 1

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
