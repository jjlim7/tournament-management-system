package com.example.elorankingservice.repository;

import com.example.elorankingservice.entity.PlayerGameScore;

import java.util.List;

public interface PlayerGameScoreRepository extends BaseGameScoreRepository<PlayerGameScore> {
    // No additional methods, inherits all methods from BaseGameScoreRepository
    List<PlayerGameScore> findByPlayerEloRank_TournamentIdAndPlayerId(Long tournamentId, Long playerId);
}
