FROM openjdk:17.0.2 AS builder
WORKDIR /app

COPY ./pom.xml ./pom.xml
COPY ./.mvn .mvn/
COPY ./mvnw ./mvnw

RUN ./mvnw dependency:go-offline

COPY ./src ./src
RUN ./mvnw clean package -DskipTests

FROM openjdk:17.0.2
WORKDIR /app

COPY --from=builder /app/target/*.jar ./app.jar
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
