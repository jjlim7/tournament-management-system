package com.example.elorankingservice.repository;

import com.example.elorankingservice.entity.RankThreshold.Rank;
import com.example.elorankingservice.entity.RankThreshold;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface RankThresholdRepository extends JpaRepository<RankThreshold, Long> {
    Optional<RankThreshold> findByRank(Rank rank);
    RankThreshold findByMinRatingLessThanEqualAndMaxRatingGreaterThanEqual(double rating);
    RankThreshold updateMinRatingAndMaxRatingByRank(Rank rank, double minRating, double maxRating);
}
