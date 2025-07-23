# Use lightweight Java 17 JDK image
FROM eclipse-temurin:17-jdk-alpine

# Set working directory inside the container
WORKDIR /app

# Copy Maven wrapper and pom.xml
COPY .mvn .mvn
COPY mvnw .
COPY pom.xml .

# Download dependencies
RUN ./mvnw dependency:go-offline -B

# Copy the source code
COPY src src

# Package the app (skip tests)
RUN ./mvnw clean package -DskipTests

# Expose the port used by the service
EXPOSE 8080

# Run the generated jar file
CMD ["java", "-jar", "target/auth-service-0.0.1-SNAPSHOT.jar"]
