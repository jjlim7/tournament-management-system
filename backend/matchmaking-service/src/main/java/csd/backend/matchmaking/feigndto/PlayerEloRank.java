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
    private double meanSkillEstimate;
    private double uncertainty;

    public PlayerEloRank(long playerId, RankThreshold rankThresholdGold, double v, double meanSkillEstimate, long uncertainty) {
        this.playerId = playerId;
        this.rankThreshold = rankThresholdGold.toString();
        this.meanSkillEstimate = meanSkillEstimate;
        this.uncertainty = uncertainty;
    }
}
