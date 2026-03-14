# -------- Stage 1: Build --------
FROM maven:3.9.5-eclipse-temurin-17-focal AS builder

WORKDIR /app

# Copy project
COPY . .

# Build the jar
RUN mvn clean package -DskipTests


# -------- Stage 2: Runtime --------
FROM eclipse-temurin:17-jre-focal

WORKDIR /app

# Install JavaFX
RUN apt-get update && apt-get install -y openjfx

# Copy any jar from target folder
COPY --from=builder /app/target/*.jar app.jar

# Run JavaFX application
ENTRYPOINT ["java","--module-path","/usr/share/openjfx/lib","--add-modules","javafx.controls,javafx.fxml","-jar","app.jar"]
