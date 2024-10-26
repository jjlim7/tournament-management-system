package com.example.elorankingservice.controller;

import com.example.elorankingservice.dto.Request;
import com.example.elorankingservice.entity.ClanEloRank;
import com.example.elorankingservice.entity.PlayerEloRank;
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
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/elo-ranking/simulate")
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
    public ResponseEntity<Map<String, Object>> simulateBattleRoyale(@RequestBody Request.SimulateBattleRoyale simulateReq) {
        if (simulateReq == null || simulateReq.getPlayerIds().isEmpty() ||
                simulateReq.getGameId() == null || simulateReq.getTournamentId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    Map.of("status", "error", "message", "missing required fields"));
        }

        List<Long> playerIds = simulateReq.getPlayerIds();
        Long tournamentId = simulateReq.getTournamentId();
        Long gameId = simulateReq.getGameId();
        try {
            // Simulate the game and generate player scores
            List<PlayerGameScore> gameScores = simulator.generateBattleRoyalePlayerGameScores(playerIds, gameId, tournamentId);

            // Assign Elo ranks to the player game scores
            for (PlayerGameScore gameScore : gameScores) {
                Optional<PlayerEloRank> playerEloRank = elorankingService.retrievePlayerEloRank(gameScore.getPlayerId(), tournamentId);
                if (playerEloRank.isEmpty()) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                            Map.of("status", "not found", "message", "missing elo rank for playerId: " + gameScore.getPlayerId() + " in tournamentId: " + tournamentId));
                }
                gameScore.setPlayerEloRank(playerEloRank.get());
            }

            // Store game scores and process the results
            gameScoreService.storeAllPlayerGameScore(gameScores);
            System.out.println("Finished storing");
            elorankingService.processUpdateBattleRoyaleResults(gameScores);
            System.out.println("Process results");

            return ResponseEntity.status(HttpStatus.CREATED).body(
                    Map.of("status", "success", "message", "Simulated Battle Royale successfully"));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    Map.of("status", "error", "message", e.getMessage()));
        }
    }


    @PostMapping("/clan-war")
    public ResponseEntity<Map<String, Object>> simulateClanWar(@RequestBody Request.SimulateClanWar simulateReq) {
        if (simulateReq == null || simulateReq.getClanPlayerIds().isEmpty() ||
                simulateReq.getTournamentId() == null || simulateReq.getGameId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    Map.of("status", "error", "message", "missing required fields"));
        }

        Long tournamentId = simulateReq.getTournamentId();
        Long gameId = simulateReq.getGameId();
        Map<Long, List<Long>> clanPlayerIds = simulateReq.getClanPlayerIds();

        try {
            // Simulate the game and generate the results
            GameSimulator.ClanWarResult result = simulator.generateClanWarGameScores(gameId, tournamentId, clanPlayerIds);

            // Update player Elo ranks for winner and loser clans
            updatePlayerEloRanks(result.getWinnerRawPlayerGameScores(), tournamentId);
            updatePlayerEloRanks(result.getLoserRawPlayerGameScores(), tournamentId);

            // Store scores for both winner and loser clans
            storeClanGameScores(result.getWinnerRawPlayerGameScores(), tournamentId, gameId, true);
            storeClanGameScores(result.getLoserRawPlayerGameScores(), tournamentId, gameId, false);

            // Process the Elo ranking updates for the clan war
            elorankingService.processUpdateClanWarResults(result.getWinnerRawPlayerGameScores(), result.getLoserRawPlayerGameScores());

            return ResponseEntity.status(HttpStatus.CREATED).body(
                    Map.of("status", "success", "message", "Simulated Clan War successfully"));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    Map.of("status", "error", "message", e.getMessage()));
        }
    }

    private void updatePlayerEloRanks(Map<Long, List<PlayerGameScore>> clanPlayerResult, Long tournamentId) throws Exception {
        for (Map.Entry<Long, List<PlayerGameScore>> entry : clanPlayerResult.entrySet()) {
            for (PlayerGameScore playerGameScore : entry.getValue()) {
                Long playerId = playerGameScore.getPlayerId();
                Optional<PlayerEloRank> playerEloRank = elorankingService.retrievePlayerEloRank(playerId, tournamentId);
                if (playerEloRank.isEmpty()) {
                    throw new Exception("Missing Elo rank for playerId: " + playerId + " in tournamentId: " + tournamentId);
                }
                playerGameScore.setPlayerEloRank(playerEloRank.get());
            }
        }
    }

    private void storeClanGameScores(Map<Long, List<PlayerGameScore>> clanPlayerResult, Long tournamentId, Long gameId, boolean isWinner) throws Exception {
        for (Map.Entry<Long, List<PlayerGameScore>> entry : clanPlayerResult.entrySet()) {
            Long clanId = entry.getKey();
            List<PlayerGameScore> playerGameScores = entry.getValue();
            Optional<ClanEloRank> currentRank = elorankingService.retrieveClanEloRank(clanId, tournamentId);
            if (currentRank.isEmpty()) {
                throw new Exception("Missing Elo rank for clanId: " + clanId + " in tournamentId: " + tournamentId);
            }
            gameScoreService.storeClanGameScore(tournamentId, gameId, clanId, playerGameScores, currentRank.get(), isWinner);
        }
    }
}
