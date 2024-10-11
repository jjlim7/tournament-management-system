package com.example.tournamentservice.controller;

import com.example.tournamentservice.service.TournamentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

import com.example.tournamentservice.entity.Tournament;

import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.List;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/tournaments")
public class TournamentController{

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
    public ResponseEntity<List<Tournament>> getAllTournament(){
        List<Tournament> tournaments = tournamentService.getAllTournaments();
        if (tournaments.isEmpty()) {
            System.out.println("No tournaments found");
        }
        return ResponseEntity.ok(tournaments);
    }

    @Operation(summary = "Get a tournament by ID", description = "Retrieve a specific tournament by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tournament retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Tournament not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Tournament> getTournamentById(@PathVariable Long id) {
        Tournament tournament = tournamentService.getTournamentById(id)
            .orElseThrow(() -> new RuntimeException("Tournament cannot be found"));
        return ResponseEntity.ok(tournament);
    }

    @Operation(summary = "Update a tournament", description = "Update a tournament by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tournament updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input provided"),
        @ApiResponse(responseCode = "404", description = "Tournament not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Tournament> updateTournament(@PathVariable Long id, @RequestBody Tournament tournament) {
        Tournament updatedTournament = tournamentService.updateTournament(id, tournament);
        return ResponseEntity.ok(updatedTournament);
    }
    
    @Operation(summary = "Delete a tournament", description = "Delete a tournament by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Tournament deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Tournament not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTournament(@PathVariable Long id) {
        tournamentService.deleteTournament(id);
        return ResponseEntity.noContent().build();
    }

    // @Operation(summary = "Join a tournament", description = "Allow a player to join a tournament")
    // @PostMapping("/{tournament_id}/join/{player_id}")
    // public ResponseEntity<String> joinTournament(
    //     @PathVariable Long tournament_id,
    //     @PathVariable Long player_id)
    // {
    //     String result = tournamentService.joinTournament(tournament_id, player_id);
    //     return ResponseEntity.ok(result);

    // }
    
    // @Operation(summary = "Leave a tournament", description = "Allows a player to leave a tournament.")
    // @PostMapping("/{tournament_id}/leave/{player_id}")
    // public ResponseEntity<String> leaveTournament(
    //     @PathVariable Long tournament_id,
    //     @PathVariable Long player_id)
        
    // {
    //     String result = tournamentService.leaveTournament(tournament_id, player_id);
    //     return ResponseEntity.ok(result);
    // }

}