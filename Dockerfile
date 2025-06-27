# ---------- Build stage ----------
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Copy Maven wrapper files first
COPY mvnw .
COPY .mvn .mvn

# Make sure wrapper is executable
RUN chmod +x mvnw

# Copy pom.xml early to cache dependencies
COPY pom.xml .

# Go offline by resolving dependencies
RUN ./mvnw dependency:go-offline

# Copy rest of the source code
COPY . .

# Package app
RUN ./mvnw -B -DskipTests clean package

# ---------- Runtime stage ----------
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]