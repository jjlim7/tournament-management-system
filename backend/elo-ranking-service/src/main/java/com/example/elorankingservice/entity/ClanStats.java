package com.example.elorankingservice.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClanStats {
    private String clanId;
    private Long tournamentId;

    private double wins;
    private double losses;
}
