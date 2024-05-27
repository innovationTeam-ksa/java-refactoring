FROM openjdk:21-jdk AS build
WORKDIR /app/source
COPY . .
RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

FROM openjdk:21-jdk
WORKDIR /app
COPY --from=build /app/source/target/*.jar /app/app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
