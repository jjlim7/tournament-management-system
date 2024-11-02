package csd.backend.matchmaking.feigndto;

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
    private String rankThreshold;  // Adjust type based on actual implementation
    private double skillMeanEstimate;
    private double uncertainty;

    public PlayerEloRank(long playerId, RankThreshold rankThresholdGold, double v, double skillMeanEstimate, long uncertainty) {
        this.playerId = playerId;
        this.rankThreshold = rankThresholdGold.toString();
        this.skillMeanEstimate = skillMeanEstimate;
        this.uncertainty = uncertainty;
    }
}
