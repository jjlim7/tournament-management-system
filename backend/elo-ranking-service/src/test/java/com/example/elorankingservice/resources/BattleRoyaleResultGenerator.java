package com.example.elorankingservice.resources;

import com.example.elorankingservice.entity.PlayerGameScore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BattleRoyaleResultGenerator {

    private static final Random random = new Random();

    public void generatePlayerGameScores(int numberOfPlayers, String fileName) throws IOException {
        List<PlayerGameScore> gameScores = new ArrayList<>();

        for (int i = 1; i <= numberOfPlayers; i++) {
            // Use constructor instead of setters
            PlayerGameScore gameScore = new PlayerGameScore(
                    (long) i,                                // playerId
                    1001L,                                   // gameId (can be same as tournamentId)
                    1001L,                                  //  tournamentId
                    random.nextInt(11),                      // kills (random between 0 and 10)
                    random.nextInt(6),                       // deaths (random between 0 and 5)
                    random.nextFloat() * 50 + 1,             // placement (random between 1 and 50)
                    Duration.ofSeconds(random.nextInt(3600)),// survivalTime (random seconds)
                    random.nextDouble() * 3000,              // distanceTraveled (random distance)
                    random.nextInt(100),                     // shotsFired
                    random.nextInt(100),                     // shotsHit
                    random.nextInt(500),                     // healingDone
                    random.nextInt(6),                       // revives
                    random.nextInt(2000),                    // damageDone
                    random.nextInt(10),                      // headshots
                    random.nextInt(2000),                    // damageTaken
                    random.nextInt(500),                     // damageMitigated
                    random.nextInt(10),                      // assists
                    random.nextInt(10)                       // longestKillStreak
            );

            gameScores.add(gameScore);
        }

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());  // Register the JavaTimeModule to support Java 8 date/time types
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);  // Enable pretty printing
        objectMapper.writeValue(new File(fileName), gameScores);
    }
}
