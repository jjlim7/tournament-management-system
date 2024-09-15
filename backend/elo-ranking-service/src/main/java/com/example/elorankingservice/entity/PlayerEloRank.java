package com.example.elorankingservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class PlayerEloRank extends EloRank {

    @Column(name = "player_id", nullable = false)
    private Long playerId;

    public PlayerEloRank() {}

    public PlayerEloRank(
            Long playerId,
            RankThreshold rankThreshold,
            float skillMeanEstimate,
            float uncertainty,
            Long tournamentId
    ) {
        super(rankThreshold, skillMeanEstimate, uncertainty, tournamentId);
        this.playerId = playerId;
    }
}
