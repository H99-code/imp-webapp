
FROM eclipse-temurin:17-jdk-alpine as build
WORKDIR /workspace/app
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src
RUN ./mvnw install -DskipTests && mv target/*.jar target/app.jar
FROM eclipse-temurin:17-jdk-alpine
COPY --from=build /workspace/app/target/app.jar /app/app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]