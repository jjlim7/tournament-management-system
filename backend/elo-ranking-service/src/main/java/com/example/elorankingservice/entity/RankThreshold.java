package com.example.elorankingservice.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class RankThreshold {

    public enum Rank {
        IRON,
        BRONZE,
        SILVER,
        GOLD,
        PLATINUM,
        DIAMOND,
        MASTER,
        GRANDMASTER,
        CHALLENGER,
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="rank", nullable=false, unique = true)
    @Enumerated(EnumType.STRING)
    private Rank rank;

    @Column(name="min_rating", nullable=false)
    private double minRating;

    @Column(name="max_rating", nullable=false)
    private double maxRating;

    public RankThreshold() {}

    public RankThreshold(Rank rank, double minRating, double maxRating) {
        this.rank = rank;
        this.minRating = minRating;
        this.maxRating = maxRating;
    }
}
