package com.example.elorankingservice.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class TournamentScore {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long tournamentId;

    public TournamentScore(Long tournamentId) {
        this.tournamentId = tournamentId;
    }

    public Long getTournamentId() {
        return tournamentId;
    }

    public Long getId() {
        return id;
    }
}
