# Build Stage
FROM maven:3.6.3-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean install

# Runtime Stage
FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/MiniHackerNews-0.0.1-SNAPSHOT.jar ./application.jar
CMD ["java", "-jar", "application.jar"]