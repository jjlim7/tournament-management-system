package com.example.elorankingservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Entity
@Getter
@Setter
public class PlayerEloRank extends EloRank {

    @Column(name = "player_id", nullable = false)
    private Long playerId;

    // One PlayerEloRank can have many PlayerGameScores
    @OneToMany(mappedBy = "playerEloRank", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PlayerGameScore> playerGameScores;

    public PlayerEloRank() {}

    public PlayerEloRank(
            Long playerId,
            RankThreshold rankThreshold,
            double skillMeanEstimate,
            double uncertainty,
            Long tournamentId
    ) {
        super(rankThreshold, skillMeanEstimate, uncertainty, tournamentId);
        this.playerId = playerId;
    }
}
