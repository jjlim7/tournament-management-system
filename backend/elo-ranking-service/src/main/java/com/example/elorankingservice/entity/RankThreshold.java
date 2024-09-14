package com.example.elorankingservice.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class RankThreshold {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="rank", nullable=false)
    private Rank rank;

    @Column(name="start_rating", nullable=false)
    private int startRating;

    @Column(name="end_rating", nullable=false)
    private int endRating;

    public RankThreshold() {}

    public RankThreshold(Rank rank, int startRating, int endRating) {
        this.rank = rank;
        this.startRating = startRating;
        this.endRating = endRating;
    }
}
