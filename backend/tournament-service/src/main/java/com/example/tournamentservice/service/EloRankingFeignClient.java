package com.example.tournamentservice.service;

import java.util.List;
import java.util.Optional;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.tournamentservice.DTO.ClanEloRankDTO;  // Create DTO classes as needed
import com.example.tournamentservice.DTO.PlayerEloRankDTO;

@FeignClient(name = "elo-ranking-service", url = "${elo.service.url}")
public interface EloRankingFeignClient {

    @GetMapping("/clan-elo/{clanId}/{tournamentId}")
    Optional<ClanEloRankDTO> getClanEloRank(
            @PathVariable("clanId") Long clanId,
            @PathVariable("tournamentId") Long tournamentId
    );

    @GetMapping("/player-elo/{playerId}/{tournamentId}")
    Optional<PlayerEloRankDTO> getPlayerEloRank(
            @PathVariable("playerId") Long playerId,
            @PathVariable("tournamentId") Long tournamentId
    );

    @GetMapping("/clan-elo/tournament/{tournamentId}")
    List<ClanEloRankDTO> getClanEloRanksByTournament(
            @PathVariable("tournamentId") Long tournamentId
    );

    @GetMapping("/player-elo/tournament/{tournamentId}")
    List<PlayerEloRankDTO> getAllPlayerEloRanksByTournament(
            @PathVariable("tournamentId") Long tournamentId
    );

    @GetMapping("/player-elo/tournament/{tournamentId}/selected")
    List<PlayerEloRankDTO> getSelectedPlayerEloRanksByTournament(
            @PathVariable("tournamentId") Long tournamentId,
            @RequestParam("playerIds") List<Long> playerIds
    );

    @GetMapping("/player-elo/tournament/{tournamentId}/rating-range")
    List<PlayerEloRankDTO> getPlayerEloRanksByRatingRange(
            @PathVariable("tournamentId") Long tournamentId,
            @RequestParam("minRating") double minRating,
            @RequestParam("maxRating") double maxRating
    );

    @GetMapping("/clan-elo/tournament/{tournamentId}/rating-range")
    List<ClanEloRankDTO> getClanEloRanksByRatingRange(
            @PathVariable("tournamentId") Long tournamentId,
            @RequestParam("minRating") double minRating,
            @RequestParam("maxRating") double maxRating
    );
}
