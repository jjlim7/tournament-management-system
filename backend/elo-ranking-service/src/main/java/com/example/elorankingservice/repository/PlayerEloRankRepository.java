package com.example.elorankingservice.repository;

import com.example.elorankingservice.entity.PlayerEloRank;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlayerEloRankRepository extends BaseEloRankRepository<PlayerEloRank>{
    Optional<PlayerEloRank> findByPlayerIdAndTournamentId(Long playerId, Long tournamentId);

}
