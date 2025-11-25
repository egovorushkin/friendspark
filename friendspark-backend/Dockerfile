# Backend Dockerfile: builds and runs the Spring Boot Kotlin app

# ---- Build stage ----
FROM gradle:8.10.2-jdk21 AS build

WORKDIR /workspace

# Copy Gradle wrapper and configuration
COPY build.gradle.kts settings.gradle.kts gradlew gradlew.bat /workspace/
COPY gradle /workspace/gradle

# Copy the actual backend sources
COPY src /workspace/src

# Build the Spring Boot jar (bootJar task)
RUN ./gradlew bootJar --no-daemon

# ---- Runtime stage ----
FROM eclipse-temurin:21-jre-alpine AS runtime

WORKDIR /app

# Copy the built jar from the build stage
COPY --from=build /workspace/build/libs/*-SNAPSHOT.jar /app/app.jar

EXPOSE 8090

ENTRYPOINT ["java", "-jar", "/app/app.jar"]

