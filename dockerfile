dockerfile
Copy code
# Stage 1: Build
FROM maven:3.8.1-openjdk-21 AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package

# Stage 2: Run
FROM openjdk:21-jdk-slim
COPY --from=build /home/app/target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]