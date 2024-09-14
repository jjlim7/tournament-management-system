package com.example.elorankingservice.entity;

import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class TournamentScore {
    @Id
    private Long id;
    private Long tournamentId;
}
