package csd.backend.matchmaking.controller;

import csd.backend.matchmaking.dto.Request;
import csd.backend.matchmaking.entity.PlayerAvailability;
import csd.backend.matchmaking.services.PlayerAvailabilityService;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/playersAvailability")
public class PlayerAvailabilityController {

  @Autowired
  private PlayerAvailabilityService playerAvailabilityService;

  @PostMapping
  public ResponseEntity<PlayerAvailability> createAvailability(
    @RequestBody PlayerAvailability playerAvailability
  ) {
    PlayerAvailability availability = playerAvailabilityService.createAvailability(
      playerAvailability
    );
    return new ResponseEntity<>(availability, HttpStatus.CREATED);
  }

  @PostMapping("/bulk")
  public ResponseEntity<List<PlayerAvailability>> bulkCreateAvailabilities(
    @RequestBody List<PlayerAvailability> playerAvailabilities
  ) {
    List<PlayerAvailability> availabilities = playerAvailabilityService.bulkCreateAvailabilities(
      playerAvailabilities
    );
    return new ResponseEntity<>(availabilities, HttpStatus.CREATED);
  }

  @GetMapping(params = "playerId")
  public ResponseEntity<List<PlayerAvailability>> getPlayerAvailabilitiesByPlayerId(
    @RequestParam("playerId") long playerId
  ) {
    return ResponseEntity.ok(
      playerAvailabilityService.getPlayerAvailabilitiesByPlayerId(playerId)
    );
  }

  @GetMapping(params = "tournamentId")
  public ResponseEntity<List<PlayerAvailability>> findPlayerAvailabilities(
    @RequestParam("tournamentId") long tournamentId
  ) {
    return ResponseEntity.ok(
      playerAvailabilityService.getPlayerAvailabilitiesByTournamentId(
        tournamentId
      )
    );
  }
}
