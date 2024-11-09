package com.example.tournamentservice.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MatchmakingDTO {
    private Long id;
    private String status;
    private Long tournamentId;

}

