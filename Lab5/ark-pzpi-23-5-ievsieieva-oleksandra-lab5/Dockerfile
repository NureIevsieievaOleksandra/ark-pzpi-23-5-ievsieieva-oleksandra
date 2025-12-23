FROM gradle:9.1.0-jdk21 AS builder

WORKDIR /app
COPY . .
RUN gradle build --no-daemon
#RUN gradle build --no-daemon --stacktrace --info

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]