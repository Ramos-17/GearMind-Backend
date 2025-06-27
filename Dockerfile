# ---------- Build stage ----------
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Copy only necessary files first for better layer caching
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

RUN chmod +x mvnw && ./mvnw dependency:go-offline

# Now copy the rest of the source code
COPY . .

RUN ./mvnw -B -DskipTests clean package

# ---------- Runtime stage ----------
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]