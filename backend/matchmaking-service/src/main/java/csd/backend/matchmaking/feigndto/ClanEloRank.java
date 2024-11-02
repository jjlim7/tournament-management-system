package csd.backend.matchmaking.feigndto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClanEloRank {
    private Long clanId;
    private Long tournamentId;
    private String rankThreshold;  // Adjust type based on actual implementation
    private double meanSkillEstimate;
    private double uncertainty;

    public ClanEloRank(long clanId, RankThreshold rankThreshold, double v, double meanSkillEstimate, long uncertainty) {
        this.clanId = clanId;
        this.rankThreshold = rankThreshold.rank.toString();
        this.meanSkillEstimate = meanSkillEstimate;
        this.uncertainty = uncertainty;
    }
}