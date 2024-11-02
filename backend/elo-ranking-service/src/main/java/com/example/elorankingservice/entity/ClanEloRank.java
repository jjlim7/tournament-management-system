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
@Table(name = "clan_elo_rank", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"clan_id", "tournament_id"})
})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ClanEloRank extends EloRank {

    @Column(name = "clan_id", nullable = false)
    private Long clanId;

    // One Tournament Score can have many Clan Game Scores
    @OneToMany(mappedBy = "clanEloRank", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    // This prevents serialization and deserialization
    private List<ClanGameScore> clanGameScores;

    public ClanEloRank() {}

    public ClanEloRank(
            Long clanId,
            RankThreshold rankThreshold,
            double meanSkillEstimate,
            double uncertainty,
            Long tournamentId
    ) {
        super(rankThreshold, meanSkillEstimate, uncertainty, tournamentId);
        this.clanId = clanId;
    }
}

