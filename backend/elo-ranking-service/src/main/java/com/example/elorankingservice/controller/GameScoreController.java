package com.example.elorankingservice.controller;

import com.example.elorankingservice.dto.Request;
import com.example.elorankingservice.entity.ClanGameScore;
import com.example.elorankingservice.entity.PlayerGameScore;
import com.example.elorankingservice.service.GameScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/game-score")
public class GameScoreController {

    private final GameScoreService gameScoreService;

    @Autowired
    public GameScoreController(GameScoreService gameScoreService) {
        this.gameScoreService = gameScoreService;
    }

    // Retrieve player game scores by tournament
    @GetMapping("/player/{playerId}/tournament/{tournamentId}")
    public ResponseEntity<Map<String, Object>> getPlayerGameScores(
            @PathVariable Long playerId, @PathVariable Long tournamentId) {

        if (playerId == null || tournamentId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "status", "failed",
                    "message", "missing params"
            ));
        }
        // Retrieve the list of player game scores
        try {
            List<PlayerGameScore> playerGameScores = gameScoreService.retrievePlayerGameScoresForTournament(tournamentId, playerId);
            return ResponseEntity.status(HttpStatus.OK).body(
                    Map.of("status", "success","playerGameScores", playerGameScores)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("status", "failed", "message", e.getMessage()));
        }
    }

    // Retrieve clan game scores by tournament
    @GetMapping("/clan/{clanId}/tournament/{tournamentId}")
    public ResponseEntity<Map<String, Object>> getClanGameScores(
            @PathVariable Long clanId, @PathVariable Long tournamentId) {

        if (tournamentId == null || clanId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "status", "failed",
                    "message", "missing params"
            ));
        }
        try {
            List<ClanGameScore> clanGameScores = gameScoreService.retrieveClanGameScoresForTournament(tournamentId, clanId);
            return ResponseEntity.status(HttpStatus.OK).body(
                    Map.of("status", "success","clanGameScores", clanGameScores)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("status", "failed", "message", e.getMessage()));
        }
    }
}

