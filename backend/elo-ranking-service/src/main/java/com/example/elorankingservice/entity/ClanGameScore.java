package com.example.elorankingservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(name = "clan_game_score", schema = "elo-ranking")
public class ClanGameScore extends GameScore {

    @Column(name = "clan_id", nullable = false)
    private Long clanId;

    // One clan game score can have multiple player game scores
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "clan_game_score_id")
    private List<PlayerGameScore> playerScores;

    @Column(name = "result", nullable = false)
    private int result;

    @Column(name = "score", nullable = false)
    private int score;

    // Many ClanGameScores are associated with one ClanTournamentEloRank
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clan_elo_rank_id", referencedColumnName = "id", nullable = false) // Correct the reference to the correct foreign key column
    private ClanEloRank clanEloRank;

    // No-Arg Constructor (required by JPA)
    public ClanGameScore() {
        super(null, null);
    }

    // Constructor
    public ClanGameScore(Long gameId,Long tournamentId,  Long clanId, int result, List<PlayerGameScore> playerScores) {
        super(gameId, tournamentId);
        this.clanId = clanId;
        this.result = result;
        this.playerScores = playerScores;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("ClanGameScore{");
        sb.append("gameId=").append(this.getGameId());
        sb.append(", tournamentId=").append(this.getTournamentId());
        sb.append(", clanId=").append(clanId);
        sb.append(", result=").append(result);
        sb.append(", score=").append(score);
        sb.append(", playerScores=[");

        if (playerScores != null && !playerScores.isEmpty()) {
            for (PlayerGameScore playerScore : playerScores) {
                sb.append(playerScore.toString()).append(", ");
            }
        } else {
            sb.append("No player scores");
        }

        sb.append("]");
        sb.append(", clanEloRankId=").append(clanEloRank != null ? clanEloRank.getId() : "null");
        sb.append('}');

        return sb.toString();
    }
}
