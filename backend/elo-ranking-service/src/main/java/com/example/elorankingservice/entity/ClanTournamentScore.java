package com.example.elorankingservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class ClanTournamentScore extends TournamentScore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long clanId;

    // One Tournament Score can have many Clan Game Scores
    @OneToMany(mappedBy = "tournamentScore", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ClanGameScore> clanGameScores;

    // No-Arg Constructor (required by JPA)
    protected ClanTournamentScore() {
        super(null); // Pass null for TournamentScore since it's abstract and doesn't hold logic in the default constructor
    }

    // Constructor
    public ClanTournamentScore(Long tournamentId, Long clanId) {
        super(tournamentId);
        this.clanId = clanId;
    }
}
