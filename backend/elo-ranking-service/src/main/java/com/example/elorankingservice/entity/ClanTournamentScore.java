package com.example.elorankingservice.entity;

import jakarta.persistence.Entity;

@Entity
public class ClanTournamentScore extends TournamentScore {
    private Long clanId;
}
