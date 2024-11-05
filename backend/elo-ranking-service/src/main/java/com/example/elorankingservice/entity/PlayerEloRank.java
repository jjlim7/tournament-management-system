package com.example.elorankingservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "player_elo_rank", schema = "elo-ranking", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"player_id", "tournament_id"})
})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class PlayerEloRank extends EloRank {

    @Column(name = "player_id", nullable = false)
    private Long playerId;

    // One PlayerEloRank can have many PlayerGameScores
    @OneToMany(mappedBy = "playerEloRank", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<PlayerGameScore> playerGameScores;

    public PlayerEloRank() {}

    public PlayerEloRank(
            Long playerId,
            RankThreshold rankThreshold,
            double meanSkillEstimate,
            double uncertainty,
            Long tournamentId
    ) {
        super(rankThreshold, meanSkillEstimate, uncertainty, tournamentId);
        this.playerId = playerId;
    }
}
