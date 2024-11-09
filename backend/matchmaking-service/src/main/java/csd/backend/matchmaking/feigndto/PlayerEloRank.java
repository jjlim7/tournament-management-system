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
    Long id;
    private Long playerId;
    private Long tournamentId;
    private RankThreshold rankThreshold;  // Adjust type based on actual implementation
    private double meanSkillEstimate;
    private double uncertainty;

    public PlayerEloRank(long playerId, RankThreshold rankThreshold, double v, double meanSkillEstimate, long uncertainty) {
        this.playerId = playerId;
        this.rankThreshold = rankThreshold;
        this.meanSkillEstimate = meanSkillEstimate;
        this.uncertainty = uncertainty;
    }
}
