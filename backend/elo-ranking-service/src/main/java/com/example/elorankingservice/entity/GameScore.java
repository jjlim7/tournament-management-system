package com.example.elorankingservice.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public abstract class GameScore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long gameId;
    private Long tournamentId;

    // No-arg constructor
    public GameScore() {
    }

    // Constructor with parameters
    public GameScore(Long gameId, Long tournamentId) {
        this.gameId = gameId;
        this.tournamentId = tournamentId;
    }
}
