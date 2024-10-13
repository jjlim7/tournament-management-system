package com.example.elorankingservice.repository;

import com.example.elorankingservice.entity.EloRank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import java.util.List;


@NoRepositoryBean
public interface BaseEloRankRepository<T extends EloRank> extends JpaRepository<T, Long> {
    // Read
    List<T> findEloRanksByTournamentId(Long tournamentId);
    List<T> findByMeanSkillEstimateBetweenAndTournamentId(double minRating, double maxRating, Long tournamentId);
}
