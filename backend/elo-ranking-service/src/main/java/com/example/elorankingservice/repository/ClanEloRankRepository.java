package com.example.elorankingservice.repository;

import com.example.elorankingservice.entity.ClanEloRank;


public interface ClanEloRankRepository extends BaseEloRankRepository<ClanEloRank>{
    // Read
    ClanEloRank findClanEloRankByClanIdAndTournamentId(Long clanId, Long tournamentId);
}
