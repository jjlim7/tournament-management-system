package com.example.elorankingservice.entity;

import jakarta.persistence.*;
import java.util.List;


@Entity
public class ClanGameScore extends GameScore {
    private Long clanId;

    // One ClanGameScore has many PlayerGameScores
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "")  // Creates the foreign key in PlayerGameScore
    private List<PlayerGameScore> playerScores;

    public ClanGameScore(Long gameId, Long clanId) {
        super(gameId);
        this.clanId = clanId;
    }

    public Long getClanId() {
        return clanId;
    }

}
