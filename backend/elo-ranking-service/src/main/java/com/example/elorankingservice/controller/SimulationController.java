package com.example.elorankingservice.controller;

import com.example.elorankingservice.dto.Request;
import com.example.elorankingservice.entity.PlayerGameScore;
import com.example.elorankingservice.service.EloRankingService;
import com.example.elorankingservice.service.GameScoreService;
import com.example.elorankingservice.util.GameSimulator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/simulate")
public class SimulationController {
    private final GameSimulator simulator;
    private final GameScoreService gameScoreService;
    private final EloRankingService elorankingService;

    @Autowired
    public SimulationController(GameScoreService gss, EloRankingService ess) {
        simulator = new GameSimulator();
        gameScoreService = gss;
        elorankingService = ess;
    }


    @PostMapping("/battle-royale")
    public ResponseEntity<Object> simulateBattleRoyale(@RequestBody Request.SimulateBattleRoyale simulateReq) throws Exception {
        if (
                simulateReq == null ||
                simulateReq.getPlayerIds().isEmpty() ||
                simulateReq.getGameId() == null ||
                simulateReq.getTournamentId() == null
        ) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // simulate the game, generate the results
        List<Long> playerIds = simulateReq.getPlayerIds();
        Long tournamentId = simulateReq.getTournamentId();
        Long gameId = simulateReq.getGameId();

        List<PlayerGameScore> gameScores = simulator.generateBattleRoyalePlayerGameScores(playerIds, tournamentId, gameId);

        // store scores
        gameScoreService.storeAllPlayerGameScore(gameScores);

        // process result
        elorankingService.processUpdateBattleRoyaleResults(gameScores);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/clan-war")
    public ResponseEntity<Object> simulateClanWar(@RequestBody Request.SimulateClanWar simulateReq) throws Exception {
        if (
                simulateReq == null ||
                        simulateReq.getClanPlayerIds().isEmpty() ||
                        simulateReq.getTournamentId() == null ||
                        simulateReq.getGameId() == null
        ) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // simulate the game, generate the result
        Long tournamentId = simulateReq.getTournamentId();
        Long gameId = simulateReq.getGameId();
        Map<Long, List<Long>> clanPlayerIds = simulateReq.getClanPlayerIds();

        GameSimulator.ClanWarResult result = simulator.generateClanWarGameScores(gameId, tournamentId, clanPlayerIds);

        // store scores
        Map<Long, List<PlayerGameScore>> winnerClanPlayerResult = result.getWinnerRawPlayerGameScores();
        Map<Long, List<PlayerGameScore>> loserClanPlayerResult = result.getLoserRawPlayerGameScores();

        for (Map.Entry<Long, List<PlayerGameScore>> entry : winnerClanPlayerResult.entrySet()) {
            Long clanId = entry.getKey();
            List<PlayerGameScore> playerGameScores = entry.getValue();
            gameScoreService.storeClanGameScore(tournamentId, gameId, clanId, playerGameScores, true);
        }

        for (Map.Entry<Long, List<PlayerGameScore>> entry : loserClanPlayerResult.entrySet()) {
            Long clanId = entry.getKey();
            List<PlayerGameScore> playerGameScores = entry.getValue();
            gameScoreService.storeClanGameScore(tournamentId, gameId, clanId, playerGameScores, false);
        }

        // process scores
        elorankingService.processUpdateClanWarResults(winnerClanPlayerResult, loserClanPlayerResult);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
