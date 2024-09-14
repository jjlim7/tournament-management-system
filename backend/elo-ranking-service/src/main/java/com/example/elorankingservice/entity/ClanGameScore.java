package com.example.elorankingservice.entity;

import jakarta.persistence.*;
import java.util.List;


@Entity
public class ClanGameScore extends GameScore {
    private Long clanId;

    // One clan game score can have multiple player game scores
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "clan_game_score_id")
    private List<PlayerGameScore> playerScores;

    @Column(name = "result", nullable = false)
    private boolean result;

    @Column(name = "score", nullable = false)
    private int score;

    public ClanGameScore(Long gameId, Long clanId, boolean result) {
        super(gameId);
        this.clanId = clanId;
        this.result = result;
    }

    public Long getClanId() {
        return clanId;
    }

    public boolean getResult() {
        return result;
    }

    public List<PlayerGameScore> getPlayerScores() {
        return playerScores;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
