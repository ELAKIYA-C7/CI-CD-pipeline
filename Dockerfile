# Stage 1: Build
FROM maven:3.9.5-eclipse-temurin-17-focal AS builder

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src

RUN mvn clean package -DskipTests


# Stage 2: Runtime
FROM eclipse-temurin:17-jre-focal

WORKDIR /app

# Install JavaFX
RUN apt-get update && apt-get install -y openjfx

COPY --from=builder /app/target/*.jar app.jar

ENTRYPOINT ["java","-jar","app.jar"]

RUN useradd -m -u 1000 appuser && chown -R appuser:appuser /app
USER appuser

EXPOSE 8080

ENTRYPOINT ["java","--module-path","/usr/share/openjfx/lib","--add-modules","javafx.controls,javafx.fxml","-jar","app.jar"]
