FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY .mvn .mvn
COPY mvnw .
COPY pom.xml .

RUN ./mvnw dependency:go-offline -B

COPY src src

RUN ./mvnw clean package -DskipTests

EXPOSE 8081

CMD ["java", "-jar", "target/gateway-service-0.0.1-SNAPSHOT.jar"]
