package com.example.elorankingservice.repository;

import com.example.elorankingservice.entity.EloRank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
public interface BaseEloRankRepository<T extends EloRank> extends JpaRepository<T, Long> {
    // Create
    T save(T eloRank);
    List<T> saveAll(List<T> eloRanks);

    // Read
    T getEloRankById(Long id);
    T getEloRankByTournamentId(Long tournamentId);
    List<T> getEloRankByRatingRange(int start, int end);
    List<T> getEloRanksByRankThreshold(Long rankThresholdId);

    // Update
    T update(T eloRank);
    void updateEloRank(double mean, double uncertainty);

    // Delete
    void deleteAll(List<T> eloRanks);
}
