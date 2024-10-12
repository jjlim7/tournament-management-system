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
    public ResponseEntity<List<PlayerGameScore>> getPlayerGameScores(
            @PathVariable Long playerId, @PathVariable Long tournamentId) {

        // Retrieve the list of player game scores
        List<PlayerGameScore> playerGameScores = gameScoreService.retrievePlayerGameScoresForTournament(tournamentId, playerId);

        // Return the list of player game scores with an OK status
        return ResponseEntity.ok(playerGameScores);
    }

    // Retrieve clan game scores by tournament
    @GetMapping("/clan/{clanId}/tournament/{tournamentId}")
    public ResponseEntity<List<ClanGameScore>> getClanGameScores(
            @PathVariable Long clanId, @PathVariable Long tournamentId) {

        // Retrieve the list of clan game scores
        List<ClanGameScore> clanGameScores = gameScoreService.retrieveClanGameScoresForTournament(tournamentId, clanId);

        // Return the list of clan game scores with an OK status
        return ResponseEntity.ok(clanGameScores);
    }
}

