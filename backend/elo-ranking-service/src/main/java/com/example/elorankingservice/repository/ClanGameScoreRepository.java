package com.example.elorankingservice.repository;

import com.example.elorankingservice.entity.ClanGameScore;

import java.util.List;

public interface ClanGameScoreRepository extends BaseGameScoreRepository<ClanGameScore> {
    // No additional methods, inherits all methods from BaseGameScoreRepository
    List<ClanGameScore> findByClanEloRank_TournamentIdAndClanId(Long tournamentId, Long clanId);
}
