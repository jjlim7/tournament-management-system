package com.example.tournamentservice.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClanEloRankDTO {
    private Long clanId;
    private Long tournamentId;
    private String rankThreshold;  // Adjust type based on actual implementation
    private double meanSkillEstimate;
    private double uncertainty;
}
