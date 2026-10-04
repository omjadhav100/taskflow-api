# Stage 1: Build the app using Maven + Java 17
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

# Stage 2: Run the app using a lightweight Java-only image
FROM eclipse-temurin:17-jre
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

# TEMPORARY DEBUG LINE: prints whether DB_URL actually arrived,
# without printing the real password. Remove this line once confirmed working.
CMD ["sh", "-c", "echo '--- DEBUG: DB_URL is =' $DB_URL '---' && echo '--- DEBUG: DB_USERNAME is =' $DB_USERNAME '---' && java -jar app.jar"]