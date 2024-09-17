package com.example.elorankingservice.repository;

import com.example.elorankingservice.entity.ClanEloRank;

import java.util.Optional;


public interface ClanEloRankRepository extends BaseEloRankRepository<ClanEloRank>{
    // Read
    Optional<ClanEloRank> findClanEloRankByClanIdAndTournamentId(Long clanId, Long tournamentId);
}
