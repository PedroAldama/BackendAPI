
FROM eclipse-temurin:21-jdk-alpine
LABEL authors="Pedro"

VOLUME /tmp

COPY target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app.jar"]
