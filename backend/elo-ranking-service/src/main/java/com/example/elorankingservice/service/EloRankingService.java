package com.example.elorankingservice.service;

import com.example.elorankingservice.entity.ClanEloRank;
import com.example.elorankingservice.entity.ClanGameScore;
import com.example.elorankingservice.entity.PlayerEloRank;
import com.example.elorankingservice.entity.PlayerGameScore;
import com.example.elorankingservice.repository.ClanEloRankRepository;
import com.example.elorankingservice.repository.PlayerEloRankRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EloRankingService {
    private static final double K = 0.5;

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

    public Map<Long, List<Float>> computePlayerEloRatingAdjustments(List<PlayerEloRank> playersEloRank, List<PlayerGameScore> playerGameScoreList, boolean isClanWar) {
        HashMap<Long, List<Float>> finalPlayerEloRatingAdjustments = new HashMap<>();

        // array of ratings
        List<Double> initialRatings = playersEloRank.stream().map(PlayerEloRank::getMeanSkillEstimate).toList();

        // average rating of game
        double averageRating = (double) initialRatings.stream().mapToDouble(Double::doubleValue).average().orElse(0);

        for (PlayerGameScore playerGameScore : playerGameScoreList) {

        }




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

        return finalPlayerEloRatingAdjustments;
    }

    public Map<Long, List<Float>> computeClanEloRatingAdjustments(ClanEloRank clanEloRank, ClanGameScore clanGameScore) {}
}


