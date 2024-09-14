package com.example.elorankingservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class PlayerTournamentScore extends TournamentScore {
    private Long playerId;
    private Rank rank;
}
