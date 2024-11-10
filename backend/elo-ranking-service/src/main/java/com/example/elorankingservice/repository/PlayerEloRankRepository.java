package com.example.elorankingservice.repository;

import com.example.elorankingservice.entity.PlayerEloRank;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlayerEloRankRepository extends BaseEloRankRepository<PlayerEloRank>{

    // Existing method for fetching a single Player EloRank
    Optional<PlayerEloRank> findByPlayerIdAndTournamentId(Long playerId, Long tournamentId);

    // New method for fetching multiple Player EloRanks
    List<PlayerEloRank> findByPlayerIdInAndTournamentId(List<Long> playerIds, Long tournamentId);
}