FROM openjdk:17
WORKDIR /app

COPY . .

RUN ./mvnw package -Dmaven.test.skip=true

EXPOSE 8080
CMD ["java", "-jar", "./target/user-service-0.0.1-SNAPSHOT.jar"]
