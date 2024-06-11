# Stage 1: Build
FROM gradle:8.8.0-jdk-21-and-22-jammy AS build
WORKDIR /home/app

COPY build.gradle /home/app
COPY gradle /home/app/gradle
COPY gradlew /home/app
COPY settings.gradle /home/app
# Download dependencies (this will be cached if the dependencies do not change)
RUN ./gradlew dependencies

COPY src /home/app/src
RUN ./gradlew clean build -x test

# Stage 2: Run
FROM eclipse-temurin:21-jre-alpine
RUN apk update && apk upgrade --no-cache
VOLUME /tmp
ARG DEPENDENCY=/home/app/build/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app
ENTRYPOINT exec java -XX:+UseContainerSupport ${JAVA_ARGS} -cp app:app/lib/* com.innovationteam.task.MovieRentalApp
