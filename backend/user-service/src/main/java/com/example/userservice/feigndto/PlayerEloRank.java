package com.example.userservice.feigndto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlayerEloRank extends EloRank {
    private Long playerId;
    private Long tournamentId;
    private RankThreshold rankThreshold;  // Adjust type based on actual implementation
    private double skillMeanEstimate;
    private double uncertainty;

    // Constructor without Lombok, if needed
    public PlayerEloRank(Long playerId, RankThreshold rankThreshold, double skillMeanEstimate, double uncertainty) {
        this.playerId = playerId;
        this.rankThreshold = rankThreshold;
        this.skillMeanEstimate = skillMeanEstimate;
        this.uncertainty = uncertainty;
    }
}