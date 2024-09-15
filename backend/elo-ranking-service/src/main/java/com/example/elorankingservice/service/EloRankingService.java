package com.example.elorankingservice.service;

import com.example.elorankingservice.entity.*;
import com.example.elorankingservice.repository.ClanEloRankRepository;
import com.example.elorankingservice.repository.PlayerEloRankRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class EloRankingService {

    private static final double K = 0.2;         // Base K value
    private static final double MAX_ALPHA = 1.5; // Cap for alpha scaling
    private static final double BASE_LAMBDA = 0.1; // Base uncertainty reduction factor
    private static final double DECAY_RATE = 0.3;  // Decay rate for placement outcome
    private static final double INITIAL_SIGMA = 8.333; // Default uncertainty for new players
    private static final double LAMBDA_ADJUSTMENT_FACTOR = 0.3; // Factor to adjust uncertainty by surprise performance

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
                    "healing", 0.3,  // Damage Mitigated
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

    // Repositories for player and clan elo rank data
    private final ClanEloRankRepository clanEloRankRepository;
    private final PlayerEloRankRepository playerEloRankRepository;

    @Autowired
    public EloRankingService(ClanEloRankRepository clanEloRankRepository, PlayerEloRankRepository playerEloRankRepository) {
        this.clanEloRankRepository = clanEloRankRepository;
        this.playerEloRankRepository = playerEloRankRepository;
    }

    public void updateClanEloRank(Long clanId, Long tournamentId, double deltaMean, double deltaUncertainty) {
        ClanEloRank eloRank = clanEloRankRepository.findClanEloRankByClanIdAndTournamentId(clanId, tournamentId);
        double finalMeanSkillEstimate = eloRank.getMeanSkillEstimate() + deltaMean;
        double finalUncertainty = eloRank.getUncertainty() + deltaUncertainty;
        eloRank.setMeanSkillEstimate(finalMeanSkillEstimate);
        eloRank.setUncertainty(finalUncertainty);
    }

    public void updatePlayerEloRank(Long playerId, Long tournamentId, double deltaMean, double deltaUncertainty) {
        PlayerEloRank eloRank = playerEloRankRepository.findByPlayerEloRankByPlayerIdAndTournamentId(playerId, tournamentId);
        double finalMeanSkillEstimate = eloRank.getMeanSkillEstimate() + deltaMean;
        double finalUncertainty = eloRank.getUncertainty() + deltaUncertainty;
        eloRank.setMeanSkillEstimate(finalMeanSkillEstimate);
        eloRank.setUncertainty(finalUncertainty);
    }

    public Map<Long, List<Double>> computeResultantPlayerEloRating(List<PlayerEloRank> playersEloRank, List<PlayerGameScore> playerGameScoreList) {

        // Sort both lists by player ID to align them
        playersEloRank.sort(Comparator.comparing(PlayerEloRank::getPlayerId));
        playerGameScoreList.sort(Comparator.comparing(PlayerGameScore::getPlayerId));

        HashMap<Long, List<Double>> finalPlayerEloRating = new HashMap<>();

        // Extract player IDs for convenience
        List<Long> playerIds = playersEloRank.stream().map(PlayerEloRank::getPlayerId).toList();

        // Step 1: Calculate RPS for all players and store it in a list
        List<Double> rpsList = new ArrayList<>();
        for (int i = 0; i < playersEloRank.size(); i++) {
            PlayerGameScore playerScore = playerGameScoreList.get(i);
            Role role = playerScore.getRole();

            // Retrieve role-based performance score from RPConfig
            double rps = playerScore.getRolePerformanceScore(RPConfig.getOrDefault(role, RPConfig.get(Role.DEFAULT)));
            rpsList.add(rps);
        }

        // Step 2: Calculate Z-score normalization
        double avgRPS = rpsList.stream().mapToDouble(Double::doubleValue).average().orElse(0);
        double stdRPS = Math.sqrt(rpsList.stream().mapToDouble(rps -> Math.pow(rps - avgRPS, 2)).average().orElse(1));

        // Step 3: Prepare for Expected Performance Calculation
        // Get the list of mean skill estimates
        List<Double> meanSkillEstimates = playersEloRank.stream().map(PlayerEloRank::getMeanSkillEstimate).toList();

        // Step 4: Compute Expected Performance (E_i) for each player
        List<Double> expectedPerformances = new ArrayList<>();
        for (int i = 0; i < playersEloRank.size(); i++) {
            PlayerEloRank playerRank = playersEloRank.get(i);
            double R_i = playerRank.getMeanSkillEstimate();

            // Compute R_opponents: average rating of all other players
            double sumRatings = meanSkillEstimates.stream().mapToDouble(Double::doubleValue).sum();
            double R_opponents = (sumRatings - R_i) / (playersEloRank.size() - 1);

            // Calculate Expected Performance E_i
            double E_i = 1 / (1 + Math.pow(10, (R_opponents - R_i) / 400));
            expectedPerformances.add(E_i);
        }

        // Step 5: Calculate Performance-Based Outcome (PBO) and update ratings
        for (int i = 0; i < playersEloRank.size(); i++) {

            PlayerEloRank playerRank = playersEloRank.get(i);
            PlayerGameScore playerScore = playerGameScoreList.get(i);
            double meanSkillEstimate = playerRank.getMeanSkillEstimate();
            double uncertainty = playerRank.getUncertainty();
            double rps_i = rpsList.get(i);
            double E_i = expectedPerformances.get(i);

            // Z-score normalization for RPS: Z_RPS_i = (RPS_i - μ_RPS) / σ_RPS
            double zNormalizedRPS = (rps_i - avgRPS) / stdRPS;

            // Calculate Alpha (scaling factor): α = K * (average rating / current player rating), capped by MAX_ALPHA
            double averageRating = meanSkillEstimates.stream().mapToDouble(Double::doubleValue).average().orElse(0);
            double ALPHA = Math.min(K * (averageRating / meanSkillEstimate), MAX_ALPHA);

            // Calculate the match outcome (between 0 and 1 based on placement) with decay
            double matchOutcome = Math.pow(1.0 - ((playerScore.getPlacement() - 1) / (double) (playerGameScoreList.size() - 1)), DECAY_RATE);

            // Calculate the Performance-Based Outcome (PBO): PBO = Match Outcome * (1 + α * Z_RPS_i)
            double performanceBasedOutcome = matchOutcome * (1 + ALPHA * zNormalizedRPS);

            // Step 6: Update Mean Skill Estimate using Expected Performance
            double newMeanSkillEstimate = meanSkillEstimate + K * (performanceBasedOutcome - E_i);

            // Step 7: Refine Uncertainty Adjustment
            // Compute variance v_i
            double v_i = 1 / (E_i * (1 - E_i));

            // Update uncertainty sigma_new
            double sigmaSquaredInverse = (1 / (uncertainty * uncertainty)) + (1 / v_i);
            double newUncertainty = getNewUncertainty(sigmaSquaredInverse, performanceBasedOutcome, meanSkillEstimate);

            // Store the result in the final map
            finalPlayerEloRating.put(
                    playerRank.getPlayerId(),
                    Arrays.asList(newMeanSkillEstimate, newUncertainty)
            );
        }

        return finalPlayerEloRating;
    }

    private static double getNewUncertainty(double sigmaSquaredInverse, double performanceBasedOutcome, double meanSkillEstimate) {
        double newUncertainty = Math.sqrt(1 / sigmaSquaredInverse);

        // Step 8: Apply Lambda for uncertainty decay based on surprise performance
        double surpriseFactor = Math.abs(performanceBasedOutcome - meanSkillEstimate) / meanSkillEstimate;
        double lambda = BASE_LAMBDA + surpriseFactor * LAMBDA_ADJUSTMENT_FACTOR;
        newUncertainty *= (1 - lambda);

        // Ensure uncertainty doesn't drop below a minimum threshold
        newUncertainty = Math.max(newUncertainty, INITIAL_SIGMA * 0.5);
        return newUncertainty;
    }
}
