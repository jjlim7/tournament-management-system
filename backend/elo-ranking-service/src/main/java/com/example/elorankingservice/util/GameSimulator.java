package com.example.elorankingservice.util;

import com.example.elorankingservice.entity.PlayerGameScore;
import lombok.Getter;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.*;

@Component
@Lazy
public class GameSimulator {
    private static final Random random = new Random();

    @Getter
    public static class ClanWarResult {
        Map<Long, List<PlayerGameScore>> winnerRawPlayerGameScores;
        Map<Long, List<PlayerGameScore>> loserRawPlayerGameScores;
        Long tournamentId;
        Long gameId;
    }

    /**
     * Generates a list of PlayerGameScore objects for a Battle Royale game.
     *
     * @param playerIds     List of player IDs participating in the game.
     * @param gameId        Unique identifier for the game.
     * @param tournamentId  Unique identifier for the tournament.
     * @return List of PlayerGameScore objects.
     */
    public List<PlayerGameScore> generateBattleRoyalePlayerGameScores(List<Long> playerIds, long gameId, long tournamentId) {
        List<PlayerGameScore> gameScores = new ArrayList<>();

        for (Long playerId : playerIds) {
            PlayerGameScore gameScore = createPlayerGameScore(
                    playerId,
                    gameId,
                    tournamentId,
                    PlayerGameScore.Role.DEFAULT,
                    PlayerGameScore.GameMode.BATTLE_ROYALE,
                    random.nextInt(11),                          // Kills (0-10)
                    random.nextInt(6),                           // Deaths (0-5)
                    random.nextInt(playerIds.size()) + 1,        // Placement (1 to number of players)
                    Duration.ofSeconds(random.nextInt(3600))     // Survival time (0-3599 seconds)
            );
            gameScores.add(gameScore);
        }

        return gameScores;
    }

    /**
     * Generates game scores for a clan war between two clans.
     *
     * @param gameId         Unique identifier for the game.
     * @param tournamentId   Unique identifier for the tournament.
     * @param clanPlayerIds  Map of clan IDs to lists of player IDs. Should contain exactly two entries.
     *                       The first entry is considered the winning clan, the second the losing clan.
     * @return ClanWarResult object containing the scores.
     * @throws IllegalArgumentException if the map does not contain exactly two clans.
     */
    public ClanWarResult generateClanWarGameScores(long gameId, long tournamentId, Map<Long, List<Long>> clanPlayerIds) {
        if (clanPlayerIds.size() != 2) {
            throw new IllegalArgumentException("Exactly two clans must be provided for a clan war.");
        }

        Iterator<Map.Entry<Long, List<Long>>> iterator = clanPlayerIds.entrySet().iterator();
        Map.Entry<Long, List<Long>> winnerEntry = iterator.next();
        Map.Entry<Long, List<Long>> loserEntry = iterator.next();

        Long wClanId = winnerEntry.getKey();
        List<Long> wPlayerIds = winnerEntry.getValue();

        Long lClanId = loserEntry.getKey();
        List<Long> lPlayerIds = loserEntry.getValue();

        ClanWarResult result = new ClanWarResult();
        result.winnerRawPlayerGameScores = new HashMap<>();
        result.loserRawPlayerGameScores = new HashMap<>();
        result.tournamentId = tournamentId;
        result.gameId = gameId;

        result.winnerRawPlayerGameScores.put(wClanId, generateClanScores(wPlayerIds, gameId, tournamentId, true));
        result.loserRawPlayerGameScores.put(lClanId, generateClanScores(lPlayerIds, gameId, tournamentId, false));

        return result;
    }

    /**
     * Generates scores for a single clan.
     *
     * @param playerIds     List of player IDs in the clan.
     * @param gameId        Unique identifier for the game.
     * @param tournamentId  Unique identifier for the tournament.
     * @param isWinner      Indicates if the clan is the winner.
     * @return List of PlayerGameScore objects for the clan.
     */
    private List<PlayerGameScore> generateClanScores(List<Long> playerIds, long gameId, long tournamentId, boolean isWinner) {
        List<PlayerGameScore> clanScores = new ArrayList<>();
        for (Long playerId : playerIds) {
            PlayerGameScore gameScore = createPlayerGameScore(
                    playerId,
                    gameId,
                    tournamentId,
                    roleRandomizer(),
                    PlayerGameScore.GameMode.CLAN_WAR,
                    random.nextInt(11),                       // Kills (0-10)
                    random.nextInt(6),                        // Deaths (0-5)
                    isWinner ? 1 : 2,                         // Placement: 1 for winner, 2 for loser
                    Duration.ofSeconds(random.nextInt(3600))  // Survival time (0-3599 seconds)
            );
            clanScores.add(gameScore);
        }
        return clanScores;
    }

    /**
     * Creates a PlayerGameScore object with randomized statistics.
     *
     * @param playerId     ID of the player.
     * @param gameId       Unique identifier for the game.
     * @param tournamentId Unique identifier for the tournament.
     * @param role         Role of the player.
     * @param gameMode     Game mode.
     * @param kills        Number of kills.
     * @param deaths       Number of deaths.
     * @param placement    Placement in the game.
     * @param survivalTime Survival time in the game.
     * @return A PlayerGameScore object.
     */
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
                random.nextDouble() * 3000,           // Damage Dealt (0.0 - 3000.0)
                random.nextInt(100),                  // Accuracy (0-99%)
                random.nextInt(100),                  // Headshot Accuracy (0-99%)
                random.nextInt(500),                  // Effective Damage (0-499)
                random.nextInt(6),                    // Revives (0-5)
                random.nextInt(2000),                 // Healing Done (0-1999)
                random.nextInt(10),                   // Assists (0-9)
                random.nextInt(2000),                 // Damage Tanked (0-1999)
                random.nextInt(500),                  // Damage Mitigated (0-499)
                random.nextInt(10),                   // Objectives Completed (0-9)
                random.nextInt(10)                    // Headshots (0-9)
        );
    }

    /**
     * Randomly selects a player role.
     *
     * @return A random PlayerGameScore.Role enum value.
     */
    private PlayerGameScore.Role roleRandomizer() {
        PlayerGameScore.Role[] roles = PlayerGameScore.Role.values();
        return roles[random.nextInt(roles.length)];
    }
}
