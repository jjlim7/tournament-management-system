package com.example.tournamentservice.service;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

import javax.swing.text.html.parser.Entity;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.tournamentservice.DTO.EntityResponseDTO;
import com.example.tournamentservice.DTO.GameDTO;
import com.example.tournamentservice.DTO.PlayerAvailabilityDTO;
import com.example.tournamentservice.entity.Tournament;
import com.example.tournamentservice.entity.Tournament.GameMode;
import com.example.tournamentservice.entity.Tournament.Status;


@FeignClient(name = "matchmaking-service", url = "${matchmaking.service.url}")
public interface MatchMakingFeignClient {


    @GetMapping("/games/{gameId}/players")
    List<Long> getPlayerIdsByGame(@PathVariable("gameId") Long gameId);

    @GetMapping("/tournaments/{tournamentId}/games")
    List<GameDTO> getGamesByTournament(@PathVariable("tournamentId") Long tournamentId);

    @GetMapping("/games/{gameId}")
    GameDTO getGameById(@PathVariable("gameId") Long gameId);

    @GetMapping("/games/{gameId}/player-or-clan-ids")
    EntityResponseDTO.EntityIdResponse getGamePlayerOrClanIds(@PathVariable("gameId") long gameId);

    @PostMapping("/tournaments/{tournamentId}/games/schedule")
    List<GameDTO> scheduleGames(@PathVariable("tournamentId") long tournamentId, @RequestParam("gameMode") Tournament.GameMode gameMode);

    @PostMapping("/availabilities/group-by-start")
    Map<OffsetDateTime, List<PlayerAvailabilityDTO>> groupAvailabilitiesByStartTime(@RequestBody List<PlayerAvailabilityDTO> availabilities);

    @PostMapping("/games/schedule")
    GameDTO createGame(
        @RequestParam("tournamentId") long tournamentId, 
        @RequestParam("startTime") OffsetDateTime startTime, 
        @RequestParam("endTime") OffsetDateTime endTime, 
        @RequestParam("gameMode") Tournament.GameMode gameMode, 
        @RequestBody List<PlayerAvailabilityDTO> availablePlayers
    );

    @GetMapping("/players/{playerId}/availabilities")
    List<PlayerAvailabilityDTO> getPlayerAvailabilitiesByPlayerId(@PathVariable("playerId") long playerId);

    @GetMapping("/tournaments/{tournamentId}/availabilities")
    List<PlayerAvailabilityDTO> getPlayerAvailabilitiesByTournamentId(@PathVariable("tournamentId") long tournamentId);
}
