package com.example.elorankingservice.repository;

import com.example.elorankingservice.entity.ClanEloRank;
import com.example.elorankingservice.entity.PlayerEloRank;
import com.example.elorankingservice.entity.PlayerGameScore;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlayerGameScoreRepository extends BaseGameScoreRepository<PlayerGameScore> {
    // No additional methods, inherits all methods from BaseGameScoreRepository
    List<PlayerGameScore> findByPlayerEloRank_TournamentIdAndPlayerId(Long tournamentId, Long playerId);
}
