package csd.backend.matchmaking.controller;

import csd.backend.matchmaking.feigndto.PlayerEloRank;
import csd.backend.matchmaking.feigndto.PlayerGameScore;
import csd.backend.matchmaking.dto.Request;
import csd.backend.matchmaking.feigndto.Tournament;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import csd.backend.matchmaking.entity.Game;
import csd.backend.matchmaking.feignclient.EloRankingClient;
import csd.backend.matchmaking.feignclient.TournamentClient;
import csd.backend.matchmaking.services.MatchmakingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/matchmaking")
public class MatchmakingController {

    @Autowired
    private MatchmakingService matchmakingService;

    @Autowired
    private EloRankingClient eloRankingClient;

    @Autowired
    private TournamentClient tournamentClient;

    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping("/scheduleGames")
    public ResponseEntity<String> scheduleGames(@RequestParam long tournamentId) {
        if (matchmakingService.isTournamentAlreadyScheduled(tournamentId)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("The tournament has already been scheduled.");
        }

        ResponseEntity<Tournament> res = tournamentClient.getTournamentById(tournamentId);
        if (res.getStatusCode().is4xxClientError()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tournament ID " + tournamentId + " not found.");
        }

        List<Game> scheduledGames = null;
        try {
            scheduledGames = matchmakingService.scheduleGames(tournamentId, Objects.requireNonNull(res.getBody()).getGameMode().toString());
            if (scheduledGames.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Failed to schedule games for tournament " + tournamentId);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Internal Server Error. Failed to schedule games for tournament " + tournamentId + "\n" + e.getMessage());
        }

        return new ResponseEntity<>("Games scheduled for tournament " + tournamentId, HttpStatus.OK);
    }

    @GetMapping("/getPlayerGameScore")
    public ResponseEntity<List<PlayerGameScore>> getPlayerGamesScore() {
        Map<String, Object> res = eloRankingClient.getPlayerGameScores(1L, 1001L);
        Object obj = res.get("playerGameScores");
        List<PlayerGameScore> gameScores = objectMapper.convertValue(
                obj,
                new TypeReference<>() {
                }
        );

        return new ResponseEntity<>(gameScores, HttpStatus.OK);
    }

    @PostMapping("/getSelectedPlayerEloRanks")
    public ResponseEntity<List<PlayerEloRank>> getSelectedPlayerEloRanks(@RequestBody Request.GetSelectedPlayerEloRanks request) {
        Map<String, Object> res = eloRankingClient.getSelectedPlayerEloRanks(request);
        Object obj = res.get("playerEloRanks");
        List<PlayerEloRank> playerEloRanks = objectMapper.convertValue(
                obj,
                new TypeReference<>() {
                }
        );

        return new ResponseEntity<>(playerEloRanks, HttpStatus.OK);
    }
}
