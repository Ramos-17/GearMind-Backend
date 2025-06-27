# Use a valid Maven image
FROM maven:3.8.6-eclipse-temurin-21 AS build
WORKDIR /app

# Copy pom.xml first for better caching
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copy the built jar
COPY --from=build /app/target/*.jar app.jar

# Create data directory for H2 database
RUN mkdir -p /app/data

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]