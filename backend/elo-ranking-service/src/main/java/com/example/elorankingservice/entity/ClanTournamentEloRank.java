package com.example.elorankingservice.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ClanTournamentEloRank extends EloRank {
    @Column(name="clan_id", nullable = false)
    private Long clanId;

    public ClanTournamentEloRank() {}
    public ClanTournamentEloRank(
            Long clanId,
            RankThreshold rankThreshold,
            int rating,
            Long tournamentId
            ) {
        super(rankThreshold, rating, tournamentId);
        this.clanId = clanId;
    }
}
