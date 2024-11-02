package csd.backend.matchmaking.dto;

import lombok.*;

@Getter
@Setter
public class EloRank {

    private Long id;
    private Long rankThresholdId; // Assuming you want to keep a reference to RankThreshold
    private double meanSkillEstimate;
    private double uncertainty;
    private Long tournamentId;
}
