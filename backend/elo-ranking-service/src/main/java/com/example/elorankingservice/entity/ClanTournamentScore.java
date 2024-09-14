package com.example.elorankingservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class ClanTournamentScore extends TournamentScore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long clanId;

    public ClanTournamentScore(Long tournamentId, Long clanId) {
        super(tournamentId);
        this.clanId = clanId;
    }

    public Long getClanId() {
        return clanId;
    }
}
