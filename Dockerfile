# Build Stage
FROM openjdk:17-jdk-alpine AS builder
COPY pom.xml /app/
COPY src /app/src

# Install Maven
RUN apk add --no-cache maven
RUN mvn -f /app/pom.xml clean package -DskipTests

# Final Stage
FROM openjdk:17-jdk-alpine
COPY --from=builder /app/target/MiniHackerNews-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
