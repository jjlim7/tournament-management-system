package csd.backend.matchmaking.controller;

import csd.backend.matchmaking.dto.Request;
import csd.backend.matchmaking.entity.PlayerAvailability;
import csd.backend.matchmaking.services.PlayerAvailabilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/players/availability")
public class PlayerAvailabilityController {

    @Autowired
    private PlayerAvailabilityService playerAvailabilityService;

    @PostMapping
    public ResponseEntity<PlayerAvailability> createAvailability(@RequestBody PlayerAvailability playerAvailability) {
        PlayerAvailability availability = playerAvailabilityService.createAvailability(playerAvailability);
        return new ResponseEntity<>(availability, HttpStatus.CREATED);
    }

    @GetMapping(params = "playerId")
    public ResponseEntity<List<PlayerAvailability>> getPlayerAvailabilitiesByPlayerId(@RequestParam("playerId") long playerId) {
        return ResponseEntity.ok(playerAvailabilityService.getPlayerAvailabilitiesByPlayerId(playerId));
    }

    @GetMapping(params = "tournamentId")
    public ResponseEntity<List<PlayerAvailability>> findPlayerAvailabilities(@RequestParam("tournamentId") long tournamentId) {
        return ResponseEntity.ok(playerAvailabilityService.getPlayerAvailabilitiesByTournamentId(tournamentId));
    }
}

