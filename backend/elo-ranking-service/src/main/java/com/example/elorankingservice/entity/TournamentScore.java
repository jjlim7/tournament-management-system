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
public abstract class TournamentScore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long tournamentId;

    // Constructor
    public TournamentScore(Long tournamentId) {
        this.tournamentId = tournamentId;
    }
}
