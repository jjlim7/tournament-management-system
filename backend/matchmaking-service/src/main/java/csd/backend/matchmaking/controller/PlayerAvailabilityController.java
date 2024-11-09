package csd.backend.matchmaking.controller;

import csd.backend.matchmaking.dto.Request;
import csd.backend.matchmaking.entity.PlayerAvailability;
import csd.backend.matchmaking.services.PlayerAvailabilityService;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

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

  @PostMapping("/bulkCreateByTimeRange")
  public ResponseEntity<List<PlayerAvailability>> bulkCreateAvailabilitiesByTimeRange(
          @Valid @RequestBody Request.BulkCreatePlayerAvailabilityByTimeRangeDto bulkRequest
          ) {
    OffsetDateTime start = bulkRequest.getStartTime();
    OffsetDateTime end = bulkRequest.getEndTime();
    long intervalInHours = bulkRequest.getInterval();

    List<PlayerAvailability> availabilities = new ArrayList<>();
    while (start.isBefore(end)) {
      OffsetDateTime intervalEnd = start.plusHours(intervalInHours);
      if (intervalEnd.isAfter(end)) {
        intervalEnd = end;
      }
      PlayerAvailability availability = new PlayerAvailability(
              bulkRequest.getPlayerId(),
              bulkRequest.getTournamentId(),
              start,
              intervalEnd,
              true  // Set availability to true; adjust as needed
      );
      availabilities.add(availability);
      start = intervalEnd;
    }

    List<PlayerAvailability> createdAvailabilities = playerAvailabilityService.bulkCreateAvailabilities(availabilities);
    return new ResponseEntity<>(createdAvailabilities, HttpStatus.CREATED);
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
