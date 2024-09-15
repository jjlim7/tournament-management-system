package com.example.elorankingservice.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class ClanEloRank extends EloRank {

    @Column(name = "clan_id", nullable = false)
    private Long clanId;

    // One Tournament Score can have many Clan Game Scores
    @OneToMany(mappedBy = "clanTournamentEloRank", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ClanGameScore> clanGameScores;

    public ClanEloRank() {}

    public ClanEloRank(
            Long clanId,
            RankThreshold rankThreshold,
            double skillMeanEstimate,
            double uncertainty,
            Long tournamentId
    ) {
        super(rankThreshold, skillMeanEstimate, uncertainty, tournamentId);
        this.clanId = clanId;
    }
}

