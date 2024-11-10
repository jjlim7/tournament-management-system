package com.example.elorankingservice.repository;

import com.example.elorankingservice.entity.ClanEloRank;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClanEloRankRepository extends BaseEloRankRepository<ClanEloRank>{
    // Read
    Optional<ClanEloRank> findByClanIdAndTournamentId(Long clanId, Long tournamentId);
}
