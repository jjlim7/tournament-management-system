package com.example.elorankingservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
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
}
