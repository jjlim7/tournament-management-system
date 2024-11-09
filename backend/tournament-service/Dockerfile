FROM openjdk:17
WORKDIR /app

COPY . .

RUN ./mvnw package -Dmaven.test.skip=false

EXPOSE 8080
CMD ["java", "-jar", "./target/tournament-service-0.0.1-SNAPSHOT.jar"]
