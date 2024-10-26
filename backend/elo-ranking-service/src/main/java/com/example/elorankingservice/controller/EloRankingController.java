package com.example.elorankingservice.controller;

import com.example.elorankingservice.dto.Request;
import com.example.elorankingservice.entity.ClanEloRank;
import com.example.elorankingservice.entity.PlayerEloRank;
import com.example.elorankingservice.repository.PlayerEloRankRepository;
import com.example.elorankingservice.service.EloRankingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.PublicKey;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/elo-ranking/")
public class EloRankingController {

    private final EloRankingService eloRankingService;

    @Autowired
    public EloRankingController(EloRankingService eloRankingService, PlayerEloRankRepository playerEloRankRepository) {
        this.eloRankingService = eloRankingService;
    }

    @GetMapping("/player/{playerId}/tournament/{tournamentId}")
    public ResponseEntity<PlayerEloRank> getPlayerEloRank(
            @PathVariable Long playerId,
            @PathVariable Long tournamentId) {

        return eloRankingService.retrievePlayerEloRank(playerId, tournamentId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/clan/{clanId}/tournament/{tournamentId}")
    public ResponseEntity<ClanEloRank> getClanEloRank(
            @PathVariable Long clanId,
            @PathVariable Long tournamentId) {

        return eloRankingService.retrieveClanEloRank(clanId, tournamentId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/clan/tournament/{tournamentId}")
    public ResponseEntity<Map<String, Object>> getAllClanEloRanks(@PathVariable Long tournamentId) {
        try {
            List<ClanEloRank> clanEloRanks = eloRankingService.retrieveClanEloRanksByTournament(tournamentId);
            return ResponseEntity.status(HttpStatus.OK).body(
                    Map.of("status", "success", "clanEloRanks", clanEloRanks)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    Map.of("status", "error", "message", e.getMessage())
            );
        }
    }

    @GetMapping("/player/tournament/{tournamentId}")
    public ResponseEntity<Map<String, Object>> getAllPlayerEloRanks(@PathVariable Long tournamentId) {
        try {
            List<PlayerEloRank> playerEloRanks = eloRankingService.retrieveAllPlayerEloRanksByTournament(tournamentId);
            return ResponseEntity.status(HttpStatus.OK).body(
                    Map.of("status", "success", "playerEloRanks", playerEloRanks)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    Map.of("status", "error", "message", e.getMessage())
            );
        }
    }

    @PostMapping("/player/tournament")
    public ResponseEntity<Map<String, Object>> getSelectedPlayerEloRanks(@RequestBody Request.GetSelectedPlayerEloRanks req) {
        if (
                req.getPlayerIds() == null ||
                req.getPlayerIds().isEmpty() ||
                req.getTournamentId() == null
        ) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    Map.of("status", "error", "message", "missing required fields"));
        }
        try {
            List<PlayerEloRank> playerEloRanks = eloRankingService.retrieveSelectedPlayerEloRanksByTournament(req.getPlayerIds(), req.getTournamentId());
            return ResponseEntity.status(HttpStatus.OK).body(
                    Map.of("status", "success", "playerEloRanks", playerEloRanks)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    Map.of("status", "error", "message", e.getMessage())
            );
        }
    }

    @PostMapping("/clan")
    public ResponseEntity<Map<String, Object>> createClanEloRank(
            @RequestBody Request.CreateNewClanEloRank newClanEloRankRequest) {
        try {
            ClanEloRank newEloRank = eloRankingService.createNewClanEloRanking(
                    newClanEloRankRequest.getClanId(),
                    newClanEloRankRequest.getTournamentId());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("status", "success", "message", "Clan Elo rank created successfully", "result", newEloRank));
        } catch (IllegalArgumentException e) {
            // Handle the case where the clan already has an Elo ranking
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("status", "error", "message", "Clan already has an Elo ranking"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("status", e, "message", "An internal error occurred"));
        }
    }

    @PostMapping("/player")
    public ResponseEntity<Map<String, Object>> createPlayerEloRank(
            @RequestBody Request.CreateNewPlayerEloRank newPlayerEloRankRequest) {
        try {
            PlayerEloRank newEloRank = eloRankingService.createNewPlayerEloRanking(
                    newPlayerEloRankRequest.getPlayerId(),
                    newPlayerEloRankRequest.getTournamentId());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("status", "success", "message", "Player Elo rank created successfully", "result", newEloRank));
        } catch (IllegalArgumentException e) {
            // Handle the case where the player already has an Elo ranking
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("status", "error", "message", "Player already has an Elo ranking"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("status", "error", "message", "An internal error occurred"));
        }
    }

    @PostMapping("/player/bulk")
    public ResponseEntity<Map<String, String>> bulkCreatePlayerEloRank(
            @RequestBody Request.BulkCreatePlayerEloRank bulkPlayersEloRankRequest) {
        try {
            Long tournamentId = bulkPlayersEloRankRequest.getTournamentId();
            List<Long> playerIds = bulkPlayersEloRankRequest.getPlayerIds();

            List<PlayerEloRank> result = eloRankingService.bulkCreatePlayerEloRanks(playerIds, tournamentId);

            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("status", "success", "message", "Elo ranks created successfully"));
        } catch (IllegalArgumentException e) {
            // Return a conflict response with a message
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("status", "error", "message", "One or more players already have an Elo ranking"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("status", "error", "message", "An internal error occurred"));
        }
    }

    @PostMapping("/clan/bulk")
    public ResponseEntity<List<ClanEloRank>> bulkCreateClanEloRank(
            @RequestBody Request.BulkCreateClanEloRank bulkClanEloRankRequest) {
        try {
            Long tournamentId = bulkClanEloRankRequest.getTournamentId();
            List<Long> clanIds = bulkClanEloRankRequest.getClanIds();

            // Call the service method to perform bulk creation
            List<ClanEloRank> result = eloRankingService.bulkCreateClanEloRanks(clanIds, tournamentId);

            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (IllegalArgumentException e) {
            // Return a conflict response if any clan already has an Elo ranking
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(null);  // Optionally, return an error message
        } catch (Exception e) {
            // Return an internal server error for unexpected issues
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
