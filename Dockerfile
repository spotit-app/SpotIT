FROM node:20.8.0-alpine3.18 AS frontend-builder

WORKDIR /app
RUN npm install -g pnpm
COPY frontend/package.json .
RUN pnpm i
COPY frontend/ .
RUN pnpm build

FROM gradle:8.3.0-jdk17-alpine AS backend-builder
WORKDIR /app
COPY backend/ .
COPY --from=frontend-builder /app/dist/ /app/src/main/resources/static/

RUN gradle build -x test --no-daemon

FROM eclipse-temurin:17-alpine
WORKDIR /app
COPY --from=backend-builder /app/build/libs/backend-0.0.1-SNAPSHOT.jar app.jar

HEALTHCHECK --interval=30s --timeout=3s \ 
    CMD wget -qO- http://localhost:8080/actuator/health || exit 1

RUN adduser -D app
USER app

EXPOSE 8080

ENTRYPOINT [ "java", "-jar", "-Dspring.profiles.active=prod", "app.jar" ]
