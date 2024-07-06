# Aşama 1: Build aşaması
FROM ubuntu:20.04 AS build

# Gerekli paketleri yükleyin
RUN apt-get update && apt-get install -y \
    openjdk-17-jdk \
    wget \
    unzip

# Gradle'ı yükleyin
RUN wget https://services.gradle.org/distributions/gradle-7.4.2-bin.zip -P /tmp \
    && unzip -d /opt/gradle /tmp/gradle-7.4.2-bin.zip \
    && rm /tmp/gradle-7.4.2-bin.zip

ENV GRADLE_HOME=/opt/gradle/gradle-7.4.2
ENV PATH=${GRADLE_HOME}/bin:${PATH}

# Çalışma dizinini ayarlayın
WORKDIR /home/gradle/project

# Proje dosyalarını kopyalayın
COPY . .

# Gradle Wrapper'ın yürütülebilir olmasını sağlayın
RUN chmod +x gradlew

# Projeyi derleyin
RUN ./gradlew clean build -x test --no-daemon

# Aşama 2: Run aşaması
FROM openjdk:17-jdk-slim

# Çalışma dizinini ayarlayın
WORKDIR /app

# Derlenmiş JAR dosyasını kopyalayın
COPY --from=build /home/gradle/project/build/libs/*.jar app.jar

# Uygulama portunu açın
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "app.jar"]

