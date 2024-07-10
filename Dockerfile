#FROM openjdk:17-jdk-alpine3.13
#WORKDIR /app
#COPY . .
#RUN chmod +x ./gradlew
#RUN ./gradlew clean build -x test
#ARG JAR_FILE=build/libs/*.jar
#COPY ${JAR_FILE} app.jar
#EXPOSE 8080
#ENTRYPOINT ["java","-jar","/app.jar"]

# Stage 1: Build the application
FROM gradle:7.3.3-jdk17-alpine AS build
WORKDIR /app
COPY --chown=gradle:gradle . .
RUN ./gradlew clean build -x test

# Stage 2: Create the final image
FROM openjdk:17-jdk-alpine3.13
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

