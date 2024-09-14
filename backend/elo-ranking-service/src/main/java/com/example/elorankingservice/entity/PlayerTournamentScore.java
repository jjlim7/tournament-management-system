package com.example.elorankingservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
public class PlayerTournamentScore extends TournamentScore {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long playerId;
    private Rank rank;

    public PlayerTournamentScore(Long tournamentId, Long playerId, Rank rank) {
        super(tournamentId);
        this.playerId = playerId;
        this.rank = rank;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public Rank getRank() {
        return rank;
    }
}
