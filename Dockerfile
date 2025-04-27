FROM maven:3.9.6-eclipse-temurin-17 as build

WORKDIR /build

COPY pom.xml .
COPY libs/tdlib.jar libs/tdlib.jar
RUN mvn install:install-file \
    -Dfile=libs/tdlib.jar \
    -DgroupId=com.devthiagoramon \
    -DartifactId=tdlib \
    -Dversion=1.0.0 \
    -Dpackaging=jar

COPY src ./src
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /build/target/hallel-api-production.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]