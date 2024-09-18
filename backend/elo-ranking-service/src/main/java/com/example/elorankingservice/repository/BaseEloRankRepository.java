package com.example.elorankingservice.repository;

import com.example.elorankingservice.entity.EloRank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;


@NoRepositoryBean
public interface BaseEloRankRepository<T extends EloRank> extends JpaRepository<T, Long> {
    // Read
    List<T> findEloRanksByTournamentId(Long tournamentId);
    List<T> findByMeanSkillEstimateBetweenAndTournamentId(double minRating, double maxRating, Long tournamentId);
    Optional<T> findEloRankByIdAndTournamentId(Long id, Long tournamentId);
}
