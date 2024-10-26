package com.example.tournamentservice.controller;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.tournamentservice.entity.Tournament;
import com.example.tournamentservice.exception.TournamentsNotFoundException;
import com.example.tournamentservice.service.TournamentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/tournaments")
//@CrossOrigin(origins = "http://your-frontend-domain.com") //adjust accordingly
public class TournamentController {

    @Autowired
    private TournamentService tournamentService;

    @Operation(summary = "Create a tournament", description = "Create a new tournament")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Tournament created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input provided"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> createTournament(@Valid @RequestBody Tournament tournament) {
        try {
            tournamentService.createTournament(tournament);
            return ResponseEntity.status(HttpStatus.CREATED).body("Tournament created successfully!");
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid input: " + e.getMessage());
            return ResponseEntity.badRequest().body("Invalid input: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error creating tournament: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating tournament.");
        }
    }

    @Operation(summary = "Get all tournaments", description = "Retrieve a list of all tournaments.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved list of tournaments"),
        @ApiResponse(responseCode = "204", description = "No tournaments found")
    })
    @GetMapping
    public ResponseEntity<List<Tournament>> getAllTournaments() {
        List<Tournament> tournaments = tournamentService.getAllTournaments();
        return ResponseEntity.ok(tournaments);
    }

    @Operation(summary = "Get tournament by ID", description = "Retrieve a tournament by its ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved tournament"),
        @ApiResponse(responseCode = "404", description = "Tournament not found with the given ID")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Tournament> getTournamentById(@PathVariable Long id) {
        Tournament tournament = tournamentService.getTournamentById(id);
        return ResponseEntity.ok(tournament);
    }

    @Operation(summary = "Get tournament by name", description = "Retrieve a tournament by its name.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved tournament"),
        @ApiResponse(responseCode = "404", description = "Tournament not found with the given name")
    })
    @GetMapping("/name/{name}")
    public ResponseEntity<Tournament> getTournamentByName(@PathVariable String name) {
        Tournament tournament = tournamentService.getTournamentByName(name);
        return ResponseEntity.ok(tournament);
    }

    @Operation(summary = "Get tournaments by date range", description = "Retrieve tournaments within a specified date range.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved list of tournaments"),
        @ApiResponse(responseCode = "404", description = "No tournaments found in the given date range")
    })
    @GetMapping("/date-range")
    public ResponseEntity<List<Tournament>> getTournamentsByDateRange(
            @RequestParam OffsetDateTime startDate,
            @RequestParam OffsetDateTime endDate) {
        List<Tournament> tournaments = tournamentService.getTournamentsByDateRange(startDate, endDate);
        return ResponseEntity.ok(tournaments);
    }

    @Operation(summary = "Update a tournament", description = "Update a tournament by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tournament updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input provided"),
        @ApiResponse(responseCode = "404", description = "Tournament not found"),
        @ApiResponse(responseCode = "409", description = "Conflict: Cannot edit an active tournament")
    })
    @PutMapping("/{id}")
    public ResponseEntity<String> updateTournament(@PathVariable Long id, @RequestBody Tournament tournament) {
        try {
            Tournament updatedTournament = tournamentService.updateTournament(id, tournament);
            return ResponseEntity.ok("Tournament updated successfully! ID: " + updatedTournament.getTournament_id());
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (TournamentsNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating tournament: " + e.getMessage());
        }
    }

    @Operation(summary = "Delete a tournament", description = "Delete a tournament by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Tournament deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Tournament not found"),
        @ApiResponse(responseCode = "403", description = "Forbidden: Only the tournament creator can delete this tournament")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTournament(@PathVariable Long id, @RequestParam Long requestingAdminId) {
        try {
            tournamentService.deleteTournament(id, requestingAdminId);
            return ResponseEntity.noContent().build();
        } catch (TournamentsNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tournament with ID " + id + " not found.");
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unauthorized: Only the tournament creator can delete this tournament.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting tournament: " + e.getMessage());
        }
    }

    @Operation(summary = "Join a tournament", description = "Allow a player to join a tournament")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Player joined tournament successfully"),
        @ApiResponse(responseCode = "404", description = "Tournament not found"),
        @ApiResponse(responseCode = "400", description = "Player cannot join the tournament due to invalid conditions")
    })
    @PostMapping("/{tournament_id}/join/{player_id}")
    public ResponseEntity<String> joinTournament(@PathVariable Long tournament_id, @PathVariable Long player_id) {
        try {
            String result = tournamentService.joinTournament(tournament_id, player_id);
            return ResponseEntity.ok(result);
        } catch (TournamentsNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tournament with ID " + tournament_id + " not found.");
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }

    @Operation(summary = "Leave a tournament", description = "Allows a player to leave a tournament.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Player left tournament successfully"),
        @ApiResponse(responseCode = "404", description = "Tournament not found"),
        @ApiResponse(responseCode = "400", description = "Player is not part of this tournament")
    })
    @PostMapping("/{tournament_id}/leave/{player_id}")
    public ResponseEntity<String> leaveTournament(@PathVariable Long tournament_id, @PathVariable Long player_id) {
        try {
            String result = tournamentService.leaveTournament(tournament_id, player_id);
            return ResponseEntity.ok(result);
        } catch (TournamentsNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tournament with ID " + tournament_id + " not found.");
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }
}
