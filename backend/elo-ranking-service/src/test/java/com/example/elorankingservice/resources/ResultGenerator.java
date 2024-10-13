package com.example.elorankingservice.resources;

import com.example.elorankingservice.entity.PlayerGameScore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.*;

public class ResultGenerator {

    private static final Random random = new Random();

    public static class CreateClanWarGameScore {
        Map<Long, List<PlayerGameScore>> winnerRawPlayerGameScores;
        Map<Long, List<PlayerGameScore>> loserRawPlayerGameScores;
        Long tournamentId;
        Long gameId;

        // Manually add getters
        public Map<Long, List<PlayerGameScore>> getWinnerRawPlayerGameScores() {
            return winnerRawPlayerGameScores;
        }

        public Map<Long, List<PlayerGameScore>> getLoserRawPlayerGameScores() {
            return loserRawPlayerGameScores;
        }

        public Long getTournamentId() {
            return tournamentId;
        }

        public Long getGameId() {
            return gameId;
        }
    }

    public void generateBattleRoyalePlayerGameScores(int numberOfPlayers, long gameId, long tournamentId, String fileName) throws IOException {
        List<PlayerGameScore> gameScores = new ArrayList<>();

        for (int i = 1; i <= numberOfPlayers; i++) {
            PlayerGameScore gameScore = createPlayerGameScore(
                    (long) i,
                    gameId,
                    tournamentId,
                    PlayerGameScore.Role.DEFAULT,
                    PlayerGameScore.GameMode.BATTLE_ROYALE,
                    random.nextInt(11),
                    random.nextInt(6),
                    random.nextInt(numberOfPlayers) + 1,
                    Duration.ofSeconds(random.nextInt(3600))
            );
            gameScores.add(gameScore);
        }

        writeToFile(fileName, gameScores);
    }

    public CreateClanWarGameScore generateClanWarGameScores(long gameId, long tournamentId, long wClanId, long lClanId, String fileName) throws IOException {
        CreateClanWarGameScore result = new CreateClanWarGameScore();
        result.winnerRawPlayerGameScores = new HashMap<>();
        result.loserRawPlayerGameScores = new HashMap<>();
        result.tournamentId = tournamentId;
        result.gameId = gameId;

        result.winnerRawPlayerGameScores.put(wClanId, generateClanScores(wClanId, gameId, tournamentId, true));
        result.loserRawPlayerGameScores.put(lClanId, generateClanScores(lClanId, gameId, tournamentId, false));

        writeClanWarScoresToFile(fileName, result);

        return result;
    }

    private List<PlayerGameScore> generateClanScores(long clanId, long gameId, long tournamentId, boolean isWinner) {
        List<PlayerGameScore> clanScores = new ArrayList<>();
        for (int j = 1; j <= 5; j++) {
            PlayerGameScore gameScore = createPlayerGameScore(
                    (clanId * 100) + j,  // Unique player ID based on clan
                    gameId,
                    tournamentId,
                    roleRandomizer(),
                    PlayerGameScore.GameMode.CLAN_WAR,
                    random.nextInt(11),
                    random.nextInt(6),
                    isWinner ? 1 : 0,  // Winner gets 1, loser 2
                    Duration.ofSeconds(random.nextInt(3600))
            );
            clanScores.add(gameScore);
        }
        return clanScores;
    }

    private PlayerGameScore createPlayerGameScore(long playerId, long gameId, long tournamentId,
                                                  PlayerGameScore.Role role, PlayerGameScore.GameMode gameMode,
                                                  int kills, int deaths, int placement, Duration survivalTime) {
        return new PlayerGameScore(
                playerId,
                gameId,
                tournamentId,
                role,
                gameMode,
                kills,
                deaths,
                placement,
                survivalTime,
                random.nextDouble() * 3000,
                random.nextInt(100),
                random.nextInt(100),
                random.nextInt(500),
                random.nextInt(6),
                random.nextInt(2000),
                random.nextInt(10),
                random.nextInt(2000),
                random.nextInt(500),
                random.nextInt(10),
                random.nextInt(10)
        );
    }

    private PlayerGameScore.Role roleRandomizer() {
        PlayerGameScore.Role[] roles = PlayerGameScore.Role.values();
        return roles[random.nextInt(roles.length)];
    }

    private void writeToFile(String fileName, List<PlayerGameScore> scores) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        try {
            objectMapper.writeValue(new File(fileName), scores);
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
            throw e;
        }
    }

    private void writeClanWarScoresToFile(String fileName, CreateClanWarGameScore clanWarScores) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        try {
            objectMapper.writeValue(new File(fileName), clanWarScores);
        } catch (IOException e) {
            System.err.println("Error writing clan war scores to file: " + e.getMessage());
            throw e;
        }
    }
}