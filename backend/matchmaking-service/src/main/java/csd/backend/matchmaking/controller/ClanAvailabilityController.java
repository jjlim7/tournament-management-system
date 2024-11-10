package csd.backend.matchmaking.controller;

import csd.backend.matchmaking.dto.Request;
import csd.backend.matchmaking.entity.ClanAvailability;
import csd.backend.matchmaking.entity.PlayerAvailability;
import csd.backend.matchmaking.services.ClanAvailabilityService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/clanAvailability")
public class ClanAvailabilityController {

    @Autowired
    private ClanAvailabilityService clanAvailabilityService;

    // Create Clan Availability
    @PostMapping
    public ResponseEntity<ClanAvailability> createClanAvailability(
            @Valid @RequestBody ClanAvailability clanAvailability) {
        ClanAvailability createdAvailability = clanAvailabilityService.createClanAvailability(clanAvailability);
        return new ResponseEntity<>(createdAvailability, HttpStatus.CREATED);
    }

    // Bulk Create Clan Availabilities
    @PostMapping("/bulk")
    public ResponseEntity<List<ClanAvailability>> bulkCreateClanAvailabilities(
            @Valid @RequestBody List<ClanAvailability> clanAvailabilities) {
        List<ClanAvailability> createdAvailabilities = clanAvailabilityService.bulkCreateClanAvailabilities(clanAvailabilities);
        return new ResponseEntity<>(createdAvailabilities, HttpStatus.CREATED);
    }

    @DeleteMapping("/{clanId}")
    public ResponseEntity<String> deleteClanAvailability(@PathVariable long clanId) {
        clanAvailabilityService.deleteAvailability(clanId);
        return new ResponseEntity<>("Clan availability with ID " + clanId + " deleted successfully.", HttpStatus.OK);
    }

    @PostMapping("/bulkCreateByTimeRange")
    public ResponseEntity<List<ClanAvailability>> bulkCreateAvailabilitiesByTimeRange(
            @Valid @RequestBody Request.BulkCreateClanAvailabilityByTimeRangeDto bulkRequest
    ) {
        OffsetDateTime start = bulkRequest.getStartTime();
        OffsetDateTime end = bulkRequest.getEndTime();
        long intervalInHours = bulkRequest.getInterval();

        List<ClanAvailability> availabilities = new ArrayList<>();
        List<Long> playerIds = bulkRequest.getPlayerIds();  // Assume player IDs are provided in the request

        while (start.isBefore(end)) {
            OffsetDateTime intervalEnd = start.plusHours(intervalInHours);
            if (intervalEnd.isAfter(end)) {
                intervalEnd = end;
            }

            // Create a ClanAvailability entry for each player in the list
            for (Long playerId : playerIds) {
                ClanAvailability availability = new ClanAvailability(
                        bulkRequest.getClanId(),
                        playerId,
                        bulkRequest.getTournamentId(),
                        start,
                        intervalEnd,
                        true  // Set availability to true; adjust as needed
                );
                availabilities.add(availability);
            }

            start = intervalEnd;
        }

        List<ClanAvailability> createdAvailabilities = clanAvailabilityService.bulkCreateClanAvailabilities(availabilities);
        return new ResponseEntity<>(createdAvailabilities, HttpStatus.CREATED);
    }


    @PutMapping("/bulkUpdateByTimeRange")
    public ResponseEntity<List<ClanAvailability>> bulkUpdateAvailabilities(
            @Valid @RequestBody Request.BulkCreateClanAvailabilityByTimeRangeDto bulkRequest) {
        OffsetDateTime start = bulkRequest.getStartTime();
        OffsetDateTime end = bulkRequest.getEndTime();
        long intervalInHours = bulkRequest.getInterval();

        List<ClanAvailability> availabilities = new ArrayList<>();
        List<Long> playerIds = bulkRequest.getPlayerIds();  // Assume player IDs are provided in the request

        while (start.isBefore(end)) {
            OffsetDateTime intervalEnd = start.plusHours(intervalInHours);
            if (intervalEnd.isAfter(end)) {
                intervalEnd = end;
            }

            // Iterate over each player to check or create their availability
            for (Long playerId : playerIds) {
                // Check if availability for this player and interval already exists
                boolean exists = clanAvailabilityService.existsByClanIdAndPlayerIdAndTournamentIdAndTimeRange(
                        bulkRequest.getClanId(), playerId, bulkRequest.getTournamentId(), start, intervalEnd);

                if (!exists) {
                    // Create new availability for the player if it doesn't exist
                    ClanAvailability availability = new ClanAvailability(
                            bulkRequest.getClanId(),
                            playerId,
                            bulkRequest.getTournamentId(),
                            start,
                            intervalEnd,
                            true  // Set availability to true; adjust as needed
                    );
                    availabilities.add(availability);
                }
            }

            start = intervalEnd;
        }

        List<ClanAvailability> updatedAvailabilities = clanAvailabilityService.bulkCreateClanAvailabilities(availabilities);
        return new ResponseEntity<>(updatedAvailabilities, HttpStatus.CREATED);
    }

    // Get Clan Availabilities by Clan ID
    @GetMapping(params = "clanId")
    public ResponseEntity<List<ClanAvailability>> getClanAvailabilitiesByClanId(@RequestParam("clanId") Long clanId) {
        List<ClanAvailability> availabilities = clanAvailabilityService.getClanAvailabilitiesByClanId(clanId);
        if (availabilities.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(availabilities);
    }

    // Get Clan Availabilities by Tournament ID
    @GetMapping(params = "tournamentId")
    public ResponseEntity<List<ClanAvailability>> getClanAvailabilitiesByTournamentId(@RequestParam("tournamentId") Long tournamentId) {
        List<ClanAvailability> availabilities = clanAvailabilityService.getClanAvailabilitiesByTournamentId(tournamentId);
        if (availabilities.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(availabilities);
    }

    // Get Clan Availability by Clan ID and Tournament ID
    @GetMapping(params = { "clanId", "tournamentId" })
    public ResponseEntity<List<ClanAvailability>> getClanAvailabilitiesByClanAndTournament(
            @RequestParam("clanId") Long clanId, @RequestParam("tournamentId") Long tournamentId) {
        List<ClanAvailability> availabilities = clanAvailabilityService.getClanAvailabilitiesByClanAndTournament(clanId, tournamentId);
        if (availabilities.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(availabilities);
    }

    // Update Clan Availability (if you want to support update functionality)
    @PutMapping("/{id}")
    public ResponseEntity<ClanAvailability> updateClanAvailability(@PathVariable Long id, @Valid @RequestBody ClanAvailability clanAvailability) {
        ClanAvailability updatedAvailability = clanAvailabilityService.updateClanAvailability(id, clanAvailability);
        return ResponseEntity.ok(updatedAvailability);
    }
}