package com.example.tournamentservice.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlayerEloRankDTO {
    private Long playerId;
    private Long tournamentId;
    private String rankThreshold;  // Adjust type based on actual implementation
    private double skillMeanEstimate;
    private double uncertainty;
}
