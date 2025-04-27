FROM maven:3.9.6-eclipse-temurin-17 as build

WORKDIR /build

COPY pom.xml .
COPY src/main/resources/tdlib.jar src/main/resources/tdlib.jar
RUN mvn install:install-file \
    -Dfile=src/main/resources/tdlib.jar \
    -DgroupId=com.telegram \
    -DartifactId=tdlib \
    -Dversion=1.0.0 \
    -Dpackaging=jar

COPY src ./src
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /build/target/hallel-api-production.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]