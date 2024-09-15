package com.example.elorankingservice.service;

import com.example.elorankingservice.entity.*;
import com.example.elorankingservice.repository.ClanEloRankRepository;
import com.example.elorankingservice.repository.PlayerEloRankRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class EloRankingService {
    // Constants for the simplified TrueSkill algorithm
    private static final double INITIAL_MEAN = 25.0;
    private static final double INITIAL_SIGMA = 8.333;
    private static final double K = 0.1; // Learning rate
    private static final double LAMBDA = 0.05; // Uncertainty decay factor

    // Role-based performance configuration (RPConfig)
    // this will determine the weightages for each metrics
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

    // for player and clan tournament elo rank
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

        // compute the performance-based outcome (PBO) for each player
        // FORMULA: PBO_i = Match Outcome * (1 + α * Normalized RPS_i)
        // RPS weightage will be different depending on player's selected role
        // Normalized RPS_i = (RPS_i - μ_RPS) / (R_max - R_min)
        // for battle royal: α will be K constant * (average rating of all players / current rating of player)
        // for battle royal: match outcome is a range from 0 to 1 depending on the placement of the player
        // for clan war: α will be K constant * (average rating of both clans / current rating of clan)
        // for clan war: the match outcome is either 0 or 1 depending on either win/loss

        // get initial elo ranking of all players in game
        // check game mode
        // retrieve the metric weightages of game mode
        // calculate the rps of each player based on the metrics and their weightages
        // for clan war, create a hashmap of the => role: {metrics: weightage}
        // for battle royale, add another entry called => default: {metrics: weightages} with its own weightages
        // calculate the RPS for each of the players wrt to their roles
        // normalise the RPS for all the players
        // calculate the alpha depending on the gamemode
        // calculate the match outcome
        // using the match outcome, alpha, normalised RPS for each player
        // calculate the Δμ	and Δσ based on PBO
        // update the elo rank of all players
        // extra step for clan war,
        // accumulate the total Δμ and Δσ based on PBO
        // update the elo rank of the clan

        // Sort both lists by player ID to align them
        playersEloRank.sort(Comparator.comparing(PlayerEloRank::getPlayerId));
        playerGameScoreList.sort(Comparator.comparing(PlayerGameScore::getPlayerId));

        HashMap<Long, List<Double>> finalPlayerEloRating = new HashMap<>();

        // Array of current ratings (mean skill estimates)
        List<Double> initialRatings = playersEloRank.stream().map(PlayerEloRank::getMeanSkillEstimate).toList();

        // Calculate the average rating of the game
        double averageRating = initialRatings.stream().mapToDouble(Double::doubleValue).average().orElse(0);

        // Step 1: Calculate RPS for all players and store it in a list
        List<Double> rpsList = new ArrayList<>();
        for (int i = 0; i < playersEloRank.size(); i++) {
            PlayerGameScore playerScore = playerGameScoreList.get(i);
            Role role = playerScore.getRole();

            // Retrieve role-based performance score from RPConfig
            double rps = playerScore.getRolePerformanceScore(RPConfig.getOrDefault(role, RPConfig.get(Role.DEFAULT)));
            rpsList.add(rps);
        }

        // Step 2: Calculate the min, max, and average RPS to normalize the values
        double maxRPS = rpsList.stream().max(Double::compare).orElse(1.0);
        double minRPS = rpsList.stream().min(Double::compare).orElse(0.0);
        double avgRPS = rpsList.stream().mapToDouble(Double::doubleValue).average().orElse(0);

        // Edge case handling for normalization (avoid division by zero)
        double denominator = maxRPS - minRPS;
        if (denominator == 0) {
            denominator = 1; // Prevent division by zero
        }

        // Step 3: Normalize the RPS and calculate the performance-based outcome (PBO)
        for (int i = 0; i < playersEloRank.size(); i++) {
            PlayerEloRank playerRank = playersEloRank.get(i);
            PlayerGameScore playerScore = playerGameScoreList.get(i);

            double meanSkillEstimate = playerRank.getMeanSkillEstimate();
            double uncertainty = playerRank.getUncertainty();
            double rps_i = rpsList.get(i);

            // Normalize the RPS: Normalized RPS_i = (RPS_i - μ_RPS) / (R_max - R_min)
            double playerNormalizedRPS = (rps_i - avgRPS) / denominator;

            // Calculate Alpha (scaling factor): α = K * (average rating / current player rating)
            double ALPHA = playerRank.getMeanSkillEstimate() != 0 ? K * (averageRating / playerRank.getMeanSkillEstimate()) : 0;

            // Calculate the match outcome (between 0 and 1 based on placement)
            double matchOutcome = 1.0 - ((playerScore.getPlacement() - 1) / (playerGameScoreList.size() - 1) * 0.1);

            // Calculate the Performance Based Outcome (PBO): PBO = Match Outcome * (1 + α * Normalized RPS_i)
            double performanceBasedOutcome = matchOutcome * (1 + ALPHA * playerNormalizedRPS);

            // Calculate the new mean skill estimate and uncertainty
            double newMeanSkillEstimate = meanSkillEstimate + K * (performanceBasedOutcome - meanSkillEstimate);
            double newUncertainty = Math.max(uncertainty * (1 - LAMBDA), INITIAL_SIGMA * 0.1);

            // Store the result in the final map
            finalPlayerEloRating.put(
                    playerRank.getPlayerId(),
                    Arrays.asList(newMeanSkillEstimate, newUncertainty)
            );
        }

        return finalPlayerEloRating;
    }

    public Map<Long, List<Float>> computeClanEloRatingAdjustments(ClanEloRank clanEloRank, ClanGameScore clanGameScore) {}
}


