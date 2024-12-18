package com.example.elorankingservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@MappedSuperclass
@Getter
@Setter
public abstract class EloRank {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Many EloRanks can be associated with one Rank
    @ManyToOne
    @JoinColumn(name = "rank_threshold_id", nullable = false) // Defines rankId as FK
    private RankThreshold rankThreshold;

    @Column(name="mean_skill_estimate", nullable = false)
    private double meanSkillEstimate;

    @Column(name="uncertainty", nullable = false)
    private double uncertainty;

    @Column(name="tournament_id", nullable = false)
    private Long tournamentId;

    // No-arg constructor required by JPA
    protected EloRank() {}

    // Parameterized constructor for easy creation
    public EloRank(RankThreshold rankThreshold, double meanSkillEstimate, double uncertainty, Long tournamentId) {
        this.rankThreshold = rankThreshold;
        this.meanSkillEstimate = meanSkillEstimate;
        this.uncertainty = uncertainty;
        this.tournamentId = tournamentId;
    }
}

