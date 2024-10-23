package csd.backend.matchmaking.controller;

import csd.backend.matchmaking.dto.Request;
import csd.backend.matchmaking.entity.PlayerAvailability;
import csd.backend.matchmaking.services.PlayerAvailabilityService;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.validation.Valid;
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
          @Valid @RequestBody PlayerAvailability playerAvailability
  ) {
    PlayerAvailability availability = playerAvailabilityService.createAvailability(playerAvailability);
    return new ResponseEntity<>(availability, HttpStatus.CREATED);
  }

  @PostMapping("/bulk")
  public ResponseEntity<List<PlayerAvailability>> bulkCreateAvailabilities(
          @Valid @RequestBody List<PlayerAvailability> playerAvailabilities
  ) {
    List<PlayerAvailability> availabilities = playerAvailabilityService.bulkCreateAvailabilities(playerAvailabilities);
    return new ResponseEntity<>(availabilities, HttpStatus.CREATED);
  }

  @GetMapping(params = "playerId")
  public ResponseEntity<List<PlayerAvailability>> getPlayerAvailabilitiesByPlayerId(
          @RequestParam("playerId") long playerId
  ) {
    List<PlayerAvailability> availabilities = playerAvailabilityService.getPlayerAvailabilitiesByPlayerId(playerId);
    if (availabilities.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    return ResponseEntity.ok(availabilities);
  }

  @GetMapping(params = "tournamentId")
  public ResponseEntity<List<PlayerAvailability>> findPlayerAvailabilities(
          @RequestParam("tournamentId") long tournamentId
  ) {
    List<PlayerAvailability> availabilities = playerAvailabilityService.getPlayerAvailabilitiesByTournamentId(tournamentId);
    if (availabilities.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    return ResponseEntity.ok(availabilities);
  }
}
