FROM openjdk:17-jdk-alpine
ARG JARFILE=target/*.jar
COPY ./target/MiniHackerNews-0.0.1.jar /app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]