package com.example.elorankingservice.service;

import com.example.elorankingservice.entity.*;
import com.example.elorankingservice.repository.ClanEloRankRepository;
import com.example.elorankingservice.repository.PlayerEloRankRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class EloRankingService {

    private static final double K = 0.2;
    private static final double MAX_ALPHA = 1.5;
    private static final double BASE_LAMBDA = 0.1;
    private static final double DECAY_RATE = 0.3;
    private static final double INITIAL_MEAN = 25;
    private static final double INITIAL_SIGMA = 8.333;
    private static final double LAMBDA_ADJUSTMENT_FACTOR = 0.3;
    private static final RankThreshold.Rank ORIGIN_RANK = RankThreshold.Rank.BRONZE;

    private final PlayerEloRankRepository playerEloRankRepository;
    private final ClanEloRankRepository clanEloRankRepository;
    private final RankService rankService;

    // Role-based performance configuration (RPConfig)
    private static final Map<PlayerGameScore.Role, Map<String, Double>> RPConfig = Map.of(
            PlayerGameScore.Role.DAMAGE_DEALER, Map.of(
                    "kdr", 0.4,
                    "acc", 0.2,
                    "dps", 0.3,
                    "headshot_acc", 0.1
            ),
            PlayerGameScore.Role.TANK, Map.of(
                    "tanked", 0.5,
                    "healing", 0.3,
                    "assists", 0.2
            ),
            PlayerGameScore.Role.HEALER, Map.of(
                    "healing", 0.6,
                    "revives", 0.2,
                    "assists", 0.2
            ),
            PlayerGameScore.Role.DEFAULT, Map.of(
                    "kdr", 0.4,
                    "acc", 0.1,
                    "effective_dmg", 0.2,
                    "dps", 0.2,
                    "headshot_acc", 0.1
            )
    );

    @Autowired
    public EloRankingService(ClanEloRankRepository clanEloRankRepository, PlayerEloRankRepository playerEloRankRepository, RankService rankService) {
        this.clanEloRankRepository = clanEloRankRepository;
        this.playerEloRankRepository = playerEloRankRepository;
        this.rankService = rankService;
    }

    // Retrieve Elo Rank methods
    public Optional<ClanEloRank> retrieveClanEloRank(Long clanId, Long tournamentId) {
        return clanEloRankRepository.findByClanIdAndTournamentId(clanId, tournamentId);
    }

    public Optional<PlayerEloRank> retrievePlayerEloRank(Long playerId, Long tournamentId) {
        return playerEloRankRepository.findByPlayerIdAndTournamentId(playerId, tournamentId);
    }

    public List<ClanEloRank> retrieveClanEloRanksByTournament(Long tournamentId) {
        return clanEloRankRepository.findEloRanksByTournamentId(tournamentId);
    }

    public List<PlayerEloRank> retrievePlayerEloRanksByTournament(Long tournamentId) {
        return playerEloRankRepository.findEloRanksByTournamentId(tournamentId);
    }

    public List<PlayerEloRank> retrievePlayerEloRanksByRatingRange(Long tournamentId, double minRating, double maxRating) {
        return playerEloRankRepository.findByMeanSkillEstimateBetweenAndTournamentId(maxRating, minRating, tournamentId);
    }

    public List<ClanEloRank> retrieveClanEloRanksByRatingRange(Long tournamentId, double minRating, double maxRating) {
        return clanEloRankRepository.findByMeanSkillEstimateBetweenAndTournamentId(maxRating, minRating, tournamentId);
    }

    // Create new Player/Clan Elo ranking
    public PlayerEloRank createNewPlayerEloRanking(long playerId, long tournamentId) {
        playerEloRankRepository.findById(playerId).ifPresent(playerEloRank -> {
            throw new IllegalArgumentException("Player already has an Elo ranking");
        });

        RankThreshold defaultRank = rankService.retrieveRankThresholdByRank(ORIGIN_RANK);
        PlayerEloRank playerEloRank = new PlayerEloRank(playerId, defaultRank, INITIAL_MEAN, INITIAL_SIGMA, tournamentId);
        return playerEloRankRepository.save(playerEloRank);
    }

    public ClanEloRank createNewClanEloRanking(long clanId, long tournamentId) {
        clanEloRankRepository.findById(clanId).ifPresent(clanEloRank -> {
            throw new IllegalArgumentException("Clan already has an Elo ranking");
        });

        RankThreshold defaultRank = rankService.retrieveRankThresholdByRank(ORIGIN_RANK);
        ClanEloRank clanEloRank = new ClanEloRank(clanId, defaultRank, INITIAL_MEAN, INITIAL_SIGMA, tournamentId);
        return clanEloRankRepository.save(clanEloRank);
    }

    // Update Player/Clan Elo rankings
    public List<PlayerEloRank> updatePlayerEloRanking(Map<Long, List<Double>> finalPlayerEloRating) throws Exception {
        List<PlayerEloRank> updatedRanks = new ArrayList<>();
        for (Map.Entry<Long, List<Double>> entry : finalPlayerEloRating.entrySet()) {
            long playerId = entry.getKey();
            List<Double> newElo = entry.getValue();
            PlayerEloRank playerEloRank = playerEloRankRepository.findById(playerId)
                    .orElseThrow(() -> new IllegalArgumentException("Player not found with ID: " + playerId));

            updateEloRank(playerEloRank, newElo.get(0), newElo.get(1));
            updatedRanks.add(playerEloRank);
        }
        return updatedRanks;
    }

    public List<ClanEloRank> updateClanEloRanking(Map<Long, List<Double>> finalClanEloRating) throws Exception {
        List<ClanEloRank> updatedRanks = new ArrayList<>();
        for (Map.Entry<Long, List<Double>> entry : finalClanEloRating.entrySet()) {
            long clanId = entry.getKey();
            List<Double> newElo = entry.getValue();
            ClanEloRank clanEloRank = clanEloRankRepository.findById(clanId)
                    .orElseThrow(() -> new IllegalArgumentException("Clan not found with ID: " + clanId));

            updateEloRank(clanEloRank, newElo.get(0), newElo.get(1));
            updatedRanks.add(clanEloRank);
        }
        return updatedRanks;
    }

    private void updateEloRank(EloRank rank, double newSkillEstimate, double newUncertainty) throws Exception {
        RankThreshold newRank = rankService.retrieveRankThresholdByRating(newSkillEstimate);
        rank.setMeanSkillEstimate(newSkillEstimate);
        rank.setUncertainty(newUncertainty);
        rank.setRankThreshold(newRank);
    }

    // Process battle royale results
    public List<PlayerEloRank> processUpdateBattleRoyaleResults(List<PlayerGameScore> battleRoyaleResults) throws Exception {
        List<PlayerEloRank> playerRanks = new ArrayList<>();
        for (PlayerGameScore score : battleRoyaleResults) {
            PlayerEloRank playerRank = retrievePlayerEloRank(score.getPlayerId(), score.getTournamentId())
                    .orElseThrow(() -> new Exception("Elo rank not found for player " + score.getPlayerId()));
            playerRanks.add(playerRank);
        }

        Map<Long, List<Double>> finalResult = computeResultantPlayerEloRating(playerRanks, battleRoyaleResults);
        return updatePlayerEloRanking(finalResult);
    }

    // Compute resultant Elo rating for players
    private Map<Long, List<Double>> computeResultantPlayerEloRating(List<PlayerEloRank> playerRanks, List<PlayerGameScore> playerScores) {
        Map<Long, List<Double>> resultMap = new HashMap<>();
        sortPlayerData(playerRanks, playerScores);

        List<Double> rpsList = calculateRPSForAllPlayers(playerRanks, playerScores);
        double avgRPS = calculateAverage(rpsList);
        double stdRPS = calculateStandardDeviation(rpsList, avgRPS);
        List<Double> expectedPerformances = calculateExpectedPerformances(playerRanks);

        for (int i = 0; i < playerRanks.size(); i++) {
            PlayerEloRank playerRank = playerRanks.get(i);
            PlayerGameScore score = playerScores.get(i);
            double newSkillEstimate = calculateNewSkillEstimate(playerRank, score, rpsList.get(i), avgRPS, stdRPS, expectedPerformances.get(i));
            double newUncertainty = calculateNewUncertainty(playerRank.getUncertainty(), expectedPerformances.get(i), newSkillEstimate, playerRank.getMeanSkillEstimate());

            resultMap.put(playerRank.getPlayerId(), Arrays.asList(newSkillEstimate, newUncertainty));
        }
        return resultMap;
    }

    // Calculate new skill estimate
    private double calculateNewSkillEstimate(PlayerEloRank rank, PlayerGameScore score, double rps, double avgRPS, double stdRPS, double expectedPerformance) {
        double zNormalizedRPS = (rps - avgRPS) / stdRPS;
        double alpha = dynamicAlpha(avgRPS, stdRPS, rps, rank.getMeanSkillEstimate());
        double matchOutcome = calculateMatchOutcome(score, expectedPerformance);

        return rank.getMeanSkillEstimate() + K * (matchOutcome * (1 + alpha * zNormalizedRPS) - expectedPerformance);
    }

    // Helper methods...
    private double calculateMatchOutcome(PlayerGameScore score, double expectedPerformance) {
        return Math.pow(1.0 - ((score.getPlacement() - 1) / (double) expectedPerformance), DECAY_RATE);
    }

    private double dynamicAlpha(double avgRPS, double stdRPS, double rps, double meanSkillEstimate) {
        double performanceDeviation = stdRPS > 0 ? Math.abs(rps - avgRPS) / stdRPS : 0;
        return Math.min(K * (avgRPS / meanSkillEstimate), MAX_ALPHA) * (1 / (1 + performanceDeviation));
    }

    private void sortPlayerData(List<PlayerEloRank> playerRanks, List<PlayerGameScore> playerScores) {
        playerRanks.sort(Comparator.comparing(PlayerEloRank::getPlayerId));
        playerScores.sort(Comparator.comparing(PlayerGameScore::getPlayerId));
    }

    private List<Double> calculateRPSForAllPlayers(List<PlayerEloRank> playerRanks, List<PlayerGameScore> playerScores) {
        List<Double> rpsList = new ArrayList<>();
        for (int i = 0; i < playerRanks.size(); i++) {
            PlayerGameScore score = playerScores.get(i);
            double rps = score.getRolePerformanceScore(RPConfig.getOrDefault(score.getRole(), RPConfig.get(PlayerGameScore.Role.DEFAULT)));
            rpsList.add(rps);
        }
        return rpsList;
    }

    private double calculateAverage(List<Double> list) {
        return list.stream().mapToDouble(Double::doubleValue).average().orElse(0);
    }

    private double calculateStandardDeviation(List<Double> list, double mean) {
        return Math.sqrt(list.stream().mapToDouble(rps -> Math.pow(rps - mean, 2)).average().orElse(0));
    }

    private List<Double> calculateExpectedPerformances(List<PlayerEloRank> playerRanks) {
        double sumRatings = playerRanks.stream().mapToDouble(PlayerEloRank::getMeanSkillEstimate).sum();
        List<Double> expectedPerformances = new ArrayList<>();
        for (PlayerEloRank rank : playerRanks) {
            double R_opponents = (sumRatings - rank.getMeanSkillEstimate()) / (playerRanks.size() - 1);
            expectedPerformances.add(1 / (1 + Math.pow(10, (R_opponents - rank.getMeanSkillEstimate()) / 400)));
        }
        return expectedPerformances;
    }

    private double calculateNewUncertainty(double uncertainty, double expectedPerformance, double performanceOutcome, double meanSkillEstimate) {
        double v_i = 1 / (expectedPerformance * (1 - expectedPerformance));
        double sigmaSquaredInverse = (1 / (uncertainty * uncertainty)) + (1 / v_i);
        double newUncertainty = Math.sqrt(1 / sigmaSquaredInverse);

        double surpriseFactor = Math.abs(performanceOutcome - meanSkillEstimate) / meanSkillEstimate;
        double lambda = BASE_LAMBDA + surpriseFactor * LAMBDA_ADJUSTMENT_FACTOR;
        newUncertainty *= (1 - lambda);

        return Math.max(newUncertainty, INITIAL_SIGMA * 0.5); // Minimum uncertainty
    }
}
