# syntax = docker/dockerfile:1.4
FROM gradle:jdk21 AS builder
WORKDIR /app

COPY gradlew .
COPY gradle/ gradle/
RUN chmod +x ./gradlew

# show project files to ensure context is correct
RUN ls -la /app || true

COPY . .

# build bootJar, skip tests and test compilation; do not redirect output so failing stacktrace is shown
RUN --mount=type=cache,target=/home/gradle/.gradle \
  ./gradlew clean bootJar -x test -x testClasses --no-daemon --stacktrace --info

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar /app/app.jar
USER 1000
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]