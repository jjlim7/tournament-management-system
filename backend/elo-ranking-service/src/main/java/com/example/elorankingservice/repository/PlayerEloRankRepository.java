package com.example.elorankingservice.repository;

import com.example.elorankingservice.entity.PlayerEloRank;

public interface PlayerEloRankRepository extends BaseEloRankRepository<PlayerEloRank>{
    PlayerEloRank findByPlayerEloRankByPlayerIdAndTournamentId(Long playerId, Long tournamentId);
}
