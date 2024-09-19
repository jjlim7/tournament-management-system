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

    // storing of player game score for battle royale
    @PostMapping("/battle-royale")
    public ResponseEntity<List<PlayerGameScore>> saveBattleRoyaleResult(@RequestBody Request.CreateBattleRoyalePlayerGameScore newGameScoresRequest) {
        List<PlayerGameScore> playerGameScores = gameScoreService.storeAllPlayerGameScore(newGameScoresRequest.getRawPlayerGameScores());
        return new ResponseEntity<>(playerGameScores, HttpStatus.CREATED);
    }

    // storing of player + clan game score for battle royale
    @PostMapping("/clan-war")
    public ResponseEntity<List<ClanGameScore>> saveClanWarResult(@RequestBody Request.CreateClanWarGameScore newClanWarRequest) {

        Long tournamentId = newClanWarRequest.getTournamentId();
        Long gameId = newClanWarRequest.getGameId();

        Map<Long, List<PlayerGameScore>> winnerRawPlayerGameScores = newClanWarRequest.getWinnerRawPlayerGameScores();
        Map<Long, List<PlayerGameScore>> loserRawPlayerGameScores = newClanWarRequest.getLoserRawPlayerGameScores();

        Long wClanId = winnerRawPlayerGameScores.keySet().stream().findFirst().get();
        Long lClanId = loserRawPlayerGameScores.keySet().stream().findFirst().get();

        List<ClanGameScore> result = new ArrayList<>();

        // store winner of clan war
        ClanGameScore wClanGameScore = gameScoreService.storeClanGameScore(tournamentId, gameId, wClanId, winnerRawPlayerGameScores.get(0), true);

        ClanGameScore lClanGameScore = gameScoreService.storeClanGameScore(tournamentId, gameId, lClanId, loserRawPlayerGameScores.get(0), false);

        result.add(wClanGameScore);
        result.add(lClanGameScore);

        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    // retrieve player game scores by tournament
    @GetMapping("/player/{playerId}/tournament/{tournamentId}")
    public ResponseEntity<PlayerGameScore> getPlayerGameScore(@PathVariable Long playerId, @PathVariable Long tournamentId) {
        List<PlayerGameScore> playerGameScores = gameScoreService.retrievePlayerGameScoresForTournament(tournamentId, playerId);
        return new ResponseEntity<>(playerGameScores.get(0), HttpStatus.OK);
    }

    // retrieve clan game scores by tournament
    @GetMapping("/clan/{clanId}/tournament/{tournamentId}")
    public ResponseEntity<ClanGameScore> getClanGameScore(@PathVariable Long clanId, @PathVariable Long tournamentId) {
        List<ClanGameScore> clanGameScore = gameScoreService.retrieveClanGameScoresForTournament(tournamentId, clanId);
        return new ResponseEntity<>(clanGameScore.get(0), HttpStatus.OK);
    }
}

