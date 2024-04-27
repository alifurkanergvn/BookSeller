FROM ubuntu:latest AS build
RUN apt-get update
RUN apt-get install openjdk-17-jdk -y
COPY . .
RUN ./gradlew bootJar --no-deamon

FROM openjdk:17-jdk-slim
EXPOSE 8080
COPY --from=build build/libs/*.jar app.jar

ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-Dserver.port=$PORT", "-jar", "app.jar"]
