package com.example.elorankingservice.repository;

import com.example.elorankingservice.entity.GameScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
public interface BaseGameScoreRepository<T extends  GameScore> extends JpaRepository<T, Long> {
    // Create
    List<T> saveAll(List<T> gameScores);

    // Read
    T getGameScoreById(Long id);
    List<T> getGameScoresByGameId(Long gameId);

    // Update
    T update(T gameScore);

    // Delete
    void deleteAll(List<T> gameScores);
}