package csd.backend.matchmaking.dto;

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
    private double skillMeanEstimate;
    private double uncertainty;

    public ClanEloRank(long clanId, RankThreshold rankThreshold, double v, double skillMeanEstimate, long uncertainty) {
        this.clanId = clanId;
        this.rankThreshold = rankThreshold.rank.toString();
        this.skillMeanEstimate = skillMeanEstimate;
        this.uncertainty = uncertainty;
    }
}