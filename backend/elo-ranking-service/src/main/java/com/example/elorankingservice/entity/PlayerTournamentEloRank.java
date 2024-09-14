package com.example.elorankingservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class PlayerTournamentEloRank extends EloRank {

    @Column(name = "player_id", nullable = false)
    private Long playerId;

    public PlayerTournamentEloRank() {}

    public PlayerTournamentEloRank(
            Long playerId,
            RankThreshold rankThreshold,
            int rating,
            Long tournamentId
    ) {
        super(rankThreshold, rating, tournamentId);
        this.playerId = playerId;
    }
}
