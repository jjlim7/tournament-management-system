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

    @PostMapping("/battle-royale")
    public ResponseEntity<List<PlayerGameScore>> saveBattleRoyaleResult(
            @RequestBody Request.CreateBattleRoyalePlayerGameScore newGameScoresRequest) {

        // Check if the request is null or doesn't contain player game scores
        if (newGameScoresRequest == null || newGameScoresRequest.getRawPlayerGameScores() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // Store all player game scores
        List<PlayerGameScore> playerGameScores = gameScoreService
                .storeAllPlayerGameScore(newGameScoresRequest.getRawPlayerGameScores());

        // Return the list of stored player game scores with a created status
        return new ResponseEntity<>(playerGameScores, HttpStatus.CREATED);
    }

    @PostMapping("/clan-war")
    public ResponseEntity<List<ClanGameScore>> saveClanWarResult(@RequestBody Request.CreateClanWarGameScore newClanWarRequest) {

        // Check if the incoming request is not null and has necessary data
        if (newClanWarRequest == null ||
                newClanWarRequest.getWinnerRawPlayerGameScores() == null ||
                newClanWarRequest.getLoserRawPlayerGameScores() == null) {

            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Long tournamentId = newClanWarRequest.getTournamentId();
        Long gameId = newClanWarRequest.getGameId();


        // Ensure there is at least one winner and one loser clan ID
        Long winnerClanId = newClanWarRequest.getWinnerClanId();
        Long loserClanId = newClanWarRequest.getLoserClanId();

        List<ClanGameScore> result = new ArrayList<>();

        // Store the game scores for both winner and loser clans
        ClanGameScore winnerClanGameScore = gameScoreService.storeClanGameScore(
                tournamentId,
                gameId,
                winnerClanId,
                newClanWarRequest.getWinnerRawPlayerGameScores(),
                true
        );

        ClanGameScore loserClanGameScore = gameScoreService.storeClanGameScore(
                tournamentId,
                gameId,
                loserClanId,
                newClanWarRequest.getLoserRawPlayerGameScores(),
                false
        );

        result.add(winnerClanGameScore);
        result.add(loserClanGameScore);

        return new ResponseEntity<>(result, HttpStatus.CREATED);
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

