package com.example.elorankingservice.repository;

import com.example.elorankingservice.entity.RankThreshold.Rank;
import com.example.elorankingservice.entity.RankThreshold;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RankThresholdRepository extends JpaRepository<RankThreshold, Long> {
    Optional<RankThreshold> findByRank(Rank rank);

    @Modifying
    @Transactional
    @Query("UPDATE RankThreshold rt SET rt.minRating = :minRating, rt.maxRating = :maxRating WHERE rt.rank = :rank")
    RankThreshold updateMinRatingAndMaxRatingByRank(@Param("rank") Rank rank, @Param("minRating") double minRating, @Param("maxRating") double maxRating);
}