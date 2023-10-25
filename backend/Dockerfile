FROM gradle:8.3.0-jdk17-alpine AS builder
WORKDIR /app
COPY . .
RUN gradle build -x test

FROM eclipse-temurin:17-alpine
WORKDIR /app
COPY --from=builder /app/build/libs/backend-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT [ "java", "-jar", "-Dspring.profiles.active=prod", "app.jar" ]
