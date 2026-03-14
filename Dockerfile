# Stage 1: Build
FROM maven:3.9.5-eclipse-temurin-17-focal AS builder

WORKDIR /app

COPY . .

RUN mvn clean package -DskipTests


# Stage 2: Runtime
FROM eclipse-temurin:17-jre-focal

WORKDIR /app

# install JavaFX
RUN apt-get update && apt-get install -y openjfx

# copy the built jar
COPY --from=builder /app/target/javafx-ci-cd-app-1.0.0.jar app.jar

ENTRYPOINT ["java","--module-path","/usr/share/openjfx/lib","--add-modules","javafx.controls,javafx.fxml","-jar","app.jar"]
