package csd.backend.matchmaking.controller;

import csd.backend.matchmaking.entity.Game;
import csd.backend.matchmaking.repository.GameRepository;
import csd.backend.matchmaking.repository.PlayerAvailabilityRepository;
import csd.backend.matchmaking.entity.Game;
import csd.backend.matchmaking.services.GameService;
import csd.backend.matchmaking.services.MatchmakingService;
import csd.backend.matchmaking.services.PlayerAvailabilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/matchmaking")
public class MatchmakingController {

    @Autowired
    private MatchmakingService matchmakingService;

    @PostMapping("/schedule-games")
    public ResponseEntity<String> scheduleGames(@RequestParam long tournamentId) {
        if (matchmakingService.isTournamentAlreadyScheduled(tournamentId)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("The tournament has already been scheduled.");
        }

        List<Game> scheduledGames = null;
        try {
            scheduledGames = matchmakingService.scheduleGames(tournamentId, Game.GameMode.BATTLE_ROYALE);
            if (scheduledGames.isEmpty()) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Failed to schedule games for tournament " + tournamentId);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return new ResponseEntity<>("Games scheduled for tournament " + tournamentId, HttpStatus.CREATED);
    }
}
