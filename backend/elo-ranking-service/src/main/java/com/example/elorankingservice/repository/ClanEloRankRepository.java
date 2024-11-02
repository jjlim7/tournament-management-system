package com.example.elorankingservice.repository;

import com.example.elorankingservice.entity.ClanEloRank;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClanEloRankRepository extends BaseEloRankRepository<ClanEloRank>{
    // Read
    Optional<ClanEloRank> findByClanIdAndTournamentId(Long clanId, Long tournamentId);

    // Method to fetch the ClanEloRank by the latest tournamentId
    @Query("SELECT p FROM ClanEloRank p WHERE p.clanId = :clanId ORDER BY p.tournamentId DESC limit 1")
    Optional<ClanEloRank> findTopByClanIdOrderByTournamentIdDesc(@Param("clanId") Long clanId);
}
