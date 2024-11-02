package csd.backend.matchmaking.controller;

import csd.backend.matchmaking.entity.ClanAvailability;
import csd.backend.matchmaking.services.ClanAvailabilityService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    // Delete Clan Availability
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClanAvailability(@PathVariable Long id) {
        clanAvailabilityService.deleteClanAvailability(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}