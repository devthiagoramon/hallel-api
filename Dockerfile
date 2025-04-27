
# BUILD
FROM maven:3.8.4-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# RUN
FROM openjdk:17-alpine
WORKDIR /app
COPY --from=build /app/target/Api-Hallel-0.0.1-SNAPSHOT.jar ./hallel-api.jar
EXPOSE 8080
CMD ["java", "-jar", "hallel-api.jar"]
