# Step 1: Build matchmaking-service
FROM openjdk:17 AS matchmaking-builder
WORKDIR /app/matchmaking-service
COPY matchmaking . 
RUN ./mvnw clean package -Dmaven.test.skip=true

# Step 2: Build tournament-service
FROM openjdk:17 AS tournament-builder
WORKDIR /app/tournament-service
COPY tournamentservice .
RUN ./mvnw clean package -Dmaven.test.skip=true

# Step 3: Build elo-ranking-service
FROM openjdk:17 AS elo-ranking-builder
WORKDIR /app/elo-ranking-service
COPY elo-ranking-service .
RUN ./mvnw clean package -Dmaven.test.skip=true

FROM openjdk:17
WORKDIR /app

# Copy the built JAR for elo-ranking-service from the builder stage
COPY --from=matchmaking-builder /app/matchmaking-service/target/matchmaking-service-0.0.1-SNAPSHOT.jar /app/matchmaking-service.jar

EXPOSE 8080

CMD ["java", "-jar", "/app/matchmaking-service.jar"]
