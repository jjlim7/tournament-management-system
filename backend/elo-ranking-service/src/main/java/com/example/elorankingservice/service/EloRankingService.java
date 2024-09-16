package com.example.elorankingservice.service;

import com.example.elorankingservice.entity.*;
import com.example.elorankingservice.repository.ClanEloRankRepository;
import com.example.elorankingservice.repository.PlayerEloRankRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class EloRankingService {

    private static final double K = 0.2;         // Fixed K value (no dynamic K)
    private static final double MAX_ALPHA = 1.5; // Cap for alpha scaling
    private static final double BASE_LAMBDA = 0.1; // Base uncertainty reduction factor
    private static final double DECAY_RATE = 0.3;  // Decay rate for placement outcome
    private static final double INITIAL_SIGMA = 8.333; // Default uncertainty for new players
    private static final double LAMBDA_ADJUSTMENT_FACTOR = 0.3; // Factor to adjust uncertainty by surprise performance

    private final ClanEloRankRepository clanEloRankRepository;
    private final PlayerEloRankRepository playerEloRankRepository;

       // Role-based performance configuration (RPConfig)
    private static final Map<Role, Map<String, Double>> RPConfig = Map.of(
            Role.DAMAGE_DEALER, Map.of(
                    "kdr", 0.4,           // Kill/Death Ratio
                    "acc", 0.2,           // Accuracy
                    "dps", 0.3,           // Damage Per Second
                    "headshot_acc", 0.1   // Headshot Accuracy
            ),
            Role.TANK, Map.of(
                    "tanked", 0.5,            // Damage Tanked
                    "healing", 0.3,           // Damage Mitigated
                    "assists", 0.2            // Assists
            ),
            Role.HEALER, Map.of(
                    "healing", 0.6,           // Healing Done per Minute
                    "revives", 0.2,           // Revives
                    "assists", 0.2            // Assists
            ),
            Role.DEFAULT, Map.of(
                    "kdr", 0.4,
                    "acc", 0.1,
                    "effective_dmg", 0.2,
                    "dps", 0.2,
                    "headshot_acc", 0.1
            )
    );

    @Autowired
    public EloRankingService(ClanEloRankRepository clanEloRankRepository,
            PlayerEloRankRepository playerEloRankRepository) {
        this.clanEloRankRepository = clanEloRankRepository;
        this.playerEloRankRepository = playerEloRankRepository;
    }
    
    // Update Player Elo rankings
    public void updatePlayerEloRanking(Map<Long, List<Double>> finalPlayerEloRating) {
        for (Map.Entry<Long, List<Double>> entry : finalPlayerEloRating.entrySet()) {
            long playerId = entry.getKey();
            List<Double> newEloRank = entry.getValue();
            PlayerEloRank playerEloRank = playerEloRankRepository.findById(playerId)
                    .orElseThrow(() -> new IllegalArgumentException("Player not found with ID: " + playerId));
            playerEloRank.setMeanSkillEstimate(newEloRank.get(0));
            playerEloRank.setUncertainty(newEloRank.get(1));
            playerEloRankRepository.save(playerEloRank);
        }
    }

    // Update Clan Elo rankings
    public void updateClanEloRanking(Map<Long, Double> finalClanEloRating) {
        for (Map.Entry<Long, Double> entry : finalClanEloRating.entrySet()) {
            long clanId = entry.getKey();
            double newEloRank = entry.getValue();
            ClanEloRank clanEloRank = clanEloRankRepository.findById(clanId)
                    .orElseThrow(() -> new IllegalArgumentException("Clan not found with ID: " + clanId));
            clanEloRank.setMeanSkillEstimate(newEloRank);
            clanEloRankRepository.save(clanEloRank);
        }
    }

    // Compute resultant player Elo ratings
    public Map<Long, List<Double>> computeResultantPlayerEloRating(List<PlayerEloRank> playersEloRank, List<PlayerGameScore> playerGameScoreList) {
        Map<Long, List<Double>> finalPlayerEloRating = new HashMap<>();

        // Sort both lists by player ID to align them
        sortPlayerData(playersEloRank, playerGameScoreList);

        // Step 1: Calculate Role Performance Score (RPS) for all players
        List<Double> rpsList = calculateRPSForAllPlayers(playersEloRank, playerGameScoreList);

        // Step 2: Normalize the RPS using Z-score
        double avgRPS = calculateAverage(rpsList);
        double stdRPS = calculateStandardDeviation(rpsList, avgRPS);

        // Step 3: Calculate Expected Performance (E_i) for each player
        List<Double> expectedPerformances = calculateExpectedPerformances(playersEloRank);

        // Step 4: Update ratings based on Performance-Based Outcome (PBO)
        updateRatingsBasedOnPBO(playersEloRank, playerGameScoreList, finalPlayerEloRating, rpsList, avgRPS, stdRPS, expectedPerformances);

        return finalPlayerEloRating;
    }

    // Sort player Elo ranks and game scores by player ID
    private void sortPlayerData(List<PlayerEloRank> playersEloRank, List<PlayerGameScore> playerGameScoreList) {
        playersEloRank.sort(Comparator.comparing(PlayerEloRank::getPlayerId));
        playerGameScoreList.sort(Comparator.comparing(PlayerGameScore::getPlayerId));
    }

    // Calculate Role Performance Scores (RPS) for all players
    private List<Double> calculateRPSForAllPlayers(List<PlayerEloRank> playersEloRank, List<PlayerGameScore> playerGameScoreList) {
        List<Double> rpsList = new ArrayList<>();
        for (int i = 0; i < playersEloRank.size(); i++) {
            PlayerGameScore playerScore = playerGameScoreList.get(i);
            Role role = playerScore.getRole();
            double rps = playerScore.getRolePerformanceScore(RPConfig.getOrDefault(role, RPConfig.get(Role.DEFAULT)));
            rpsList.add(rps);
        }
        return rpsList;
    }

    // Calculate the average value of a list of doubles
    private double calculateAverage(List<Double> list) {
        return list.stream().mapToDouble(Double::doubleValue).average().orElse(0);
    }

    // Calculate the standard deviation of a list of doubles
    private double calculateStandardDeviation(List<Double> list, double mean) {
        return Math.sqrt(list.stream().mapToDouble(rps -> Math.pow(rps - mean, 2)).average().orElse(1));
    }

    // Calculate Expected Performance (E_i) for each player
    private List<Double> calculateExpectedPerformances(List<PlayerEloRank> playersEloRank) {
        List<Double> expectedPerformances = new ArrayList<>();
        List<Double> meanSkillEstimates = playersEloRank.stream().map(PlayerEloRank::getMeanSkillEstimate).toList();
        double sumRatings = meanSkillEstimates.stream().mapToDouble(Double::doubleValue).sum();

        for (PlayerEloRank playerRank : playersEloRank) {
            double R_i = playerRank.getMeanSkillEstimate();
            double R_opponents = (sumRatings - R_i) / (playersEloRank.size() - 1);
            double E_i = 1 / (1 + Math.pow(10, (R_opponents - R_i) / 400));
            expectedPerformances.add(E_i);
        }

        return expectedPerformances;
    }

    // Update player ratings based on Performance-Based Outcome (PBO)
    private void updateRatingsBasedOnPBO(
            List<PlayerEloRank> playersEloRank,
            List<PlayerGameScore> playerGameScoreList,
            Map<Long, List<Double>> finalPlayerEloRating,
            List<Double> rpsList, double avgRPS, double stdRPS,
            List<Double> expectedPerformances) {

        for (int i = 0; i < playersEloRank.size(); i++) {
            PlayerEloRank playerRank = playersEloRank.get(i);
            PlayerGameScore playerScore = playerGameScoreList.get(i);
            double meanSkillEstimate = playerRank.getMeanSkillEstimate();
            double uncertainty = playerRank.getUncertainty();
            double rps_i = rpsList.get(i);
            double E_i = expectedPerformances.get(i);

            double zNormalizedRPS = (rps_i - avgRPS) / stdRPS;
            double ALPHA = dynamicAlpha(avgRPS, stdRPS, rps_i, meanSkillEstimate);
            double matchOutcome = calculateMatchOutcome(playerScore, playerGameScoreList.size());

            double performanceBasedOutcome = matchOutcome * (1 + ALPHA * zNormalizedRPS);
            double newMeanSkillEstimate = meanSkillEstimate + K * (performanceBasedOutcome - E_i);
            double newUncertainty = calculateNewUncertainty(uncertainty, E_i, performanceBasedOutcome, meanSkillEstimate);

            finalPlayerEloRating.put(playerRank.getPlayerId(), Arrays.asList(newMeanSkillEstimate, newUncertainty));
        }
    }

    // Calculate the match outcome based on player placement
    private double calculateMatchOutcome(PlayerGameScore playerScore, int totalPlayers) {
        return Math.pow(1.0 - ((playerScore.getPlacement() - 1) / (double) (totalPlayers - 1)), DECAY_RATE);
    }

    // Dynamic alpha to control RPS influence based on performance variation
    private double dynamicAlpha(double avgRPS, double stdRPS, double rps, double meanSkillEstimate) {
        double dynamicCap = Math.min(K * (avgRPS / meanSkillEstimate), MAX_ALPHA);

        if (stdRPS > 0) {
            double performanceDeviation = Math.abs(rps - avgRPS) / stdRPS;
            return Math.min(dynamicCap, 1 / (1 + performanceDeviation));
        }

        return dynamicCap;
    }

    // Calculate the new uncertainty value for a player
    private double calculateNewUncertainty(double uncertainty, double E_i, double performanceBasedOutcome, double meanSkillEstimate) {
        double v_i = 1 / (E_i * (1 - E_i));
        double sigmaSquaredInverse = (1 / (uncertainty * uncertainty)) + (1 / v_i);
        double newUncertainty = Math.sqrt(1 / sigmaSquaredInverse);

        double surpriseFactor = Math.abs(performanceBasedOutcome - meanSkillEstimate) / meanSkillEstimate;
        double lambda = BASE_LAMBDA + surpriseFactor * LAMBDA_ADJUSTMENT_FACTOR;
        newUncertainty *= (1 - lambda);

        return Math.max(newUncertainty, INITIAL_SIGMA * 0.5);
    }

    // Clan Elo computation remains unchanged, using fixed K for updates
    public Map<Long, Double> computeResultantClanEloRating(
            ClanGameScore winnerGameScore,
            ClanGameScore loserGameScore,
            ClanEloRank winnerEloRank,
            ClanEloRank loserEloRank,
            List<PlayerGameScore> winnerPlayerGameScore,
            List<PlayerGameScore> loserPlayerGameScore,
            List<PlayerEloRank> winnerPlayerEloRank,
            List<PlayerEloRank> loserPlayerEloRank
    ) {

        Map<Long, Double> finalClanEloRating = new HashMap<>();

        // Calculate the average RPS for each clan
        double avgRPSWinner = calculateAverageRPS(winnerPlayerGameScore);
        double avgRPSLoser = calculateAverageRPS(loserPlayerGameScore);

        // Get the expected performance for each clan
        double expectedPerformanceWinner = calculateExpectedPerformance(winnerPlayerEloRank);
        double expectedPerformanceLoser = calculateExpectedPerformance(loserPlayerEloRank);

        // Update Elo ratings based on PBO
        updateClanRatingsBasedOnPBO(
                winnerEloRank, loserEloRank,
                avgRPSWinner, avgRPSLoser,
                expectedPerformanceWinner, expectedPerformanceLoser,
                finalClanEloRating
        );

        return finalClanEloRating;
    }

    // Calculate the average RPS for a clan
    private double calculateAverageRPS(List<PlayerGameScore> playerGameScoreList) {
        return playerGameScoreList.stream()
                .mapToDouble(player -> player.getRolePerformanceScore(RPConfig.getOrDefault(player.getRole(), RPConfig.get(Role.DEFAULT))))
                .average()
                .orElse(0);
    }

    // Calculate the expected performance for a clan
    private double calculateExpectedPerformance(List<PlayerEloRank> playerEloRankList) {
        double totalRatings = playerEloRankList.stream().mapToDouble(PlayerEloRank::getMeanSkillEstimate).sum();
        double averageRating = totalRatings / playerEloRankList.size();
        return 1 / (1 + Math.pow(10, (averageRating - totalRatings / playerEloRankList.size()) / 400));
    }

    // Update clan ratings based on Performance-Based Outcome (PBO)
    private void updateClanRatingsBasedOnPBO(
            ClanEloRank winnerEloRank, ClanEloRank loserEloRank,
            double avgRPSWinner, double avgRPSLoser,
            double expectedPerformanceWinner, double expectedPerformanceLoser,
            Map<Long, Double> finalClanEloRating) {

        // Winner's new Elo rating
        double winnerPerformanceOutcome = 1 + MAX_ALPHA * avgRPSWinner;
        double winnerNewElo = winnerEloRank.getMeanSkillEstimate() +
                K * (winnerPerformanceOutcome - expectedPerformanceWinner);

        // Loser's new Elo rating
        double loserPerformanceOutcome = 0 + MAX_ALPHA * avgRPSLoser;
        double loserNewElo = loserEloRank.getMeanSkillEstimate() +
                K * (loserPerformanceOutcome - expectedPerformanceLoser);

        finalClanEloRating.put(winnerEloRank.getClanId(), winnerNewElo);
        finalClanEloRating.put(loserEloRank.getClanId(), loserNewElo);
    }
}
