FROM maven:3.9.6-eclipse-temurin-17 as build

WORKDIR /build

COPY pom.xml .
COPY src/main/resources/tdlib.jar src/main/resources/tdlib.jar
RUN mvn install:install-file \
    -Dfile=src/main/resources/tdlib.jar \
    -DgroupId=com.telegram \
    -DartifactId=tdlib \
    -Dversion=1.0 \
    -Dpackaging=jar

COPY src ./src
RUN mvn clean package -DskipTests

FROM openjdk:17-alpine
WORKDIR /app
COPY --from=build /app/target/Api-Hallel-0.0.1-SNAPSHOT.jar ./hallel-api.jar
EXPOSE 8080
CMD ["java", "-jar", "hallel-api.jar"]