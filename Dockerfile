###############################################################################
#
# Copyright (c) 2023 Tal Aharon
#
# Permission is hereby granted, free of charge, to any person obtaining a copy
# of this software and associated documentation files (the "Software"), to deal
# in the Software without restriction, including without limitation the rights
# to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
# copies of the Software, and to permit persons to whom the Software is
# furnished to do so, subject to the following conditions:
#
# The above copyright notice and this permission notice shall be included in all
# copies or substantial portions of the Software.
#
# THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
# IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
# FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
# AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
# LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
# OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
# SOFTWARE.

###############################################################################

################################################################################
# Dockerfile for MiniHackerNews REST API Application
#
# This Dockerfile defines the build and final stages for the MiniHackerNews
# application. It uses multi-stage building to efficiently build and package
# the Java application using Maven, then creates a lightweight runtime
# container to run the application.
#
# Steps:
# 1. Build Stage (Builder):
#    - Use the 'openjdk:17-jdk-alpine' base image for the build stage.
#    - Copy the project's 'pom.xml' and source code from the host to the '/app/'
#      directory in the container.
#    - Install Maven in the container using Alpine Linux package manager 'apk'.
#    - Build the application using Maven with 'clean package' command, skipping
#      tests.
#
# 2. Final Stage:
#    - Use another 'openjdk:17-jdk-alpine' base image for the final stage,
#      creating a lightweight runtime container.
#    - Copy the compiled JAR file from the build stage to the root directory of
#      the final container and name it 'app.jar'.
#    - Expose port 8080 to allow external access to the application.
#    - Set the entry point to run the Java application by executing 'java -jar app.jar'.
#
################################################################################

# Build Stage
FROM openjdk:17-jdk-alpine AS builder
COPY pom.xml /app/
COPY src /app/src

# Install with Maven
RUN apk add --no-cache maven
RUN mvn -f /app/pom.xml clean package -DskipTests

# Final Stage
FROM openjdk:17-jdk-alpine
COPY --from=builder /app/target/MiniHackerNews-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
