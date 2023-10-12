FROM maven:3.9.1-eclipse-temurin-17-alpine AS builder
WORKDIR /app
COPY . .
RUN mvn clean install -DskipTests

FROM eclipse-temurin:17-alpine
WORKDIR /app
COPY --from=builder /app/target/backend-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT [ "java", "-jar", "-Dspring.profiles.active=prod", "app.jar" ]
