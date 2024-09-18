package com.example.elorankingservice.repository;

import com.example.elorankingservice.entity.ClanEloRank;
import com.example.elorankingservice.entity.PlayerEloRank;
import java.util.Optional;


public interface PlayerEloRankRepository extends BaseEloRankRepository<PlayerEloRank>{
    Optional<PlayerEloRank> findByPlayerIdAndTournamentId(Long playerId, Long tournamentId);

}
