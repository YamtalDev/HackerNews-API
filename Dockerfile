# Use an official OpenJDK runtime as a parent image
FROM adoptopenjdk:alpine-jre

# Set the working directory in the container
WORKDIR /app

ARG JAR_FILE=target/your-api-app.jar

# Copy the JAR file into the container at /app
COPY  ${JAR_FILE} your-api-app.jar

# Expose the port your Spring Boot application will run on
EXPOSE 8080

# Specify the command to run your Spring Boot application when the container starts
CMD ["java", "-jar", "your-api-app.jar"]
