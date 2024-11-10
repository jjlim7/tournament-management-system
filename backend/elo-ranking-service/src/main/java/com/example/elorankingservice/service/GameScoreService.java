package com.example.elorankingservice.service;

import com.example.elorankingservice.entity.*;
import com.example.elorankingservice.repository.ClanGameScoreRepository;
import com.example.elorankingservice.repository.PlayerGameScoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;

@Service
public class GameScoreService {
    // for player and clan game score
    private final ClanGameScoreRepository clanGameScoreRepository;
    private final PlayerGameScoreRepository playerGameScoreRepository;
    private final EloRankingService eloRankingService;

    @Autowired
    public GameScoreService(ClanGameScoreRepository clanGameScoreRepository, PlayerGameScoreRepository playerGameScoreRepository, EloRankingService eloRankingService) {
        this.clanGameScoreRepository = clanGameScoreRepository;
        this.playerGameScoreRepository = playerGameScoreRepository;
        this.eloRankingService = eloRankingService;
    }

    public void storeAllPlayerGameScore(List<PlayerGameScore> playersGameScore) {
        playerGameScoreRepository.saveAll(playersGameScore);
    }


    public void storeClanGameScore(Long tournamentId, Long gameId, Long clanId, List<PlayerGameScore> playersGameScore, ClanEloRank currentRank, boolean isWinner)  {
        ClanGameScore clanGameScore = new ClanGameScore(gameId, tournamentId, clanId, 0, playersGameScore);;
        clanGameScore.setClanEloRank(currentRank);
        if (isWinner) {
            clanGameScore.setResult(1);
        }
        System.out.println(clanGameScore);
        clanGameScoreRepository.save(clanGameScore);
    }

    // Get all the clan game scores for the tournament
    public List<ClanGameScore> retrieveClanGameScoresForTournament(Long tournamentId, Long clanId) {
        return clanGameScoreRepository.findByClanEloRank_TournamentIdAndClanId(tournamentId, clanId);
    }

    public List<PlayerGameScore> retrievePlayerGameScoresForTournament(Long tournamentId, Long playerId) {
        return playerGameScoreRepository.findByPlayerEloRank_TournamentIdAndPlayerId(tournamentId, playerId);
    }

    public PlayerStats retrievePlayerStatisticsForTournament(Long tournamentId, Long playerId, PlayerGameScore.GameMode gameMode) {
        List<PlayerGameScore> allPlayerStats = retrievePlayerGameScoresForTournament(tournamentId, playerId);
        System.out.println("Retrieved PlayerGameScores: " + allPlayerStats);

        // Filter stats by game mode if specified
        List<PlayerGameScore> filteredStats = (gameMode == null) ? allPlayerStats :
                allPlayerStats.stream().filter(score -> score.getGameMode() == gameMode).toList();
        System.out.println("Filtered Stats for game mode " + gameMode + ": " + filteredStats);

        long totalMatches = filteredStats.size();

        // Calculate aggregate statistics
        int totalKills = sum(filteredStats, PlayerGameScore::getKills);
        int totalDeaths = sum(filteredStats, PlayerGameScore::getDeaths);
        int totalAssists = sum(filteredStats, PlayerGameScore::getAssists);
        int totalRevives = sum(filteredStats, PlayerGameScore::getRevives);
        int totalShotsFired = sum(filteredStats, PlayerGameScore::getShotsFired);
        int totalShotsHit = sum(filteredStats, PlayerGameScore::getShotsHit);

        // Calculate averages
        double avgKillDeathRatio = average(filteredStats, PlayerGameScore::getKillDeathRatio);
        double avgAccuracy = average(filteredStats, PlayerGameScore::getAccuracy);
        double avgHeadshotAccuracy = average(filteredStats, PlayerGameScore::getHeadshotAccuracy);
        double avgHealingDonePerSecond = average(filteredStats, PlayerGameScore::getHealingDonePerSecond);
        double avgDamageDonePerSecond = average(filteredStats, PlayerGameScore::getDamageDonePerSecond);
        double avgDamageTanked = average(filteredStats, PlayerGameScore::getDamageTanked);

        // Prepare PlayerStats
        PlayerStats statistics = new PlayerStats();
        statistics.setPlayerId(playerId);
        statistics.setTournamentId(tournamentId);
        statistics.setTotalMatches(totalMatches);
        statistics.setTotalKills(totalKills);
        statistics.setTotalDeaths(totalDeaths);
        statistics.setAvgKillDeathRatio(avgKillDeathRatio);
        statistics.setAvgAccuracy(avgAccuracy);
        statistics.setAvgHeadshotAccuracy(avgHeadshotAccuracy);
        statistics.setAvgHealingDonePerSecond(avgHealingDonePerSecond);
        statistics.setAvgDamageDonePerSecond(avgDamageDonePerSecond);
        statistics.setAvgDamageTanked(avgDamageTanked);

        statistics.setTotalAssists(totalAssists);
        statistics.setTotalRevives(totalRevives);
        statistics.setTotalShotsFired(totalShotsFired);
        statistics.setTotalShotsHit(totalShotsHit);
        statistics.setGameMode(gameMode);

        System.out.println("Calculated PlayerStats: " + statistics);

        return statistics;
    }

    // Method to retrieve clan statistics for a tournament
    public ClanStats retrieveClanStatisticsForTournament(Long tournamentId, Long clanId) {
        // Retrieve all game scores for the specified clan in the specified tournament
        List<ClanGameScore> allClanGameScores = retrieveClanGameScoresForTournament(tournamentId, clanId);
        System.out.println("Retrieved ClanGameScores: " + allClanGameScores);

        // Initialize ClanStats object to hold the results
        ClanStats clanStats = new ClanStats();
        clanStats.setClanId(clanId.toString());
        clanStats.setTournamentId(tournamentId);

        // Variables to count wins and losses
        double wins = 0;
        double losses = 0;

        // Loop through each game score to calculate wins and losses
        for (ClanGameScore score : allClanGameScores) {
            if (score.getResult() == 1) {
                wins++;
            } else {
                losses++;
            }
        }

        // Set the calculated wins and losses in the clanStats object
        clanStats.setWins(wins);
        clanStats.setLosses(losses);

        return clanStats;
    }

    // Helper methods for summing and averaging
    private int sum(List<PlayerGameScore> stats, ToIntFunction<PlayerGameScore> mapper) {
        return stats.stream().mapToInt(mapper).sum();
    }

    private double average(List<PlayerGameScore> stats, ToDoubleFunction<PlayerGameScore> mapper) {
        return stats.stream().mapToDouble(mapper).average().orElse(0);
    }

}

